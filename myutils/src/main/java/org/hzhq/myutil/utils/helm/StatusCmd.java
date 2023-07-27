package org.hzhq.myutil.utils.helm;


import java.util.*;

public class StatusCmd extends RootCmd implements GlobalFlags<StatusCmd> {

    private static final String COMMAND_NAME = "status";

    private String releaseName;

    private String output;

    private int revision;

    private boolean showDesc;

    public StatusCmd releaseName(String releaseName) {
        this.releaseName = releaseName;
        return this;
    }

    public StatusCmd output(OutputType outputType) {
        this.output = outputType.name();
        return this;
    }

    public StatusCmd revision(int revision) {
        this.revision = revision;
        return this;
    }

    public StatusCmd showDesc() {
        this.showDesc = true;
        return this;
    }

    @Override
    public StatusCmd burstLimit(Integer burstLimit) {
        return (StatusCmd)super.burstLimit(burstLimit);
    }

    @Override
    public StatusCmd debug() {
        return (StatusCmd)super.debug();
    }

    @Override
    public StatusCmd help() {
        return (StatusCmd)super.help();
    }

    @Override
    public StatusCmd kubeAPIServer(String kubeAPIServer) {
        return (StatusCmd)super.kubeAPIServer(kubeAPIServer);
    }

    @Override
    public StatusCmd kubeAsUser(String kubeAsUser) {
        return (StatusCmd)super.kubeAsUser(kubeAsUser);
    }

    @Override
    public StatusCmd kubeAsGroup(String... kubeAsGroup) {
        return (StatusCmd)super.kubeAsGroup(kubeAsGroup);
    }

    @Override
    public StatusCmd kubeCAFile(String kubeCAFile) {
        return (StatusCmd)super.kubeCAFile(kubeCAFile);
    }

    @Override
    public StatusCmd kubeContext(String kubeContext) {
        return (StatusCmd)super.kubeContext(kubeContext);
    }

    @Override
    public StatusCmd kubeInsecureSkipTLSVerify() {
        return (StatusCmd)super.kubeInsecureSkipTLSVerify();
    }

    @Override
    public StatusCmd kubeTLSServerName(String kubeTLSServerName) {
        return (StatusCmd)super.kubeTLSServerName(kubeTLSServerName);
    }

    @Override
    public StatusCmd kubeToken(String kubeToken) {
        return (StatusCmd)super.kubeToken(kubeToken);
    }

    @Override
    public StatusCmd kubeconfig(String kubeconfig) {
        return (StatusCmd)super.kubeconfig(kubeconfig);
    }

    @Override
    public StatusCmd namespace(String namespace) {
        return (StatusCmd)super.namespace(namespace);
    }

    @Override
    public StatusCmd registryConfig(String registryConfig) {
        return (StatusCmd)super.registryConfig(registryConfig);
    }

    @Override
    public StatusCmd repositoryCache(String repositoryCache) {
        return (StatusCmd)super.repositoryCache(repositoryCache);
    }

    @Override
    public StatusCmd repositoryConfig(String repositoryConfig) {
        return (StatusCmd)super.repositoryConfig(repositoryConfig);
    }

    public StatusCmd() {
        super();
        this.cmds.add(COMMAND_NAME);
    }

    @Override
    public StatusCmd buildArgs() {
        List<String> args = new ArrayList<>();
        if (releaseName != null) {
            args.add(releaseName);
        }
        if (revision > 0) {
            args.add("--revision");
            args.add(Integer.toString(revision));
        }
        if (output != null) {
            args.add("--output");
            args.add(output);
        }
        if (showDesc) {
            args.add("--show-desc");
        }
        this.args = args;
        super.buildArgs();
        return this;
    }

    @Override
    protected String buildCmd() {
        return super.buildCmd();
    }

    @Override
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




}
