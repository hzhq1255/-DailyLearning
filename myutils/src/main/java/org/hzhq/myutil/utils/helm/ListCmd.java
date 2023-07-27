package org.hzhq.myutil.utils.helm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-08 12:27 <br/>
 *        helm list --help
 */
public class ListCmd extends RootCmd implements GlobalFlags<ListCmd> {
    private static final String COMMAND_NAME = "list";
    private boolean all;
    private boolean allNamespaces;
    private boolean date;
    private boolean deployed;
    private boolean failed;
    private String filter;
    private int max;
    private boolean noHeaders;
    private int offset;
    private String output;
    private boolean pending;
    private boolean reverse;
    private String selector;
    private boolean shortOutput;
    private boolean superseded;
    private String timeFormat;
    private boolean uninstalled;
    private boolean uninstalling;

    public ListCmd all() {
        this.all = true;
        return this;
    }

    public ListCmd allNamespaces() {
        this.allNamespaces = true;
        return this;
    }

    public ListCmd date() {
        this.date = true;
        return this;
    }

    public ListCmd deployed() {
        this.deployed = true;
        return this;
    }

    public ListCmd failed() {
        this.failed = true;
        return this;
    }

    public ListCmd filter(String filter) {
        this.filter = filter;
        return this;
    }

    public ListCmd max(int max) {
        this.max = max;
        return this;
    }

    public ListCmd noHeaders() {
        this.noHeaders = true;
        return this;
    }

    public ListCmd offset(int offset) {
        this.offset = offset;
        return this;
    }

    public ListCmd output(String output) {
        this.output = output;
        return this;
    }

    public ListCmd output(OutputType type) {
        this.output = type.name();
        return this;
    }

    public ListCmd pending() {
        this.pending = true;
        return this;
    }

    public ListCmd reverse() {
        this.reverse = true;
        return this;
    }

    public ListCmd selector(String selector) {
        this.selector = selector;
        return this;
    }

    public ListCmd shortOutput() {
        this.shortOutput = true;
        return this;
    }

    public ListCmd superseded() {
        this.superseded = true;
        return this;
    }

    public ListCmd timeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
        return this;
    }

    public ListCmd uninstalled() {
        this.uninstalled = true;
        return this;
    }

    public ListCmd uninstalling() {
        this.uninstalling = true;
        return this;
    }

    @Override
    public ListCmd burstLimit(Integer burstLimit) {
        return ((ListCmd)super.burstLimit(burstLimit));
    }

    @Override
    public ListCmd debug() {
        return ((ListCmd)super.debug());
    }

    @Override
    public ListCmd help() {
        return ((ListCmd)super.help());
    }

    @Override
    public ListCmd kubeAsGroup(String... kubeAsGroup) {
        return ((ListCmd)super.kubeAsGroup(kubeAsGroup));
    }

    @Override
    public ListCmd kubeAsUser(String kubeAsUser) {
        return ((ListCmd)super.kubeAsUser(kubeAsUser));
    }

    @Override
    public ListCmd kubeCAFile(String kubeCAFile) {
        return ((ListCmd)super.kubeCAFile(kubeCAFile));
    }

    @Override
    public ListCmd kubeContext(String kubeContext) {
        return (ListCmd)super.kubeContext(kubeContext);
    }

    @Override
    public ListCmd kubeconfig(String kubeconfig) {
        super.kubeconfig(kubeconfig);
        return this;
    }

    @Override
    public ListCmd kubeAPIServer(String kubeAPIServer) {
        super.kubeAPIServer(kubeAPIServer);
        return this;
    }

    @Override
    public ListCmd namespace(String namespace) {
        super.namespace(namespace);
        return this;
    }

    @Override
    public ListCmd kubeToken(String kubeToken) {
        super.kubeToken(kubeToken);
        return this;
    }

    @Override
    public ListCmd kubeTLSServerName(String kubeTLSServerName) {
        super.kubeTLSServerName(kubeTLSServerName);
        return this;
    }

    @Override
    public ListCmd kubeInsecureSkipTLSVerify() {
        super.kubeInsecureSkipTLSVerify();
        return this;
    }

    @Override
    public ListCmd registryConfig(String registryConfig) {
        super.registryConfig(registryConfig);
        return this;
    }

    @Override
    public ListCmd repositoryConfig(String repositoryConfig) {
        super.repositoryConfig(repositoryConfig);
        return this;
    }

    @Override
    public ListCmd repositoryCache(String repositoryCache) {
        super.repositoryCache(repositoryCache);
        return this;
    }

    public ListCmd() {
        super();
        this.cmds.add(COMMAND_NAME);
    }

    @Override
    public ListCmd buildArgs() {
        this.args = new ArrayList<>();
        if (all) {
            this.args.add("--all");
        }
        if (allNamespaces) {
            this.args.add("--all-namespaces");
        }
        if (date) {
            this.args.add("--date");
        }
        if (deployed) {
            this.args.add("--deployed");
        }
        if (failed) {
            this.args.add("--failed");
        }
        if (filter != null) {
            this.args.add("--filter");
            this.args.add(filter);
        }
        if (max > 0) {
            this.args.add("--max");
            this.args.add(Integer.toString(max));
        }
        if (noHeaders) {
            this.args.add("--no-headers");
        }
        if (offset > 0) {
            this.args.add("--offset");
            this.args.add(Integer.toString(offset));
        }
        if (output != null) {
            this.args.add("--output");
            this.args.add(output);
        }
        if (pending) {
            this.args.add("--pending");
        }
        if (reverse) {
            this.args.add("--reverse");
        }

        if (selector != null) {
            this.args.add("-l");
            this.args.add(selector);
        }

        if (shortOutput) {
            this.args.add("-q");
        }

        if (superseded) {
            this.args.add("--superseded");
        }

        if (timeFormat != null) {
            this.args.add("--time-format");
            this.args.add(timeFormat);
        }

        if (uninstalled) {
            this.args.add("--uninstalled");
        }

        if (uninstalling) {
            this.args.add("--uninstalling");
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
