package org.hzhq.myutil.utils.helm.helm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-09 上午1:53
 * helm package --help
 */
public class PackageCmd extends RootCmd implements GlobalFlags<PackageCmd> {
    private static final String COMMAND_NAME = "package";
    private final List<String> chartPaths = new ArrayList<>();
    private String appVersion;
    private boolean dependencyUpdate;
    private String destination = ".";
    private String help;
    private String key;
    private String keyring;
    private String passphraseFile;
    private boolean sign;
    private String version;

    public PackageCmd chartPath(String chartPath) {
        this.chartPaths.add(chartPath);
        return this;
    }

    public PackageCmd appVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public PackageCmd dependencyUpdate() {
        this.dependencyUpdate = true;
        return this;
    }

    public PackageCmd destination(String destination) {
        this.destination = destination;
        return this;
    }

    public PackageCmd help(String help) {
        this.help = help;
        return this;
    }

    public PackageCmd key(String key) {
        this.key = key;
        return this;
    }

    public PackageCmd keyring(String keyring) {
        this.keyring = keyring;
        return this;
    }

    public PackageCmd passphraseFile(String passphraseFile) {
        this.passphraseFile = passphraseFile;
        return this;
    }

    public PackageCmd sign() {
        this.sign = true;
        return this;
    }

    public PackageCmd version(String version) {
        this.version = version;
        return this;
    }

    @Override
    public PackageCmd burstLimit(Integer burstLimit) {
        return ((PackageCmd) super.burstLimit(burstLimit));
    }

    @Override
    public PackageCmd debug() {
        return ((PackageCmd) super.debug());
    }

    @Override
    public PackageCmd help() {
        return ((PackageCmd) super.help());
    }

    @Override
    public PackageCmd kubeAsGroup(String... kubeAsGroup) {
        return ((PackageCmd) super.kubeAsGroup(kubeAsGroup));
    }

    @Override
    public PackageCmd kubeAsUser(String kubeAsUser) {
        return ((PackageCmd) super.kubeAsUser(kubeAsUser));
    }

    @Override
    public PackageCmd kubeCAFile(String kubeCAFile) {
        return ((PackageCmd) super.kubeCAFile(kubeCAFile));
    }

    @Override
    public PackageCmd kubeContext(String kubeContext) {
        return ((PackageCmd) super.kubeContext(kubeContext));
    }

    @Override
    public PackageCmd kubeconfig(String kubeconfig) {
        super.kubeconfig(kubeconfig);
        return this;
    }

    @Override
    public PackageCmd kubeAPIServer(String kubeAPIServer) {
        super.kubeAPIServer(kubeAPIServer);
        return this;
    }

    @Override
    public PackageCmd namespace(String namespace) {
        super.namespace(namespace);
        return this;
    }

    @Override
    public PackageCmd kubeToken(String kubeToken) {
        super.kubeToken(kubeToken);
        return this;
    }

    @Override
    public PackageCmd kubeTLSServerName(String kubeTLSServerName) {
        super.kubeTLSServerName(kubeTLSServerName);
        return this;
    }

    @Override
    public PackageCmd kubeInsecureSkipTLSVerify() {
        super.kubeInsecureSkipTLSVerify();
        return this;
    }

    @Override
    public PackageCmd registryConfig(String registryConfig) {
        super.registryConfig(registryConfig);
        return this;
    }

    @Override
    public PackageCmd repositoryConfig(String repositoryConfig) {
        super.repositoryConfig(repositoryConfig);
        return this;
    }

    @Override
    public PackageCmd repositoryCache(String repositoryCache) {
        super.repositoryCache(repositoryCache);
        return this;
    }

    public PackageCmd buildArgs() {
        this.args.add(COMMAND_NAME);
        this.args.addAll(this.chartPaths);
        if (appVersion != null) {
            this.args.add("--app-version");
            this.args.add(appVersion);
        }
        if (dependencyUpdate) {
            this.args.add("--dependency-update");
        }
        if (destination != null) {
            this.args.add("--destination");
            this.args.add(destination);
        }
        if (help != null) {
            this.args.add("--help");
            this.args.add(help);
        }
        if (key != null) {
            this.args.add("--key");
            this.args.add(key);
        }
        if (keyring != null) {
            this.args.add("--keyring");
            this.args.add(keyring);
        }
        if (passphraseFile != null) {
            this.args.add("--passphrase-file");
            this.args.add(passphraseFile);
        }
        if (sign) {
            this.args.add("--sign");
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
        return super.exec();
    }

    @Override
    public <T> T execToObj(Class<T> clazz) {
        this.buildArgs();
        return super.execToObj(clazz);
    }

    @Override
    public <T> List<T> execToObjs(Class<T> clazz) {
        this.buildArgs();
        return super.execToObjs(clazz);
    }
}
