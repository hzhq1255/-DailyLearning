package org.hzhq.myutil.utils.helm;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-09 上午9:20 <br/>
 * Usage: helm upgrade --help
 */
public class UpgradeCmd extends RootCmd implements GlobalFlags<UpgradeCmd> {
    private static final String COMMAND_NAME = "upgrade";
    private String releaseName;
    private String chart;
    private boolean atomic;
    private String caFile;
    private String certFile;
    private boolean cleanupOnFail;
    private boolean createNamespace;
    private boolean dependencyUpdate;
    private String description;
    private boolean devel;
    private boolean disableOpenAPIValidation;
    private boolean dryRun;
    private boolean enableDNS;
    private boolean force;
    private int historyMax;
    private boolean insecureSkipTLSVerify;
    private boolean install;
    private String keyFile;
    private String keyring;
    private boolean noHooks;
    private String output;
    private boolean passCredentials;
    private String password;
    private String postRenderer;
    private final List<String> postRendererArgs = new ArrayList<>();
    private boolean renderSubchartNotes;
    private String repo;
    private boolean resetValues;
    private final List<String> values = new ArrayList<>();
    private final List<String> set = new ArrayList<>();
    private final List<String> setFile = new ArrayList<>();
    private final List<String> setJSON = new ArrayList<>();
    private final List<String> setString = new ArrayList<>();
    private boolean skipCrds;
    private Duration timeout;
    private String username;
    private boolean verify;
    private String version;
    private boolean wait;
    private boolean waitForJobs;

    public UpgradeCmd releaseName(String releaseName) {
        this.releaseName = releaseName;
        return this;
    }

    public UpgradeCmd chart(String chart) {
        this.chart = chart;
        return this;
    }

    public UpgradeCmd atomic() {
        this.atomic = true;
        return this;
    }

    public UpgradeCmd caFile(String caFile) {
        this.caFile = caFile;
        return this;
    }

    public UpgradeCmd certFile(String certFile) {
        this.certFile = certFile;
        return this;
    }

    public UpgradeCmd cleanupOnFail() {
        this.cleanupOnFail = true;
        return this;
    }

    public UpgradeCmd createNamespace() {
        this.createNamespace = true;
        return this;
    }

    public UpgradeCmd dependencyUpdate() {
        this.dependencyUpdate = true;
        return this;
    }

    public UpgradeCmd description(String description) {
        this.description = description;
        return this;
    }

    public UpgradeCmd devel() {
        this.devel = true;
        return this;
    }

    public UpgradeCmd disableOpenAPIValidation() {
        this.disableOpenAPIValidation = true;
        return this;
    }

    public UpgradeCmd dryRun() {
        this.dryRun = true;
        return this;
    }

    public UpgradeCmd enableDNS() {
        this.enableDNS = true;
        return this;
    }

    public UpgradeCmd force() {
        this.force = true;
        return this;
    }

    public UpgradeCmd historyMax(int historyMax) {
        this.historyMax = historyMax;
        return this;
    }

    public UpgradeCmd insecureSkipTLSVerify() {
        this.insecureSkipTLSVerify = true;
        return this;
    }

    public UpgradeCmd install() {
        this.install = true;
        return this;
    }

    public UpgradeCmd keyFile(String keyFile) {
        this.keyFile = keyFile;
        return this;
    }

    public UpgradeCmd keyring(String keyring) {
        this.keyring = keyring;
        return this;
    }

    public UpgradeCmd noHooks() {
        this.noHooks = true;
        return this;
    }

    public UpgradeCmd output(String output) {
        this.output = output;
        return this;
    }

    public UpgradeCmd output(OutputType type) {
        this.output = type.name();
        return this;
    }

    public UpgradeCmd passCredentials() {
        this.passCredentials = true;
        return this;
    }

    public UpgradeCmd password(String password) {
        this.password = password;
        return this;
    }

    public UpgradeCmd postRenderer(String postRenderer) {
        this.postRenderer = postRenderer;
        return this;
    }

    public UpgradeCmd postRendererArgs(String... postRendererArgs) {
        this.postRendererArgs.addAll(Arrays.asList(postRendererArgs));
        return this;
    }

    public UpgradeCmd renderSubchartNotes() {
        this.renderSubchartNotes = true;
        return this;
    }

    public UpgradeCmd repo(String repo) {
        this.repo = repo;
        return this;
    }

    public UpgradeCmd resetValues() {
        this.resetValues = true;
        return this;
    }

    public UpgradeCmd values(String... values) {
        this.values.addAll(Arrays.asList(values));
        return this;
    }

    public UpgradeCmd set(String... set) {
        this.set.addAll(Arrays.asList(set));
        return this;
    }

    public UpgradeCmd setFile(String... setFile) {
        this.setFile.addAll(Arrays.asList(setFile));
        return this;
    }

    public UpgradeCmd setJSON(String... setJSON) {
        this.setJSON.addAll(Arrays.asList(setJSON));
        return this;
    }

    public UpgradeCmd setString(String... setString) {
        this.setString.addAll(Arrays.asList(setString));
        return this;
    }

    public UpgradeCmd skipCrds() {
        this.skipCrds = true;
        return this;
    }

    public UpgradeCmd timeout(Duration timeout) {
        this.timeout = timeout;
        return this;
    }

    public UpgradeCmd username(String username) {
        this.username = username;
        return this;
    }

    public UpgradeCmd verify() {
        this.verify = true;
        return this;
    }

    public UpgradeCmd version(String version) {
        this.version = version;
        return this;
    }

    public UpgradeCmd waitArg() {
        this.wait = true;
        return this;
    }

    public UpgradeCmd waitForJobs() {
        this.waitForJobs = true;
        return this;
    }

    @Override
    public UpgradeCmd burstLimit(Integer burstLimit) {
        return ((UpgradeCmd) super.burstLimit(burstLimit));
    }

    @Override
    public UpgradeCmd debug() {
        return ((UpgradeCmd) super.debug());
    }

    @Override
    public UpgradeCmd help() {
        return ((UpgradeCmd) super.help());
    }

    @Override
    public UpgradeCmd kubeAsGroup(String... kubeAsGroup) {
        return ((UpgradeCmd) super.kubeAsGroup(kubeAsGroup));
    }

    @Override
    public UpgradeCmd kubeCAFile(String kubeCAFile) {
        return ((UpgradeCmd) super.kubeCAFile(kubeCAFile));
    }

    @Override
    public UpgradeCmd kubeContext(String kubeContext) {
        return ((UpgradeCmd) super.kubeContext(kubeContext));
    }

    @Override
    public UpgradeCmd kubeconfig(String kubeconfig) {
        super.kubeconfig(kubeconfig);
        return this;
    }

    @Override
    public UpgradeCmd kubeAPIServer(String kubeAPIServer) {
        super.kubeAPIServer(kubeAPIServer);
        return this;
    }

    @Override
    public UpgradeCmd namespace(String namespace) {
        super.namespace(namespace);
        return this;
    }

    @Override
    public UpgradeCmd kubeToken(String kubeToken) {
        super.kubeToken(kubeToken);
        return this;
    }

    @Override
    public UpgradeCmd kubeTLSServerName(String kubeTLSServerName) {
        super.kubeTLSServerName(kubeTLSServerName);
        return this;
    }

    @Override
    public UpgradeCmd kubeInsecureSkipTLSVerify() {
        super.kubeInsecureSkipTLSVerify();
        return this;
    }

    @Override
    public UpgradeCmd registryConfig(String registryConfig) {
        super.registryConfig(registryConfig);
        return this;
    }

    @Override
    public UpgradeCmd repositoryConfig(String repositoryConfig) {
        super.repositoryConfig(repositoryConfig);
        return this;
    }

    @Override
    public UpgradeCmd repositoryCache(String repositoryCache) {
        super.repositoryCache(repositoryCache);
        return this;
    }

    @Override
    public UpgradeCmd kubeAsUser(String kubeAsUser) {
        return ((UpgradeCmd) super.kubeAsUser(kubeAsUser));
    }

    public UpgradeCmd() {
        super();
        this.cmds.add(COMMAND_NAME);
    }

    public UpgradeCmd buildArgs() {
        this.args = new ArrayList<>();
        if (releaseName != null) {
            this.args.add(releaseName);
        }
        if (chart != null) {
            this.args.add(chart);
        }
        if (atomic) {
            this.args.add("--atomic");
        }
        if (caFile != null) {
            this.args.add("--ca-file");
            this.args.add(caFile);
        }
        if (certFile != null) {
            this.args.add("--cert-file");
            this.args.add(certFile);
        }
        if (cleanupOnFail) {
            this.args.add("--cleanup-on-fail");
        }
        if (createNamespace) {
            this.args.add("--create-namespace");
        }
        if (dependencyUpdate) {
            this.args.add("--dependency-update");
        }
        if (description != null) {
            this.args.add("--description");
            this.args.add(description);
        }
        if (devel) {
            this.args.add("--devel");
        }
        if (disableOpenAPIValidation) {
            this.args.add("--disable-openapi-validation");
        }
        if (dryRun) {
            this.args.add("--dry-run");
        }
        if (enableDNS) {
            this.args.add("--enable-dns");
        }
        if (force) {
            this.args.add("--force");
        }
        if (historyMax > 0) {
            this.args.add("--history-max");
            this.args.add(Integer.toString(historyMax));
        }
        if (insecureSkipTLSVerify) {
            this.args.add("--insecure-skip-tls-verify");
        }
        if (install) {
            this.args.add("-i");
        }
        if (keyFile != null) {
            this.args.add("--key-file");
            this.args.add(keyFile);
        }
        if (keyring != null) {
            this.args.add("--keyring");
            this.args.add(keyring);
        }
        if (noHooks) {
            this.args.add("--no-hooks");
        }
        if (output != null) {
            this.args.add("--output");
            this.args.add(output);
        }
        if (passCredentials) {
            this.args.add("--pass-credentials");
        }
        if (password != null) {
            this.args.add("--password");
            this.args.add(password);
        }
        if (postRenderer != null) {
            this.args.add("--post-renderer");
            this.args.add(postRenderer);
        }
        if (!postRendererArgs.isEmpty()) {
            this.args.add("--post-renderer-args");
            this.args.add(String.join(",", postRendererArgs));
        }
        if (renderSubchartNotes) {
            this.args.add("--render-subchart-notes");
        }
        if (repo != null) {
            this.args.add("--repo");
            this.args.add(repo);
        }
        if (resetValues) {
            this.args.add("--reset-values");
        }

        if (!values.isEmpty()) {
            List<String> valueFiles = super.createTempFiles(values.toArray(String[]::new));
            for (String value : valueFiles) {
                this.args.add("-f");
                this.args.add(value);
            }
        }
        if (!set.isEmpty()) {
            for (String s : set) {
                this.args.add("--set");
                this.args.add(s);
            }
        }
        if (!setFile.isEmpty()) {
            List<String> valueFiles = super.createTempFiles(setFile.toArray(String[]::new));
            for (String s : valueFiles) {
                this.args.add("--set-file");
                this.args.add(s);
            }
        }
        if (!setJSON.isEmpty()) {
            for (String s : setJSON) {
                this.args.add("--set-json");
                this.args.add(s);
            }
        }
        if (!setString.isEmpty()) {
            for (String s : setString) {
                this.args.add("--set-string");
                this.args.add(s);
            }
        }
        if (skipCrds) {
            this.args.add("--skip-crds");
        }
        if (timeout != null) {
            this.args.add("--timeout");
            this.args.add(timeout.toString());
        }
        if (username != null) {
            this.args.add("--username");
            this.args.add(username);
        }
        if (verify) {
            this.args.add("--verify");
        }
        if (version != null) {
            this.args.add("--version");
            this.args.add(version);
        }
        if (wait) {
            this.args.add("--wait");
        }
        if (waitForJobs) {
            this.args.add("--wait-for-jobs");
        }
        super.buildArgs();
        return this;
    }

    @Override
    public String buildCmd() {
        this.buildArgs();
        return super.buildCmd();
    }

    public String exec() {
        return super.exec();
    }

    @Override
    public String exec(long timoutMillSeconds) {
        return super.exec(timoutMillSeconds);
    }

    @Override
    public <T> T execToObj(Class<T> clazz) {
        return super.execToObj(clazz);
    }

    @Override
    public <T> List<T> execToObjs(Class<T> clazz) {
        return super.execToObjs(clazz);
    }
}
