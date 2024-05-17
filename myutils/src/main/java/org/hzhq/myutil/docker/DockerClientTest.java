package org.hzhq.myutil.docker;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.okhttp.OkDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

import java.io.IOException;
import java.time.Duration;

/**
 * @author hzhq
 * @version 1.0
 * @since 2024/1/15 10:52
 */
public class DockerClient {

  public static final DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost("tcp://10.10.103.155:12375")
        .withDockerTlsVerify(true)
        .withDockerCertPath("/home/user/.docker")
        .withRegistryUsername("admin")
        .withRegistryPassword("Harbor12345")
        .withRegistryUrl("10.10.103.155")
        .build();
    public static void main(String[] args) {
        try (DockerHttpClient httpClient = new OkDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .connectTimeout(30)
                .readTimeout(45)
                .build();){
            httpClient.execute()

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
