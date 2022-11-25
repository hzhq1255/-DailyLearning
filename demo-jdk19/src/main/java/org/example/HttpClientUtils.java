package org.example;

import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author hzhq
 * @since 2022-11-25 10:36:06
 * @version 0.1
 *
 * base jdk 11 httpclient
 * use org.json library
 */
public class HttpClientUtils {

    private static final TrustManager[] trustAllCertificates = new TrustManager[]{ new X509ExtendedTrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {}

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {}
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {}
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }};

    private static final Integer CONNECTED_SECONDS_TIME_OUT = 5;

    private static final HttpClient.Version HTTP_VERSION = HttpClient.Version.HTTP_1_1;

    private static HttpClient sslClient;

    private static HttpClient httpClient;

    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String CONTENT_TYEP_FORM_DATA = "application/x-www-form-urlencoded";


    static {
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification","true");//取消主机名验证
        SSLParameters sslParams = new SSLParameters();
        sslParams.setEndpointIdentificationAlgorithm("");
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            sc.init(null, trustAllCertificates, new SecureRandom());
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        sslClient =  HttpClient.newBuilder()
                .version(HTTP_VERSION)
                .sslContext(sc)
                .sslParameters(sslParams)
                .connectTimeout(Duration.ofSeconds(CONNECTED_SECONDS_TIME_OUT))
                .build();
        httpClient =  HttpClient.newBuilder()
                .version(HTTP_VERSION)
                .connectTimeout(Duration.ofSeconds(CONNECTED_SECONDS_TIME_OUT))
                .build();
    }


    private static String bodyToJSONString(Object body){
        // change use json binary
        return JSONObject.valueToString(body);
        // return String.format("%s", o);
    }

    private static String getParamsAsString(Map<String, String> params){
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry: params.entrySet()){
            if (stringBuilder.length() > 0){
                stringBuilder.append("&");
            }
            stringBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return stringBuilder.toString();
    }

    private static String[] getHttpHeadersAsStringArray(Map<String, String> headers){
        Map<String,String> newHeaders =  Optional.ofNullable(headers).orElse(new HashMap<>());
        newHeaders.putIfAbsent("Content-Type", CONTENT_TYEP_FORM_DATA);
        return  newHeaders.entrySet()
                .stream()
                .map(e-> String.format("%s=%s", e.getKey(),e.getValue()))
                .toArray(String[]::new);
    }

    private static String getRequestBodyAsString(Map<String, String> headers, Map<String, String> params, Object body){
        String contentType = Optional.ofNullable(headers).orElse(new HashMap<String, String>())
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e->e.getKey().toLowerCase(), Map.Entry::getValue))
                .getOrDefault("content-type", CONTENT_TYEP_FORM_DATA);
        return switch (contentType) {
            case CONTENT_TYPE_JSON -> bodyToJSONString(body);
            case CONTENT_TYEP_FORM_DATA, default -> getParamsAsString(params);
        };
    }

    private static HttpClient getClient(String url){
        return Optional.ofNullable(url)
                .orElse("")
                .startsWith("https") ?
                sslClient : httpClient;
    }





    public static <T> HttpResponse<T> get(String url, Map<String, String> headers,Map<String, String> query, Map<String, String> params, String body, Class<T> clazz) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(getHttpHeadersAsStringArray(headers))
                .POST(HttpRequest.BodyPublishers.ofString(getRequestBodyAsString(headers, params, body)))
                .build();
        return getClient(url).send(httpRequest, );
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", "abc");
        map.put("password", "Ab12356");
        map.put("address", new HashMap<String, Object>(){{
            put("city", "hangzhou");
            put("country","CN" );
        }});
        String json = JSONObject.valueToString(map);
        System.out.println(json);

    }

}
