package org.hzhq.myutil.utils.helm.helm;

import java.util.List;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-09 上午1:23
 * helm create --help
 */
public class CreateCmd extends RootCmd implements GlobalFlags<CreateCmd> {
    private static final String COMMAND_NAME = "create";
    private String name;
    private String starter;

    public CreateCmd name(String name) {
        this.name = name;
        return this;
    }



    public CreateCmd starter(String starter) {
        this.starter = starter;
        return this;
    }

    @Override
    public CreateCmd kubeconfig(String kubeconfig) {
        super.kubeconfig(kubeconfig);
        return this;
    }

    @Override
    public CreateCmd burstLimit(Integer burstLimit) {
        super.burstLimit(burstLimit);
        return this;
    }

    @Override
    public CreateCmd debug() {
        super.debug();
        return this;
    }

    @Override
    public CreateCmd help() {
        this.help = true;
        return this;
    }

    @Override
    public CreateCmd kubeAPIServer(String kubeAPIServer) {
        super.kubeAPIServer(kubeAPIServer);
        return this;
    }

    @Override
    public CreateCmd kubeAsUser(String kubeAsUser) {
        return (CreateCmd) super.kubeAsUser(kubeAsUser);
    }


    @Override
    public CreateCmd kubeAsGroup(String... kubeAsGroup) {
        super.kubeAsGroup(kubeAsGroup);
        return this;
    }

    @Override
    public CreateCmd kubeCAFile(String kubeCAFile) {
        super.kubeCAFile(kubeCAFile);
        return this;
    }

    @Override
    public CreateCmd kubeContext(String kubeContext) {
        super.kubeContext(kubeContext);
        return this;
    }

    @Override
    public CreateCmd namespace(String namespace) {
        super.namespace(namespace);
        return this;
    }

    @Override
    public CreateCmd kubeToken(String kubeToken) {
        super.kubeToken(kubeToken);
        return this;
    }

    @Override
    public CreateCmd kubeTLSServerName(String kubeTLSServerName) {
        super.kubeTLSServerName(kubeTLSServerName);
        return this;
    }

    @Override
    public CreateCmd kubeInsecureSkipTLSVerify() {
        super.kubeInsecureSkipTLSVerify();
        return this;
    }

    @Override
    public CreateCmd registryConfig(String registryConfig) {
        super.registryConfig(registryConfig);
        return this;
    }

    @Override
    public CreateCmd repositoryConfig(String repositoryConfig) {
        super.repositoryConfig(repositoryConfig);
        return this;
    }

    @Override
    public CreateCmd repositoryCache(String repositoryCache) {
        super.repositoryCache(repositoryCache);
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
        return super.execToObj(clazz);
    }

    @Override
    public <T> List<T> execToObjs(Class<T> clazz) {
        return super.execToObjs(clazz);
    }

    public CreateCmd buildArgs() {
        this.args.add(COMMAND_NAME);
        if (name != null) {
            this.args.add(name);
        }
        if (starter != null) {
            this.args.add("-p");
            this.args.add(starter);
        }
        super.buildArgs();
        return this;
    }


}


