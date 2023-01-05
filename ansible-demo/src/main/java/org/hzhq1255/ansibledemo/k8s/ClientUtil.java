package org.hzhq1255.ansibledemo.k8s;

import io.kubernetes.client.Exec;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-01-04 11:08
 */
public class ClientUtil {

    private static ApiClient apiClient;

    private static final Logger LOGGER = Logger.getLogger("clientUtil");

    static {
        String kubeHost = System.getenv("KUBERNETES_SERVICE_HOST");
        try {
            if (Objects.isNull(kubeHost) || "".equals(kubeHost)) {
                String kubeconfigPath = System.getenv("HOME") + "/.kube/config";
                apiClient = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeconfigPath))).build();
            } else {
                apiClient = ClientBuilder.cluster().build();
            }
        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING, "build kubernetes client error");
            ioe.printStackTrace();
        }
    }

    public static ApiClient getApiClient() {
        if (apiClient == null) {
            throw new RuntimeException("get kube api client failed");
        }
        Configuration.setDefaultApiClient(apiClient);
        return apiClient;
    }


}
