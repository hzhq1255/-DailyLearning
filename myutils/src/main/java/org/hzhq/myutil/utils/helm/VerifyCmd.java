package org.hzhq.myutil.utils.helm.helm;

import java.util.List;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-09 14:32
 * helm verify --help
 */
public class VerifyCmd extends RootCmd implements GlobalFlags<VerifyCmd>{

    private static final String COMMAND_NAME = "verify";
    private String path;
    private String keyring;

    @Override
    public VerifyCmd burstLimit(Integer burstLimit) {
        return ((VerifyCmd) super.burstLimit(burstLimit));
    }

    @Override
    public VerifyCmd debug() {
        return ((VerifyCmd) super.debug());
    }

    @Override
    public VerifyCmd help() {
        return ((VerifyCmd) super.help());
    }

    @Override
    public VerifyCmd kubeAsGroup(String... kubeAsGroup) {
        return ((VerifyCmd) super.kubeAsGroup(kubeAsGroup));
    }

    @Override
    public VerifyCmd kubeCAFile(String kubeCAFile) {
        return ((VerifyCmd) super.kubeCAFile(kubeCAFile));
    }

    @Override
    public VerifyCmd kubeContext(String kubeContext) {
        return ((VerifyCmd) super.kubeContext(kubeContext));
    }

    @Override
    public VerifyCmd kubeconfig(String kubeconfig) {
        super.kubeconfig(kubeconfig);
        return this;
    }

    @Override
    public VerifyCmd kubeAPIServer(String kubeAPIServer) {
        super.kubeAPIServer(kubeAPIServer);
        return this;
    }

    @Override
    public VerifyCmd namespace(String namespace) {
        super.namespace(namespace);
        return this;
    }

    @Override
    public VerifyCmd kubeToken(String kubeToken) {
        super.kubeToken(kubeToken);
        return this;
    }

    @Override
    public VerifyCmd kubeTLSServerName(String kubeTLSServerName) {
        super.kubeTLSServerName(kubeTLSServerName);
        return this;
    }

    @Override
    public VerifyCmd kubeInsecureSkipTLSVerify() {
        super.kubeInsecureSkipTLSVerify();
        return this;
    }

    @Override
    public VerifyCmd registryConfig(String registryConfig) {
        super.registryConfig(registryConfig);
        return this;
    }

    @Override
    public VerifyCmd repositoryConfig(String repositoryConfig) {
        super.repositoryConfig(repositoryConfig);
        return this;
    }

    @Override
    public VerifyCmd repositoryCache(String repositoryCache) {
        super.repositoryCache(repositoryCache);
        return this;
    }


    @Override
    public VerifyCmd kubeAsUser(String kubeAsUser) {
        return ((VerifyCmd) super.kubeAsUser(kubeAsUser));
    }

    public VerifyCmd path(String path) {
        this.path = path;
        return this;
    }


    public VerifyCmd keyring(String keyring) {
        this.keyring = keyring;
        return this;
    }

    public VerifyCmd buildArgs() {
        this.args.add(COMMAND_NAME);
        if (path != null) {
            this.args.add(path);
        }
        if (keyring != null) {
            this.args.add("--keyring");
            this.args.add(keyring);
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
