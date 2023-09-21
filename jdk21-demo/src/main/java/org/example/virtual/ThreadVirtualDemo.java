package org.example.virtual;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Time;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


/**
 * @author hzhq
 * @version 1.0
 * @since 2023/9/21 上午12:58
 */
public class ThreadVirtualDemo {


    private static User parseJson(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, User.class);
    }

    public static void main(String[] args) {
        String url = "http://localhost:8000/user.json";
        String path = ThreadVirtualDemo.class.getClassLoader().getResource("").getPath();
        System.out.println(path);
        Thread pt = Thread.startVirtualThread(() -> {
            Process jwebserver = null;
            try {
                System.out.println("Start JWEBServer ....");
                jwebserver = Runtime.getRuntime().exec(new String[]{"jwebserver", "-d", path, "-b", "::"});
                TimeUnit.SECONDS.sleep(5);
                jwebserver.destroy();
                System.out.println("Close JWEBServer ....");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (jwebserver != null) {
                    jwebserver.destroyForcibly();
                }
            }
        });
        Thread vt = Thread.startVirtualThread(() -> {
            try (HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build()) {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Start http virtual thread...");
                HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url))
                        .GET().build();
                String body = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
                System.out.println(body);
                User user = parseJson(body);
                System.out.println("End http virtual thread...");
                System.out.println("username=" + user.username() + ", password=" + user.password());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        while (true) {
            if (!vt.isAlive() && !pt.isAlive()) {
                break;
            }
        }
    }

}
