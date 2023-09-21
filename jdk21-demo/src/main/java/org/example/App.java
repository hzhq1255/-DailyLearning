package org.example;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;

/**
 * @author hzhq
 * @version 1.0
 * @since 2023/9/21 上午12:22
 */
public class App {
    public static void main(String[] args) throws Exception {
        String a = """
        select from 
        """;
        String s = STR. "aaa = \{ a }" ;
        System.out.println(s);
        Thread vt = Thread.startVirtualThread(() -> {
            try {
                HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("https://www.baidu.com"))
                        .GET().build();
                HttpClient httpClient = HttpClient.newBuilder().sslContext(SSLContext.getDefault()).build();
                String body = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
                System.out.println(body);
            } catch (InterruptedException e){
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        while (true){
            if (!vt.isAlive()) {
                break;
            }
        }
    }
}
