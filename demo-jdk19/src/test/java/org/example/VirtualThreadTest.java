package org.example;


import org.hzhq.myutil.utils.HttpClientUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class VirtualThreadTest {

    private static final Logger LOGGER = Logger.getLogger("virtual test");

    @Test
    public void VirtualThreadCreateTest1() {

        var platformThread = Thread.ofPlatform()
                .unstarted(() -> System.out.printf("platform thread id = %s\n", Thread.currentThread()));

        var virtualThread = Thread.ofVirtual()
                .unstarted(() -> System.out.printf("virtual thread id = %s \n", Thread.currentThread()));

        platformThread.start();
        virtualThread.start();
    }

    @Test
    public void VirtualThreadExecuteTest1(){

        try (var scheduledExecutorService= Executors.newScheduledThreadPool(1)){
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
                ThreadInfo[] threadInfo = threadBean.dumpAllThreads(false, false);
                System.out.println(threadInfo.length + " os thread");
            }, 10, 10, TimeUnit.MILLISECONDS);
        }

        long startTime = System.currentTimeMillis();
        try (var executor = Executors.newFixedThreadPool(200)){
            IntStream.range(0, 10000).forEach(i->{
                executor.submit(()->{
                    Thread.sleep(Duration.ofSeconds(1));
//                    System.out.println(i);
                    return i;
                });
            });
        }
        System.out.printf("physical thread spent time is %sms\n", System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()){
            IntStream.range(0, 10000).forEach(i->{
                executor.submit(()->{
                    Thread.sleep(Duration.ofSeconds(1));
//                    System.out.println(i);
                    return i;
                });
            });
        }
        System.out.printf("virtual thread spent time is %sm\n", System.currentTimeMillis() - startTime);

    }


    @Test
    public void VirtualRequestK8s() {
        // 4542
        List<Future<String>> futures = new ArrayList<>();
        String namespaceStr = "ack-agility-test,acs-harbor,acs-minio,acs-system,caas-system,caas-terminal,cicd,cluster-g,cluster-local,default,default-tenant-ns-1,default-tenant-ns-2,default-tenant-ns-3,emissary-system,kube-node-lease,kube-public,kube-system,monitoring,ns1,ocm-deployer,open-cluster-management,open-cluster-management-addon,open-cluster-management-agent,open-cluster-management-agent-addon,open-cluster-management-cluster-proxy,open-cluster-management-credentials,open-cluster-management-hub,open-cluster-management-managed-serviceaccount,terminal-test,test,test-cluster,vela-system";
        String[] namespaces = namespaceStr.split(",");
        String urlFormat = "https://127.0.0.1:6443/api/v1/namespaces/%s/pods?limit=500";
        final List<String> bodyList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        Set<String> idSet = new HashSet<>();
//        try (var executor = Executors.newFixedThreadPool(8)) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            var headers = new HashMap<String, String>();
            headers.put("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IlhxZXlRZmsxclZ0SGxuRFR6OE0waEczT1VzeXdWRy1Ed0k4dmhEMlV2MmMifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJkZWZhdWx0LXRva2VuLXo0dGNjIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImRlZmF1bHQiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI5YTNmNjRiNy1kZDkzLTRiNGYtYTY0YS1kYmU2ODUzMzI1MTgiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZS1zeXN0ZW06ZGVmYXVsdCJ9.ZcqY0N5Eqf_-lF8sU7QlWWJgn2G02NSc4PO6MXhxz2TTUtDN2c3UTfReIaWzDhFLoRK6X32ITNMSBB6l_K-ErY8OUfzM2UCnVyfy9q_2KXo_iH4uxiMVfmFYkX2OoTN__g3z-KJsjKeSO4-_x2h1APYYqWlh4qxKf-1WVYSwPbM4AHGUrCmWbtuAuuEkoTi_AEI0v-0cBHlYtEZxNwAVc7-EGsNV8-N7bDy59S1M4c4KMAHbg0IJ-qEHlAAD7-90xrsHizHi9zQNqaNo4w49P7UrSiIqJQ3a6YyvvG-HzWU2R7-0XCMKp1dSCkYMITehATLcKeOxscHdTp7fd7wUBg");
            int i = 0;
            for (String namespace : namespaces) {
                final int index = i;
                Future<String> stringFuture = executor.submit(() -> {
                    LOGGER.info(String.format("index is %d, thread is %s", index, Thread.currentThread().threadId()));
                    idSet.add(String.valueOf(Thread.currentThread().threadId()));
                    HttpResponse<String> response = HttpClientUtils.doGet(String.format(urlFormat, namespace), headers, null);
                    if (response.statusCode() == 200) {
                        return response.body();
                    }
                    return null;
                });
                futures.add(stringFuture);
                i++;
            }
            for (Future<String> f : futures) {
                String rsp = f.get();
                if (Objects.nonNull(rsp)) {
                    bodyList.add(rsp);
                }
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "thread break", e);
        } catch (ExecutionException e) {
            LOGGER.log(Level.WARNING, "thread execute error", e);
        }

        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        System.out.println(bodyList.size());
        System.out.println("Threads are " + String.join(",", idSet));
    }

    @Test
    public void LoopRequestK8s() throws NoSuchAlgorithmException, IOException, InterruptedException, KeyManagementException {
        String namespaceStr = "ack-agility-test,acs-harbor,acs-minio,acs-system,caas-system,caas-terminal,cicd,cluster-g,cluster-local,default,default-tenant-ns-1,default-tenant-ns-2,default-tenant-ns-3,emissary-system,kube-node-lease,kube-public,kube-system,monitoring,ns1,ocm-deployer,open-cluster-management,open-cluster-management-addon,open-cluster-management-agent,open-cluster-management-agent-addon,open-cluster-management-cluster-proxy,open-cluster-management-credentials,open-cluster-management-hub,open-cluster-management-managed-serviceaccount,terminal-test,test,test-cluster,vela-system";
        String[] namespaces = namespaceStr.split(",");
        String urlFormat = "https://127.0.0.1:6443/api/v1/namespaces/%s/pods?limit=500";
        final List<String> bodyList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        var headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IlhxZXlRZmsxclZ0SGxuRFR6OE0waEczT1VzeXdWRy1Ed0k4dmhEMlV2MmMifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJkZWZhdWx0LXRva2VuLXo0dGNjIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImRlZmF1bHQiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI5YTNmNjRiNy1kZDkzLTRiNGYtYTY0YS1kYmU2ODUzMzI1MTgiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZS1zeXN0ZW06ZGVmYXVsdCJ9.ZcqY0N5Eqf_-lF8sU7QlWWJgn2G02NSc4PO6MXhxz2TTUtDN2c3UTfReIaWzDhFLoRK6X32ITNMSBB6l_K-ErY8OUfzM2UCnVyfy9q_2KXo_iH4uxiMVfmFYkX2OoTN__g3z-KJsjKeSO4-_x2h1APYYqWlh4qxKf-1WVYSwPbM4AHGUrCmWbtuAuuEkoTi_AEI0v-0cBHlYtEZxNwAVc7-EGsNV8-N7bDy59S1M4c4KMAHbg0IJ-qEHlAAD7-90xrsHizHi9zQNqaNo4w49P7UrSiIqJQ3a6YyvvG-HzWU2R7-0XCMKp1dSCkYMITehATLcKeOxscHdTp7fd7wUBg");
        for (String namespace : namespaces) {
            HttpResponse<String> response = HttpClientUtils.doGet(String.format(urlFormat, namespace), headers, null);
            if (response.statusCode() == 200) {
                bodyList.add(response.body());
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        System.out.println(bodyList.size());
    }

}
