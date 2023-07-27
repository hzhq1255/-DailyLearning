package org.hzhq.myutil.utils.helm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hzhq.myutil.exception.HelmException;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-08 11:43 base on helm cli v3.10.1 version
 * <p>
 * GlobalFlags: --burst-limit int client-side default throttling limit (default 100) --debug enable verbose
 * output -h, --help help for helm --kube-apiserver string the address and the port for the Kubernetes API server
 * --kube-as-group stringArray group to impersonate for the operation, this flag can be repeated to specify
 * multiple groups. --kube-as-user string username to impersonate for the operation --kube-ca-file string the
 * certificate authority file for the Kubernetes API server connection --kube-context string name of the
 * kubeconfig context to use --kube-insecure-skip-tls-verify if true, the Kubernetes API server's certificate
 * will not be checked for validity. This will make your HTTPS connections insecure --kube-tls-server-name string
 * server name to use for Kubernetes API server certificate validation. If it is not provided, the hostname used
 * to contact the server is used --kube-token string bearer token used for authentication --kubeconfig string
 * path to the kubeconfig file -n, --namespace string namespace scope for this request --registry-config string
 * path to the registry config file (default "$HOME/Library/Preferences/helm/registry/config.json")
 * --repository-cache string path to the file containing cached repository indexes (default
 * "$HOME/Library/Caches/helm/repository") --repository-config string path to the file containing repository
 * names and URLs (default "$HOME/Library/Preferences/helm/repositories.yaml")
 */
public class RootCmd {
    private static final String HELM_COMMAND = "helm";
    protected static final String CMD_SPLIT = " ";

    protected static final Logger LOGGER = Logger.getLogger("helm root cmd");

    private static final long DEFAULT_TIMEOUT_MILL_SECONDS = 30000;

    private static final String HELM_CMD_TIMEOUT_ENV = "HELM_CMD_TIMEOUT";

    private long getDefaultTimeoutMillSeconds() {
        String timeout = System.getenv(HELM_CMD_TIMEOUT_ENV);
        try {
            if (timeout != null && !timeout.isEmpty()) {
                return Long.parseLong(timeout);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.FINE, "timeout format failed error : {0}", e);
        }
        return DEFAULT_TIMEOUT_MILL_SECONDS;
    }

    protected List<String> cmds = new ArrayList<>();

    protected List<String> args = new ArrayList<>();

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

    public RootCmd() {
        this.cmds.add(HELM_COMMAND);
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
        this.buildArgs();
        List<String> cmds = new ArrayList<>(this.cmds);
        cmds.addAll(this.args);
        return String.join(CMD_SPLIT, cmds);
    }

    /**
     * Improve the filterArgs function, if you want to avoid an injection attack.
     *
     * @return filterArgs;
     */
    private List<String> filterCmds(List<String> cmds, List<String> args) {
        List<String> newCmds = new ArrayList<>(cmds);
        List<String> filterArgs = args.stream().map(String::trim).collect(Collectors.toList());
        newCmds.addAll(filterArgs);
        return newCmds;

    }

    private static class ProcessWorkerThread extends Thread {

        private final Process process;
        private volatile int exitCode = -100;
        private volatile String output = "";
        private volatile boolean completed = false;

        public ProcessWorkerThread(Process process) {
            this.process = process;
        }

        public int getExitCode() {
            return exitCode;
        }

        public String getOutput() {
            return output;
        }

        public boolean isCompleted() {
            return completed;
        }

        private String readStdout(InputStream inputStream) throws IOException {
            StringBuilder builder = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
            }
            return builder.toString();
        }

        @Override
        public void run() {
            try {
                String outputMessage = readStdout(process.getInputStream());
                String errorMessage = readStdout(process.getErrorStream());
                if (errorMessage.length() > 0) {
                    outputMessage = errorMessage + "\n" + outputMessage;
                }
                output = outputMessage;
                exitCode = process.waitFor();
                completed = true;
            } catch (InterruptedException | IOException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    protected String execCmd(List<String> cmds, List<String> args, long timeoutMillSeconds) {
        List<String> filterCmds = filterCmds(cmds, args);
        LOGGER.log(Level.INFO, "start exec helm command {}", String.join(",", filterCmds));

        ProcessBuilder builder = new ProcessBuilder(filterCmds);
        ProcessWorkerThread processWorkerThread = null;
        try {
            // 获取命令输出
            Process process = builder.start();
            processWorkerThread = new ProcessWorkerThread(process);
            processWorkerThread.start();
            // default timeout 30 seconds
            processWorkerThread.join(timeoutMillSeconds);
            if (processWorkerThread.isCompleted()) {
                if (processWorkerThread.getExitCode() != 0) {
                    throw new HelmException(
                            "exitCode: " + processWorkerThread.getExitCode() + "errorMsg: " + processWorkerThread.getOutput());
                }
                return processWorkerThread.getOutput();
            } else {
                process.destroy();
                processWorkerThread.interrupt();
                throw new HelmException("exec cmd timeout: " + String.join(" ", filterCmds));
            }

        } catch (InterruptedException e) {
            processWorkerThread.interrupt();
            throw new HelmException(e.toString());
        } catch (IOException e) {
            throw new HelmException(e.toString());
        }
    }

    protected String exec() {
        this.buildArgs();
        return execCmd(this.cmds, this.args, getDefaultTimeoutMillSeconds());
    }

    protected String exec(long timeoutMillSeconds) {
        this.buildArgs();
        return execCmd(this.cmds, this.args, timeoutMillSeconds);
    }

    private List<String> addOutputJsonArg() {
        this.buildArgs();
        List<String> args = new ArrayList<>(this.args);
        if (!args.contains("--output") && !args.contains("-o")) {
            args.add("-o");
            args.add("json");
        }
        return args;
    }

    protected <T> T jsonToPojo(String json, Class<T> clazz){
        ObjectMapper objectMapper= new ObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e ){
            LOGGER.log(Level.FINE, "json to obj ");
        }
        return null;
    }

    protected <T> List<T> jsonToList(String json, Class<T> clazz){
        ObjectMapper objectMapper= new ObjectMapper();
        try {

            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class,clazz));
        } catch (JsonProcessingException e ){
            LOGGER.log(Level.FINE, "json to obj ");
        }
        return null;
    }


    /**
     * @param clazz class
     * @return T
     */
    protected <T> T execToObj(Class<T> clazz) {
        List<String> args = addOutputJsonArg();
        String result = this.execCmd(this.cmds, args, getDefaultTimeoutMillSeconds());
        return jsonToPojo(result, clazz);
    }

    /**
     * @param clazz class
     * @return T
     */
    protected <T> List<T> execToObjs(Class<T> clazz) {
        List<String> args = addOutputJsonArg();
        String result = this.execCmd(this.cmds, args, getDefaultTimeoutMillSeconds());
        return jsonToList(result, clazz);
    }

    protected List<String> createTempFiles(String... values) {
        List<String> valueFiles = new ArrayList<>();
        for (String value : values) {
            String path = createTempFile(value);
            if (path != null) {
                valueFiles.add(path);
            }
        }
        return valueFiles;
    }


    public static String createTempFile(String value) {
        File valueFile;
        File tempFile;
        try {
            // 判断传入的值是否已经是文件
            String path;
            valueFile = new File(value);
            if (valueFile.exists() && valueFile.isFile()) {
                path = valueFile.getAbsolutePath();
            } else {
                // 创建临时文件
                tempFile = File.createTempFile("chart-values-", ".yaml", new File("/tmp"));
                tempFile.deleteOnExit();
                FileWriter writer = new FileWriter(tempFile);
                writer.write(value);
                writer.close();
                path = tempFile.getAbsolutePath();
            }
            return path;
        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING, "create helm temp value file failed, this value is {0} error {1}", new Object[]{value, ioe});
        }
        return null;
    }


    protected void setSourceYamlContent(String text, Map<String, String> ctxMap) {
        if (text == null || ctxMap == null) {
            return;
        }
        String prefix = "# Source: ";
        for (String content : text.split("---")) {
            for (String line : content.split("\n")) {
                if (line.startsWith(prefix)) {
                    int index = line.indexOf("/");
                    if (index != -1) {
                        String key = line.substring(index + 1);
                        String value = content.replaceAll("#.*", "").strip();
                        ctxMap.compute(key, (k, v) -> (v == null ? "" : v + "\n---\n") + value);
                    }
                    break;
                }
            }
        }
    }

}
