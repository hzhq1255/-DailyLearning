package org.hzhq.myutil.utils.helm.helm;

import com.skyview.caas.common.exception.HelmException;
import com.skyview.caas.common.util.JsonUtil;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-08 11:43
 * base on helm cli v3.10.1 version
 * <p>
 * GlobalFlags:
 * --burst-limit int                 client-side default throttling limit (default 100)
 * --debug                           enable verbose output
 * -h, --help                            help for helm
 * --kube-apiserver string           the address and the port for the Kubernetes API server
 * --kube-as-group stringArray       group to impersonate for the operation, this flag can be repeated to specify multiple groups.
 * --kube-as-user string             username to impersonate for the operation
 * --kube-ca-file string             the certificate authority file for the Kubernetes API server connection
 * --kube-context string             name of the kubeconfig context to use
 * --kube-insecure-skip-tls-verify   if true, the Kubernetes API server's certificate will not be checked for validity. This will make your HTTPS connections insecure
 * --kube-tls-server-name string     server name to use for Kubernetes API server certificate validation. If it is not provided, the hostname used to contact the server is used
 * --kube-token string               bearer token used for authentication
 * --kubeconfig string               path to the kubeconfig file
 * -n, --namespace string                namespace scope for this request
 * --registry-config string          path to the registry config file (default "$HOME/Library/Preferences/helm/registry/config.json")
 * --repository-cache string         path to the file containing cached repository indexes (default "$HOME/Library/Caches/helm/repository")
 * --repository-config string        path to the file containing repository names and URLs (default "$HOME/Library/Preferences/helm/repositories.yaml")
 */
public class RootCmd {
    private static final String HELM_COMMAND = "helm";

    protected static final String CMD_SPLIT = " ";

    protected static final Logger LOGGER = Logger.getLogger(RootCmd.class.getName());

    protected List<String> args = new ArrayList<>(Collections.singleton(HELM_COMMAND));

    protected Integer burstLimit;
    protected boolean debug;
    protected boolean help;
    protected String kubeAPIServer;
    protected List<String> kubeAsGroup = new ArrayList<>();
    protected String kubeAsUser;
    protected String kubeContext;
    protected String kubeCAFile;
    protected String namespace;
    protected String kubeconfig;
    protected String kubeToken;
    protected String kubeTLSServerName;
    protected boolean kubeInsecureSkipTLSVerify;
    protected String registryConfig;
    protected String repositoryCache;
    protected String repositoryConfig;


    public RootCmd namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public RootCmd kubeToken(String kubeToken) {
        this.kubeToken = kubeToken;
        return this;
    }

    public RootCmd kubeconfig(String kubeconfig) {
        this.kubeconfig = kubeconfig;
        return this;
    }


    public RootCmd burstLimit(Integer burstLimit) {
        this.burstLimit = burstLimit;
        return this;
    }


    public RootCmd debug() {
        this.debug = true;
        return this;
    }


    public RootCmd help() {
        this.help = true;
        return this;
    }




    public RootCmd kubeAPIServer(String kubeAPIServer) {
        this.kubeAPIServer = kubeAPIServer;
        return this;
    }


    public RootCmd kubeAsGroup(String... kubeAsGroup) {
        this.kubeAsGroup.addAll(Arrays.asList(kubeAsGroup));
        return this;
    }


    public RootCmd kubeCAFile(String kubeCAFile) {
        this.kubeCAFile = kubeCAFile;
        return this;
    }

    public RootCmd kubeAsUser(String kubeAsUser) {
        this.kubeAsUser = kubeAsUser;
        return this;
    }

    public RootCmd kubeContext(String kubeContext) {
        this.kubeContext = kubeContext;
        return this;
    }


    public RootCmd kubeInsecureSkipTLSVerify() {
        this.kubeInsecureSkipTLSVerify = true;
        return this;
    }


    public RootCmd kubeTLSServerName(String kubeTLSServerName) {
        this.kubeTLSServerName = kubeTLSServerName;
        return this;
    }


    public RootCmd registryConfig(String registryConfig) {
        this.registryConfig = registryConfig;
        return this;
    }

    public RootCmd repositoryConfig(String repositoryConfig) {
        this.repositoryConfig = repositoryConfig;
        return this;
    }


    public RootCmd repositoryCache(String repositoryCache) {
        this.repositoryCache = repositoryCache;
        return this;
    }

    protected RootCmd buildArgs() {
        if (burstLimit != null) {
            this.args.add("--burst-limit");
            this.args.add(Integer.toString(burstLimit));
        }
        if (debug) {
            this.args.add("--debug");
        }
        if (help) {
            this.args.add("--help");
        }
        if (kubeAPIServer != null) {
            this.args.add("--kube-apiserver");
            this.args.add(kubeAPIServer);
        }
        if (!kubeAsGroup.isEmpty()) {
            for (String s : kubeAsGroup) {
                this.args.add("--kube-as-group");
                this.args.add(s);
            }
        }
        if (kubeCAFile != null) {
            this.args.add("--kube-ca-file");
            this.args.add(kubeCAFile);
        }
        if (kubeAsUser != null) {
            this.args.add("--kube-as-user");
            this.args.add(kubeAsUser);
        }
        if (kubeContext != null) {
            this.args.add("--kube-context");
            this.args.add(kubeContext);
        }
        if (kubeInsecureSkipTLSVerify) {
            this.args.add("--kube-insecure-skip-tls-verify");
        }
        if (kubeTLSServerName != null) {
            this.args.add("--kube-tls-server-name");
            this.args.add(kubeTLSServerName);
        }
        if (kubeToken != null) {
            this.args.add("--kube-token");
            this.args.add(kubeToken);
        }
        if (kubeconfig != null) {
            this.args.add("--kubeconfig");
            this.args.add(kubeconfig);
        }
        if (namespace != null) {
            this.args.add("--namespace");
            this.args.add(namespace);
        }
        if (registryConfig != null) {
            this.args.add("--registry-config");
            this.args.add(registryConfig);
        }
        if (repositoryCache != null) {
            this.args.add("--repository-cache");
            this.args.add(repositoryCache);
        }
        if (repositoryConfig != null) {
            this.args.add("--repository-config");
            this.args.add(repositoryConfig);
        }
        return this;
    }

    protected String buildCmd() {
        return String.join(CMD_SPLIT, this.args);
    }


    /**
     * XXX: Improve the filterArgs function, if want to avoid an injection attack.
     *
     * @return filterArgs;
     */
    private List<String> filterArgs() {
        return this.args.stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }


    protected String exec() {
        try {
            LOGGER.log(Level.INFO,"start exec helm command {0}", this.args);
            // 获取命令输出
            List<String> filterArgs = filterArgs();
            ProcessBuilder builder = new ProcessBuilder(filterArgs);
            Process process = builder.start();
            int exitCode = process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // 获取命令输出
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
            if (exitCode != 0) {
                BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder err = new StringBuilder();
                while ((line = errReader.readLine()) != null) {
                    err.append(line).append("\n");
                }
                errReader.close();
                throw new HelmException(err.toString());
            }
            return output.toString();

        } catch (IOException | InterruptedException e) {
            throw new HelmException(e.toString());
        }
    }

    /**
     * @param clazz
     * @return
     */
    protected <T> T execToObj(Class<T> clazz) {
        this.args.add("-o");
        this.args.add("json");
        String result = this.exec();
        return JsonUtil.jsonToPojo(result, clazz);
    }

    /**
     * @param clazz
     * @return
     */
    protected <T> List<T> execToObjs(Class<T> clazz) {
        this.args.add("-o");
        this.args.add("json");
        String result = this.exec();
        return JsonUtil.jsonToList(result, clazz);
    }


    protected List<String> createTempFiles(String... values) {

        List<String> valueFiles = new ArrayList<>();
        File tempFile;
        File valueFile;
        for (String value : values) {
            try {
                // 判断传入的值是否已经是文件
                valueFile = new File(value);
                if (valueFile.exists()) {
                    valueFiles.add(value);
                } else {
                    // 创建临时文件
                    String randomStr = RandomStringUtils.random(6, true, true);
                    tempFile = File.createTempFile("value", randomStr);
                    FileWriter writer = new FileWriter(tempFile);
                    writer.write(value);
                    writer.close();
                    valueFiles.add(tempFile.getAbsolutePath());
                }
            } catch (IOException ioe) {
                LOGGER.log(Level.WARNING, "create helm temp value file failed, this value is {0}", value);
            }
        }
        return valueFiles;
    }

    protected void cleanTempFiles(String... files) {
        for (String file : files) {
            File valueFile = new File(file);
            if (valueFile.exists()) {
                boolean success = valueFile.delete();
                if (!success) {
                    LOGGER.log(Level.WARNING, "delete helm temp value file failed, the value file is {0}", file);
                }
            }
        }

    }


}
