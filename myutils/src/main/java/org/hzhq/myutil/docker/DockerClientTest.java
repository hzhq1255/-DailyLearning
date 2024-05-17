package org.hzhq.myutil.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;

/**
 * @author hzhq
 * @version 1.0
 * @since 2024/1/15 10:52
 */
public class DockerClientTest {

    public static final DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
            .withDockerHost("unix:///Users/hzhq/.orbstack/run/docker.sock")
            .withDockerTlsVerify(false)
//            .withApiVersion("v1.40")
            .withRegistryUsername("admin")
            .withRegistryPassword("Harbor12345")
            .withRegistryUrl("10.10.103.155")
            .build();

    public static void main(String[] args) {
        try (DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(60))
                .build();) {
            DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
//            dockerClient.pingCmd().exec();
//            dockerClient.listImagesCmd().exec().forEach(System.out::println);
//            dockerClient.pullImageCmd("docker.io/openjdk").withTag("21").exec(new ResultCallback.Adapter<PullResponseItem>() {
//
//
//                @Override
//                public void onStart(Closeable closeable) {
//                    System.out.println(new Date());
//                }
//                @Override
//                public void onNext(PullResponseItem object) {
//                    if (object.getStatus() != null){
//                        var progress = object.getProgressDetail();
//                        if (progress == null){
//                            System.out.println(object.getStatus());
//                        } else {
//                            System.out.println(object.getStatus() + ": " + "current=" + progress.getCurrent() + " total=" + progress.getTotal() + " start=" + progress.getStart());
//                        }
//                    }
//                }
//
//            }).awaitCompletion();
////            dockerClient.loadImageCmd(new FileInputStream("/Users/hzhq/Downloads/nginx-latest.tar"));
//            dockerClient.tagImageCmd("docker.io/openjdk:21", "10.10.103.155/library/openjdk", "21").exec();
//            dockerClient.tagImageCmd("", "10.10.103.155/library/nginx", "20230115").exec();
            dockerClient.buildImageCmd().withTarget()
            var push = dockerClient.pushImageCmd("10.10.103.155/library/openjdk:21").exec(new ResultCallback.Adapter<PushResponseItem>() {
                @Override
                public void onStart(Closeable closeable) {
                    System.out.println(new Date());
                    super.onStart(closeable);
                }
                @Override
                public void onNext(PushResponseItem object) {
                    if (object.getStatus() != null){
                        var progress = object.getProgressDetail();
                        if (progress == null){
                            System.out.println(object.getStatus());
                        } else {
                            System.out.println(object.getStatus() + ": " + "current=" + progress.getCurrent() + " total=" + progress.getTotal() + " start=" + progress.getStart());
                        }
                    }
                }
            });
            push.awaitCompletion();

//            dockerClient.pushImageCmd("10.10.103.155/library/nginx:20230115").start().awaitCompletion();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
