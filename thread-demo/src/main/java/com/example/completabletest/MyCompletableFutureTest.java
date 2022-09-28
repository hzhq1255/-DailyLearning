package com.example.completabletest;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaim;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2022-09-13 12:28 AM
 */
public class MyCompletableFutureTest {

    public static void test1() throws Exception{
        ApiClient client = Config.fromConfig("/home/hzhq/.kube/119.23.212.83-test2");
        Configuration.setDefaultApiClient(client);
        CoreV1Api coreV1Api = new CoreV1Api(client);
        V1NamespaceList v1NamespaceList = coreV1Api.listNamespace(null, null, null, null, null, null, null, null, null, null);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10,
                20,
                0,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        List<V1PersistentVolumeClaim> v1PersistentVolumeClaims = new ArrayList<>();
        List<CompletableFuture<List<V1PersistentVolumeClaim>>> cfs = v1NamespaceList.getItems().parallelStream().map((v1Namespace) -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return coreV1Api.listNamespacedPersistentVolumeClaim(Objects.requireNonNull(v1Namespace.getMetadata()).getName(), null, null, null, null, null, null, null, null, null, null).getItems();
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                }, executor)
//                .exceptionally(ex -> new ArrayList<>())
//                .thenAccept(v1PersistentVolumeClaims::addAll)
        ).toList();

        List<V1PersistentVolumeClaim> cfsAll = CompletableFuture.allOf(cfs.toArray(new CompletableFuture[0]))
                .thenApply(v -> cfs.stream().map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList())).join().stream().flatMap(List::stream).toList();

        System.out.println(v1PersistentVolumeClaims);
        System.out.println(cfsAll);
    }





    public static void main(String[] args) throws Exception {


    }
}
