package org.hzhq.myutil.utils.helm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-09 13:57 <br/>
 *        helm history --help <br/>
 */
public class HistoryCmd extends RootCmd implements GlobalFlags<HistoryCmd> {
    private static final String COMMAND_NAME = "history";
    private String releaseName;
    private int max;
    private String output;

    public HistoryCmd releaseName(String releaseName) {
        this.releaseName = releaseName;
        return this;
    }

    @Override
    public HistoryCmd help() {
        this.help = true;
        return this;
    }

    public HistoryCmd max(int max) {
        this.max = max;
        return this;
    }

    public HistoryCmd output(String output) {
        this.output = output;
        return this;
    }

    public HistoryCmd output(OutputType type) {
        this.output = type.name();
        return this;
    }

    @Override
    public HistoryCmd burstLimit(Integer burstLimit) {
        return (HistoryCmd)super.burstLimit(burstLimit);
    }

    @Override
    public HistoryCmd debug() {
        return (HistoryCmd)super.debug();
    }

    @Override
    public HistoryCmd kubeAsGroup(String... kubeAsGroup) {
        return (HistoryCmd)super.kubeAsGroup(kubeAsGroup);
    }

    @Override
    public HistoryCmd kubeCAFile(String kubeCAFile) {
        return (HistoryCmd)super.kubeCAFile(kubeCAFile);
    }

    @Override
    public HistoryCmd kubeContext(String kubeContext) {
        return (HistoryCmd)super.kubeContext(kubeContext);
    }

    @Override
    public HistoryCmd kubeAsUser(String kubeAsUser) {
        return ((HistoryCmd)super.kubeAsUser(kubeAsUser));
    }

    @Override
    public HistoryCmd kubeconfig(String kubeconfig) {
        return (HistoryCmd)super.kubeconfig(kubeconfig);
    }

    @Override
    public HistoryCmd kubeAPIServer(String kubeAPIServer) {
        return (HistoryCmd)super.kubeAPIServer(kubeAPIServer);
    }

    @Override
    public HistoryCmd namespace(String namespace) {
        return (HistoryCmd)super.namespace(namespace);
    }

    @Override
    public HistoryCmd kubeToken(String kubeToken) {
        return (HistoryCmd)super.kubeToken(kubeToken);
    }

    @Override
    public HistoryCmd kubeTLSServerName(String kubeTLSServerName) {
        return (HistoryCmd)super.kubeTLSServerName(kubeTLSServerName);
    }

    @Override
    public HistoryCmd kubeInsecureSkipTLSVerify() {
        return (HistoryCmd)super.kubeInsecureSkipTLSVerify();
    }

    @Override
    public HistoryCmd registryConfig(String registryConfig) {
        return (HistoryCmd)super.registryConfig(registryConfig);
    }

    @Override
    public HistoryCmd repositoryConfig(String repositoryConfig) {
        return (HistoryCmd)super.repositoryConfig(repositoryConfig);
    }

    @Override
    public HistoryCmd repositoryCache(String repositoryCache) {
        return (HistoryCmd)super.repositoryCache(repositoryCache);
    }

    public HistoryCmd() {
        super();
        this.cmds.add(COMMAND_NAME);
    }

    @Override
    public HistoryCmd buildArgs() {
        this.args = new ArrayList<>();
        if (releaseName != null) {
            this.args.add(releaseName);
        }
        if (max > 0) {
            this.args.add("--max");
            this.args.add(Integer.toString(max));
        }
        if (output != null) {
            this.args.add("--output");
            this.args.add(output);
        }
        super.buildArgs();
        return this;
    }

    @Override
    public String buildCmd() {
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

    @Override
    public <T> List<T> execToObjs(Class<T> clazz) {
        return super.execToObjs(clazz);
    }

}
