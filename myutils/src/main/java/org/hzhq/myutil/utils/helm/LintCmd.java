package org.hzhq.myutil.utils.helm.helm;

import java.util.List;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-09 上午1:48
 * helm lint --help
 */
public class LintCmd extends RootCmd implements GlobalFlags<LintCmd> {
    private static final String COMMAND_NAME = "lint";
    private String path;
    private boolean quiet;
    private String set;
    private String setFile;
    private String setJson;
    private String setString;
    private boolean strict;
    private List<String> values;
    private boolean withSubcharts;

    public LintCmd path(String path) {
        this.path = path;
        return this;
    }

    public LintCmd quiet(boolean quiet) {
        this.quiet = quiet;
        return this;
    }

    public LintCmd set(String set) {
        this.set = set;
        return this;
    }

    public LintCmd setFile(String setFile) {
        this.setFile = setFile;
        return this;
    }

    public LintCmd setJson(String setJson) {
        this.setJson = setJson;
        return this;
    }

    public LintCmd setString(String setString) {
        this.setString = setString;
        return this;
    }

    public LintCmd strict() {
        this.strict = true;
        return this;
    }

    public LintCmd values(List<String> values) {
        this.values = values;
        return this;
    }

    public LintCmd withSubcharts(boolean withSubcharts) {
        this.withSubcharts = withSubcharts;
        return this;
    }

    @Override
    public LintCmd burstLimit(Integer burstLimit) {
        return (LintCmd) super.burstLimit(burstLimit);
    }

    @Override
    public LintCmd debug() {
        return (LintCmd) super.debug();
    }

    @Override
    public LintCmd help() {
        return (LintCmd) super.help();

    }

    @Override
    public LintCmd kubeAsGroup(String... kubeAsGroup) {
        return (LintCmd) super.kubeAsGroup(kubeAsGroup);
    }

    @Override
    public LintCmd kubeCAFile(String kubeCAFile) {
        return (LintCmd) super.kubeCAFile(kubeCAFile);
    }

    @Override
    public LintCmd kubeContext(String kubeContext) {
        return (LintCmd) super.kubeContext(kubeContext);
    }

    @Override
    public LintCmd kubeconfig(String kubeconfig) {
        super.kubeconfig(kubeconfig);
        return this;
    }

    @Override
    public LintCmd kubeAPIServer(String kubeAPIServer) {
        super.kubeAPIServer(kubeAPIServer);
        return this;
    }

    @Override
    public LintCmd kubeAsUser(String kubeAsUser) {
        return ((LintCmd) super.kubeAsUser(kubeAsUser));
    }


    @Override
    public LintCmd namespace(String namespace) {
        super.namespace(namespace);
        return this;
    }

    @Override
    public LintCmd kubeToken(String kubeToken) {
        super.kubeToken(kubeToken);
        return this;
    }

    @Override
    public LintCmd kubeTLSServerName(String kubeTLSServerName) {
        super.kubeTLSServerName(kubeTLSServerName);
        return this;
    }

    @Override
    public LintCmd kubeInsecureSkipTLSVerify() {
        super.kubeInsecureSkipTLSVerify();
        return this;
    }

    @Override
    public LintCmd registryConfig(String registryConfig) {
        super.registryConfig(registryConfig);
        return this;
    }

    @Override
    public LintCmd repositoryConfig(String repositoryConfig) {
        super.repositoryConfig(repositoryConfig);
        return this;
    }

    @Override
    public LintCmd repositoryCache(String repositoryCache) {
        super.repositoryCache(repositoryCache);
        return this;
    }

    public LintCmd buildArgs() {
        this.args.add(COMMAND_NAME);
        if (path != null) {
            this.args.add(path);
        }
        if (quiet) {
            this.args.add("--quiet");
        }
        if (set != null) {
            this.args.add("--set");
            this.args.add(set);
        }
        if (setFile != null) {
            this.args.add("--set-file");
            this.args.add(setFile);
        }
        if (setJson != null) {
            this.args.add("--set-json");
            this.args.add(setJson);
        }
        if (setString != null) {
            this.args.add("--set-string");
            this.args.add(setString);
        }
        if (strict) {
            this.args.add("--strict");
        }
        if (values != null) {
            for (String value : values) {
                this.args.add("-f");
                this.args.add(value);
            }
        }
        if (withSubcharts) {
            this.args.add("--with-subcharts");
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
