package org.hzhq.myutil.utils.helm.helm;


import com.skyview.caas.common.util.helm.response.HelmInstall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-09 11:50
 * helm install --help
 */
public class InstallCmd extends RootCmd implements GlobalFlags<InstallCmd> {
    private static final String COMMAND_NAME = "install";
    private String name;
    private String chart;
    private String caFile;
    private String certFile;
    private boolean createNamespace;
    private boolean dependencyUpdate;
    private String description;
    private boolean devel;
    private boolean disableOpenapiValidation;
    private boolean dryRun;
    private boolean atomic;
    private boolean enableDNS;
    private boolean force;
    private boolean generateName;
    private boolean insecureSkipTLSVerify;
    private String keyFile;
    private String keyRing;
    private String nameTemplate;
    private boolean noHooks;
    private String output;
    private boolean passCredentials;
    private String password;
    private String postRenderer;
    private final List<String> postRendererArgs = new ArrayList<>();
    private boolean renderSubchartNotes;
    private boolean replace;
    private String repo;
    private final List<String> set = new ArrayList<>();
    private final List<String> setFile = new ArrayList<>();
    private final List<String> setJSON = new ArrayList<>();
    private final List<String> setString = new ArrayList<>();
    private boolean skipCRDs;
    private String timeout;
    private String username;
    private final List<String> values = new ArrayList<>();
    private boolean verify;
    private String version;
//    private boolean help;


    public InstallCmd name(String name) {
        this.name = name;
        return this;
    }

    public InstallCmd chart(String chart) {
        this.chart = chart;
        return this;
    }


    public InstallCmd caFile(String caFile) {
        this.caFile = caFile;
        return this;
    }

    public InstallCmd certFile(String certFile) {
        this.certFile = certFile;
        return this;
    }

    public InstallCmd createNamespace() {
        this.createNamespace = true;
        return this;
    }

    public InstallCmd dependencyUpdate() {
        this.dependencyUpdate = true;
        return this;
    }

    public InstallCmd description(String description) {
        this.description = description;
        return this;
    }

    public InstallCmd devel() {
        this.devel = true;
        return this;
    }

    public InstallCmd disableOpenapiValidation() {
        this.disableOpenapiValidation = true;
        return this;
    }

    public InstallCmd dryRun() {
        this.dryRun = true;
        return this;
    }

    public InstallCmd atomic() {
        this.atomic = true;
        return this;
    }

    public InstallCmd enableDNS() {
        this.enableDNS = true;
        return this;
    }

    public InstallCmd force() {
        this.force = true;
        return this;
    }

    public InstallCmd generateName() {
        this.generateName = true;
        return this;
    }

    public InstallCmd insecureSkipTLSVerify() {
        this.insecureSkipTLSVerify = true;
        return this;
    }

    public InstallCmd keyFile(String keyFile) {
        this.keyFile = keyFile;
        return this;
    }

    public InstallCmd keyRing(String keyRing) {
        this.keyRing = keyRing;
        return this;
    }

    public InstallCmd nameTemplate(String nameTemplate) {
        this.nameTemplate = nameTemplate;
        return this;
    }

    public InstallCmd noHooks() {
        this.noHooks = true;
        return this;
    }

    public InstallCmd output(String output) {
        this.output = output;
        return this;
    }

    public InstallCmd output(OutputType type){
        this.output = type.name();
        return this;
    }

    public InstallCmd passCredentials() {
        this.passCredentials = true;
        return this;
    }

    public InstallCmd password(String password) {
        this.password = password;
        return this;
    }


    public InstallCmd postRenderer(String postRenderer) {
        this.postRenderer = postRenderer;
        return this;
    }

    public InstallCmd postRendererArgs(String... postRendererArgs) {
        this.postRendererArgs.addAll(Arrays.asList(postRendererArgs));
        return this;
    }

    public InstallCmd renderSubchartNotes() {
        this.renderSubchartNotes = true;
        return this;
    }

    public InstallCmd replace() {
        this.replace = true;
        return this;
    }

    public InstallCmd repo(String repo) {
        this.repo = repo;
        return this;
    }

    public InstallCmd set(String... set) {
        this.set.addAll(Arrays.asList(set));
        return this;
    }

    public InstallCmd setFile(String... setFile) {
        this.setFile.addAll(Arrays.asList(setFile));
        return this;
    }

    public InstallCmd setJSON(String... setJSON) {
        this.setJSON.addAll(Arrays.asList(setJSON));
        return this;
    }

    public InstallCmd setString(String... setString) {
        this.setString.addAll(Arrays.asList(setString));
        return this;
    }

    public InstallCmd skipCRDs() {
        this.skipCRDs = true;
        return this;
    }

    public InstallCmd timeout(String timeout) {
        this.timeout = timeout;
        return this;
    }

    public InstallCmd username(String username) {
        this.username = username;
        return this;
    }

    public InstallCmd values(String... values) {
        this.values.addAll(Arrays.asList(values));
        return this;
    }

    public InstallCmd verify() {
        this.verify = true;
        return this;
    }

    public InstallCmd version(String version) {
        this.version = version;
        return this;
    }

    @Override
    public InstallCmd help() {
        this.help =true;
        return this;
    }

    @Override
    public InstallCmd burstLimit(Integer burstLimit) {
        return (InstallCmd) super.burstLimit(burstLimit);
    }

    @Override
    public InstallCmd debug() {
        return (InstallCmd) super.debug();
    }

    @Override
    public InstallCmd kubeAsGroup(String... kubeAsGroup) {
        return (InstallCmd) super.kubeAsGroup(kubeAsGroup);
    }

    @Override
    public InstallCmd kubeAsUser(String kubeAsUser) {
        return ((InstallCmd) super.kubeAsUser(kubeAsUser));
    }

    @Override
    public InstallCmd kubeCAFile(String kubeCAFile) {
        return (InstallCmd) super.kubeCAFile(kubeCAFile);
    }

    @Override
    public InstallCmd kubeContext(String kubeContext) {
        return (InstallCmd) super.kubeContext(kubeContext);
    }

    @Override
    public InstallCmd kubeconfig(String kubeconfig) {
        super.kubeconfig(kubeconfig);
        return this;
    }

    @Override
    public InstallCmd kubeAPIServer(String kubeAPIServer) {
        super.kubeAPIServer(kubeAPIServer);
        return this;
    }

    @Override
    public InstallCmd namespace(String namespace) {
        super.namespace(namespace);
        return this;
    }

    @Override
    public InstallCmd kubeToken(String kubeToken) {
        super.kubeToken(kubeToken);
        return this;
    }

    @Override
    public InstallCmd kubeTLSServerName(String kubeTLSServerName) {
        super.kubeTLSServerName(kubeTLSServerName);
        return this;
    }

    @Override
    public InstallCmd kubeInsecureSkipTLSVerify() {
        super.kubeInsecureSkipTLSVerify();
        return this;
    }

    @Override
    public InstallCmd registryConfig(String registryConfig) {
        super.registryConfig(registryConfig);
        return this;
    }

    @Override
    public InstallCmd repositoryConfig(String repositoryConfig) {
        super.repositoryConfig(repositoryConfig);
        return this;
    }

    @Override
    public InstallCmd repositoryCache(String repositoryCache) {
        super.repositoryCache(repositoryCache);
        return this;
    }

    public InstallCmd buildArgs() {
        this.args.add(COMMAND_NAME);
        if (name != null) {
            this.args.add(name);
        }
        if (chart != null) {
            this.args.add(chart);
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
        if (disableOpenapiValidation) {
            this.args.add("--disable-openapi-validation");
        }
        if (dryRun) {
            this.args.add("--dry-run");
        }
        if (atomic) {
            this.args.add("--atomic");
        }
        if (generateName) {
            this.args.add("--generate-name");
        }
        if (enableDNS) {
            this.args.add("--enable-dns");
        }
        if (force) {
            this.args.add("--force");
        }
        if (insecureSkipTLSVerify) {
            this.args.add("--insecure-skip-tls-verify");
        }
        if (keyFile != null) {
            this.args.add("--key-file");
            this.args.add(keyFile);
        }
        if (keyRing != null) {
            this.args.add("--keyring");
            this.args.add(keyRing);
        }
        if (nameTemplate != null) {
            this.args.add("--name-template");
            this.args.add(nameTemplate);
        }
        if (noHooks) {
            this.args.add("--no-hooks");
        }
        if (output != null) {
            this.args.add("-o");
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
        if (replace) {
            this.args.add("--replace");
        }
        if (repo != null) {
            this.args.add("--repo");
            this.args.add(repo);
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
        if (skipCRDs) {
            this.args.add("--skip-crds");
        }
        if (timeout != null) {
            this.args.add("--timeout");
            this.args.add(timeout);
        }
        if (username != null) {
            this.args.add("--username");
            this.args.add(username);
        }
        if (!values.isEmpty()) {
            List<String> valueFiles = super.createTempFiles(values.toArray(String[]::new));
            for (String s : valueFiles) {
                this.args.add("-f");
                this.args.add(s);
            }
        }
        if (verify) {
            this.args.add("--verify");
        }
        if (version != null) {
            this.args.add("--version");
            this.args.add(version);
        }
        super.buildArgs();
        return this;
    }

    @Override
    public String buildCmd() {
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

    public HelmInstall get() {
        return this.execToObj(HelmInstall.class);
    }
}
