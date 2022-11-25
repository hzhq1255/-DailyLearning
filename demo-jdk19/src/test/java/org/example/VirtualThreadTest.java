package org.example;


import netscape.javascript.JSObject;
import org.junit.jupiter.api.Test;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class VirtualThreadTest {


    @Test
    public void VirtualThreadCreateTest1(){

        var platformThread = Thread.ofPlatform()
                .unstarted(()-> System.out.printf( "platform thread id = %s\n",Thread.currentThread()));

        var virtualThread = Thread.ofVirtual()
                .unstarted(()-> System.out.printf("virtual thread id = %s \n",Thread.currentThread()));

        platformThread.start();
        virtualThread.start();
    }


    @Test
    public void VirtualRequestK8s() throws NoSuchAlgorithmException, IOException, InterruptedException, KeyManagementException {
        TrustManager[] trustAllCertificates = new TrustManager[]{ new X509ExtendedTrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
                // todo
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
                // todo
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
                // todo
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
                // todo
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // todo

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // todo
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};
        SSLParameters sslParams = new SSLParameters();
        sslParams.setEndpointIdentificationAlgorithm("");
        SSLContext sc = SSLContext.getInstance("SSL");
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification","true");//取消主机名验证
        sc.init(null, trustAllCertificates, new SecureRandom());
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://www.baidu.com"))
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString("");
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .sslContext(sc)
                .sslParameters(sslParams)
                .connectTimeout(Duration.ofSeconds(5)).build();
        HttpResponse<App> response = httpClient.send(httpRequest, new HttpResponse.BodyHandler<App>() {
            @Override
            public HttpResponse.BodySubscriber<App> apply(HttpResponse.ResponseInfo responseInfo) {
                return null;
            }
        });
        System.out.println(response.body());

    }

}
