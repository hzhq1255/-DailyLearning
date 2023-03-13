package org.hzhq.myutil.utils.helm.helm;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-09 14:06
 * helm template --help
 *
 */
public class TemplateCmd extends RootCmd implements GlobalFlags<TemplateCmd> {

    private static final String COMMAND_NAME = "template";
    private String name;
    private String chart;
    private List<String> apiVersions;
    private boolean atomic;
    private String caFile;
    private String certFile;
    private boolean createNamespace;
    private boolean dependencyUpdate;
    private String description;
    private boolean devel;
    private boolean disableOpenAPIValidation;
    private boolean dryRun;
    private boolean enableDns;
    private boolean force;
    private boolean generateName;
    private boolean includeCrds;
    private boolean insecureSkipTlsVerify;
    private boolean isUpgrade;
    private String keyFile;
    private String keyring;
    private String kubeVersion;
    private String nameTemplate;
    private boolean noHooks;
    private String outputDir;
    private boolean passCredentials;
    private String password;
    private String postRenderer;
    private final List<String> postRendererArgs = new ArrayList<>();
    private boolean releaseName;
    private boolean renderSubchartNotes;
    private boolean replace;
    private String repo;
    private final List<String> set = new ArrayList<>();
    private final List<String> setFile = new ArrayList<>();
    private final List<String> setJSON = new ArrayList<>();
    private final List<String> setString = new ArrayList<>();
    private final List<String> showOnly = new ArrayList<>();
    private boolean skipCrds;
    private boolean skipTests;
    private Duration timeout;
    private String username;
    private boolean validate;
    private final List<String> values = new ArrayList<>();
    private boolean verify;
    private String version;
    private boolean wait;
    private boolean waitForJobs;

    public TemplateCmd name(String name) {
        this.name = name;
        return this;
    }

    public TemplateCmd chart(String chart) {
        this.chart = chart;
        return this;
    }

    public TemplateCmd apiVersions(List<String> apiVersions) {
        this.apiVersions = apiVersions;
        return this;
    }

    public TemplateCmd atomic() {
        this.atomic = true;
        return this;
    }

    public TemplateCmd caFile(String caFile) {
        this.caFile = caFile;
        return this;
    }

    public TemplateCmd certFile(String certFile) {
        this.certFile = certFile;
        return this;
    }

    public TemplateCmd createNamespace() {
        this.createNamespace = true;
        return this;
    }

    public TemplateCmd dependencyUpdate() {
        this.dependencyUpdate = true;
        return this;
    }

    public TemplateCmd description(String description) {
        this.description = description;
        return this;
    }

    public TemplateCmd devel() {
        this.devel = true;
        return this;
    }

    public TemplateCmd disableOpenAPIValidation() {
        this.disableOpenAPIValidation = true;
        return this;
    }

    public TemplateCmd dryRun() {
        this.dryRun = true;
        return this;
    }

    public TemplateCmd enableDns() {
        this.enableDns = true;
        return this;
    }

    public TemplateCmd force() {
        this.force = true;
        return this;
    }

    public TemplateCmd generateName() {
        this.generateName = true;
        return this;
    }

    public TemplateCmd includeCrds() {
        this.includeCrds = true;
        return this;
    }

    public TemplateCmd insecureSkipTlsVerify() {
        this.insecureSkipTlsVerify = true;
        return this;
    }

    public TemplateCmd isUpgrade() {
        this.isUpgrade = true;
        return this;
    }

    public TemplateCmd keyFile(String keyFile) {
        this.keyFile = keyFile;
        return this;
    }

    public TemplateCmd keyring(String keyring) {
        this.keyring = keyring;
        return this;
    }

    public TemplateCmd kubeVersion(String kubeVersion) {
        this.kubeVersion = kubeVersion;
        return this;
    }

    public TemplateCmd nameTemplate(String nameTemplate) {
        this.nameTemplate = nameTemplate;
        return this;
    }

    public TemplateCmd noHooks() {
        this.noHooks = true;
        return this;
    }

    public TemplateCmd outputDir(String outputDir) {
        this.outputDir = outputDir;
        return this;
    }

    public TemplateCmd passCredentials(boolean passCredentials) {
        this.passCredentials = passCredentials;
        return this;
    }

    public TemplateCmd password(String password) {
        this.password = password;
        return this;
    }

    public TemplateCmd postRenderer(String postRenderer) {
        this.postRenderer = postRenderer;
        return this;
    }

    public TemplateCmd postRendererArgs(String ...postRendererArgs) {
        this.postRendererArgs.addAll(Arrays.asList(postRendererArgs));
        return this;
    }

    public TemplateCmd releaseName() {
        this.releaseName = true;
        return this;
    }

    public TemplateCmd renderSubchartNotes() {
        this.renderSubchartNotes = true;
        return this;
    }

    public TemplateCmd replace() {
        this.replace = true;
        return this;
    }

    public TemplateCmd repo(String repo) {
        this.repo = repo;
        return this;
    }

    public TemplateCmd set(String ...set) {
        this.set.addAll(Arrays.asList(set));
        return this;
    }

    public TemplateCmd setFile(String ...setFile) {
        this.setFile.addAll(Arrays.asList(setFile));
        return this;
    }

    public TemplateCmd setJSON(String ...setJSON) {
        this.setJSON.addAll(Arrays.asList(setJSON));
        return this;
    }

    public TemplateCmd setString(String ...setString) {
        this.setString.addAll(Arrays.asList(setString));
        return this;
    }

    public TemplateCmd showOnly(String ...showOnly) {
        this.showOnly.addAll(Arrays.asList(showOnly));
        return this;
    }

    public TemplateCmd skipCrds() {
        this.skipCrds = true;
        return this;
    }

    public TemplateCmd skipTests() {
        this.skipTests = true;
        return this;
    }

    public TemplateCmd timeout(Duration timeout) {
        this.timeout = timeout;
        return this;
    }

    public TemplateCmd username(String username) {
        this.username = username;
        return this;
    }

    public TemplateCmd validate() {
        this.validate = true;
        return this;
    }

    public TemplateCmd values(List<String> values) {
        this.values.addAll(values);
        return this;
    }

    public TemplateCmd verify() {
        this.verify = true;
        return this;
    }

    public TemplateCmd version(String version) {
        this.version = version;
        return this;
    }

    public TemplateCmd waitArg() {
        this.wait = true;
        return this;
    }

    public TemplateCmd waitForJobs() {
        this.waitForJobs = true;
        return this;
    }


    @Override
    public TemplateCmd burstLimit(Integer burstLimit) {
        return ((TemplateCmd) super.burstLimit(burstLimit));
    }

    @Override
    public TemplateCmd debug() {
        return ((TemplateCmd) super.debug());
    }

    @Override
    public TemplateCmd help() {
        return ((TemplateCmd) super.help());
    }

    @Override
    public TemplateCmd kubeAsGroup(String... kubeAsGroup) {
        return ((TemplateCmd) super.kubeAsGroup(kubeAsGroup));
    }

    @Override
    public TemplateCmd kubeCAFile(String kubeCAFile) {
        return ((TemplateCmd) super.kubeCAFile(kubeCAFile));
    }

    @Override
    public TemplateCmd kubeContext(String kubeContext) {
        return ((TemplateCmd) super.kubeContext(kubeContext));
    }

    @Override
    public TemplateCmd kubeconfig(String kubeconfig) {
        super.kubeconfig(kubeconfig);
        return this;
    }

    @Override
    public TemplateCmd kubeAPIServer(String kubeAPIServer) {
        super.kubeAPIServer(kubeAPIServer);
        return this;
    }

    @Override
    public TemplateCmd namespace(String namespace) {
        super.namespace(namespace);
        return this;
    }

    @Override
    public TemplateCmd kubeToken(String kubeToken) {
        super.kubeToken(kubeToken);
        return this;
    }

    @Override
    public TemplateCmd kubeTLSServerName(String kubeTLSServerName) {
        super.kubeTLSServerName(kubeTLSServerName);
        return this;
    }

    @Override
    public TemplateCmd kubeInsecureSkipTLSVerify() {
        super.kubeInsecureSkipTLSVerify();
        return this;
    }

    @Override
    public TemplateCmd registryConfig(String registryConfig) {
        super.registryConfig(registryConfig);
        return this;
    }

    @Override
    public TemplateCmd repositoryConfig(String repositoryConfig) {
        super.repositoryConfig(repositoryConfig);
        return this;
    }

    @Override
    public TemplateCmd repositoryCache(String repositoryCache) {
        super.repositoryCache(repositoryCache);
        return this;
    }

    @Override
    public TemplateCmd kubeAsUser(String kubeAsUser) {
        return ((TemplateCmd) super.kubeAsUser(kubeAsUser));
    }

    public TemplateCmd buildArgs() {
        this.args.add(COMMAND_NAME);
        if (name != null) {
            this.args.add(name);
        }
        if (chart != null) {
            this.args.add(chart);
        }
        if (apiVersions != null) {
            for (String version : apiVersions) {
                this.args.add("-a");
                this.args.add(version);
            }
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
        if (enableDns) {
            this.args.add("--enable-dns");
        }
        if (force) {
            this.args.add("--force");
        }
        if (generateName) {
            this.args.add("-g");
        }
        if (includeCrds) {
            this.args.add("--include-crds");
        }
        if (insecureSkipTlsVerify) {
            this.args.add("--insecure-skip-tls-verify");
        }
        if (isUpgrade) {
            this.args.add("--is-upgrade");
        }
        if (keyFile != null) {
            this.args.add("--key-file");
            this.args.add(keyFile);
        }
        if (keyring != null) {
            this.args.add("--keyring");
            this.args.add(keyring);
        }
        if (kubeVersion != null) {
            this.args.add("--kube-version");
            this.args.add(kubeVersion);
        }
        if (nameTemplate != null) {
            this.args.add("--name-template");
            this.args.add(nameTemplate);
        }
        if (noHooks) {
            this.args.add("--no-hooks");
        }
        if (outputDir != null) {
            this.args.add("--output-dir");
            this.args.add(outputDir);
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
            for (String arg : postRendererArgs) {
                this.args.add("--post-renderer-args");
                this.args.add(arg);
            }
        }
        if (releaseName) {
            this.args.add("--release-name");
        }
        if (renderSubchartNotes) {
            this.args.add("--render-subchart-notes");
        }
        if (replace) {
            this.args.add("--replace");
        }
        if (repo != null) {
            this.args.add("--repo");
            this.args.add(repo);
        }
        if (!set.isEmpty()){
            for (String s : set) {
                this.args.add("--set");
                this.args.add(s);
            }
        }
        if (!setFile.isEmpty()){
            List<String> valueFiles = super.createTempFiles(setFile.toArray(String[]::new));
            for (String s : valueFiles) {
                this.args.add("--set-file");
                this.args.add(s);
            }
        }
        if (!setJSON.isEmpty()){
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
        if (!showOnly.isEmpty()) {
            for (String s : showOnly) {
                this.args.add("--show-only");
                this.args.add(s);
            }
        }

        if (skipCrds) {
            this.args.add("--skip-crds");
        }
        if (skipTests) {
            this.args.add("--skip-tests");
        }
        if (timeout != null) {
            this.args.add("--timeout");
            this.args.add(timeout.toString());
        }
        if (username != null) {
            this.args.add("--username");
            this.args.add(username);
        }
        if (validate) {
            this.args.add("--validate");
        }
        if (!values.isEmpty()){
            List<String> valueFiles = super.createTempFiles(values.toArray(String[]::new));
            for (String value : valueFiles) {
                this.args.add("-f");
                this.args.add(value);
            }
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
    protected String buildCmd() {
        this.buildArgs();
        return super.buildCmd();
    }

    @Override
    public String exec() {
        this.buildArgs();
        String result =  super.exec();
        List<String> tempFiles = new ArrayList<>();
        tempFiles.addAll(this.setFile);
        tempFiles.addAll(this.values);
        cleanTempFiles(tempFiles.toArray(String[]::new));
        return result;
    }

    @Override
    public <T> T execToObj(Class<T> clazz) {
        this.buildArgs();
        T obj = super.execToObj(clazz);
        List<String> tempFiles = new ArrayList<>();
        tempFiles.addAll(this.setFile);
        tempFiles.addAll(this.values);
        cleanTempFiles(tempFiles.toArray(String[]::new));
        return obj;
    }

    @Override
    public <T> List<T> execToObjs(Class<T> clazz) {
        this.buildArgs();
        List<T> objs = super.execToObjs(clazz);
        List<String> tempFiles = new ArrayList<>();
        tempFiles.addAll(this.setFile);
        tempFiles.addAll(this.values);
        cleanTempFiles(tempFiles.toArray(String[]::new));
        return objs;
    }
}
