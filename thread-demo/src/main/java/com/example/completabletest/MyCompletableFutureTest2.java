package com.example.completabletest;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2022-09-13 11:24 AM
 */
public class MyCompletableFutureTest2 {
   public static final Config config =  new ConfigBuilder()
            .withMasterUrl("https://172.16.161.72:6443")
            .withTrustCerts(true)
            .withOauthToken("eyJhbGciOiJSUzI1NiIsImtpZCI6IkVoUnRhUEhkVWFVTTk2S0szd1hwRTBLVjlnUzFqMWwtQnJuRG5xWHZDVTgifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJjYWFzLWFkbWluLXRva2VuLW1uZDY0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImNhYXMtYWRtaW4iLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJiOThkNmY5Ni1iODljLTRlYmMtOTkzYS03Mzc4MDY0ODU2MjAiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZS1zeXN0ZW06Y2Fhcy1hZG1pbiJ9.UAGsoOUQMoZCthq_v956G8RpjJCdk1rVWVdbNHiJGi-JM7nAuqgziGJfR9jFDPP24TeNWYohayfYiSmjDvPyY9jh8z4-AE78ICmCEWNIjUN_csaxa6_aKpxjZvccwGjfr4DC_INMuqMcn2RPSI_T4z7iWZRnTPQhmFhcFHsvZqdUuIgbTfO534_4qdJvPzSrAi41MpyRuingT2gRrLrCX_HgfNMRMD0lJlsafa8wVzavIS6JxMKvV2LfB18xRQiGMEqqq6RaWygCEapxKVeV8bR-PPMc6RGeuBWp5ex2dj1hx316U5ztD0xi8jrLvN1Dtj525GUSQCJQv1h85tyxww")
            .build();
    public static final KubernetesClient kubernetesClient = new DefaultKubernetesClient(config);


    public void test() {

        List<String> namesapces = Arrays.asList("caas-system", "kube-system", "acs-system");
        List<Object> res = new ArrayList<>();
        var deployF = CompletableFuture.supplyAsync(() ->
                kubernetesClient.apps().deployments().list().getItems().stream()
                        .filter(item -> namesapces.stream().anyMatch(item.getMetadata().getNamespace()::equalsIgnoreCase))
                        .collect(Collectors.toList())
        );
        var fsF = CompletableFuture.supplyAsync(() ->
                kubernetesClient.apps().daemonSets().list().getItems().stream()
                        .filter(item -> namesapces.stream().anyMatch(item.getMetadata().getNamespace()::equalsIgnoreCase))
                        .collect(Collectors.toList())
        );
        var stsF = CompletableFuture.supplyAsync(() ->
                kubernetesClient.apps().statefulSets().list().getItems().stream()
                        .filter(item -> namesapces.stream().anyMatch(item.getMetadata().getNamespace()::equalsIgnoreCase))
                        .collect(Collectors.toList())
        );

        var deploys = new ArrayList<Deployment>();
        var dses = new ArrayList<DaemonSet>();
        var stses = new ArrayList<StatefulSet>();

        var resources = CompletableFuture.allOf(deployF, fsF, stsF).thenApplyAsync(v -> {
            List<Object> kubernetesResources = new ArrayList<>();
            kubernetesResources.addAll(deployF.join());
            kubernetesResources.addAll(fsF.join());
            kubernetesResources.addAll(stsF.join());
            return kubernetesResources;
        }).join();
        resources.forEach(e -> {
            if (e instanceof Deployment) {
                deploys.add((Deployment) e);
            } else if (e instanceof DaemonSet){
                dses.add((DaemonSet) e);
            } else if (e instanceof StatefulSet){
                stses.add((StatefulSet) e);
            }
        });
        System.out.println(deploys);
        System.out.println(dses);
        System.out.println(stses);
    }

    @SafeVarargs
    private static  <T extends HasMetadata & Namespaced>  List<? extends T> listResourcesByNamespaces(List<String> namespaces, Function<Void, List< ? extends T >>... functions){
        final List<T> resources = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        CompletableFuture.allOf(Arrays.stream(functions)
                .map(f -> CompletableFuture.supplyAsync(() -> f.apply(null), threadPool)
                        .thenAcceptAsync(resources::addAll)).toArray(CompletableFuture[]::new))
                .join();
        threadPool.shutdown();
        return resources;
    }


    public static void main(String[] args) throws Exception {

        List<String> namesapces = Arrays.asList("caas-system", "kube-system", "acs-system");
        List<Deployment> deployments = new ArrayList<>();
        List<DaemonSet> daemonSets = new ArrayList<>();
        List<StatefulSet> statefulSets = new ArrayList<>();
        List<PersistentVolumeClaim> persistentVolumeClaims = new ArrayList<>();


        listResourcesByNamespaces(namesapces, (v)->kubernetesClient.apps().deployments().list().getItems(),
                (v)-> kubernetesClient.apps().daemonSets().list().getItems(),
                (v)-> kubernetesClient.apps().statefulSets().list().getItems(),
                (v)-> kubernetesClient.persistentVolumeClaims().list().getItems())
                .forEach(o-> {
                    if (o instanceof Deployment){
                        deployments.add((Deployment) o);
                    } else if (o instanceof DaemonSet) {
                        daemonSets.add((DaemonSet) o);
                    } else if (o instanceof StatefulSet) {
                        statefulSets.add((StatefulSet) o);
                    } else if (o instanceof PersistentVolumeClaim) {
                        persistentVolumeClaims.add((PersistentVolumeClaim) o);
                    }
                });

        System.out.println(deployments);
        System.out.println(daemonSets);
        System.out.println(statefulSets);
        System.out.println(persistentVolumeClaims);


    }
}
