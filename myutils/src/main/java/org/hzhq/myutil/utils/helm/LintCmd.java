package org.hzhq.myutil.utils.helm;

import java.util.ArrayList;
import java.util.Arrays;
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
    private final List<String> set = new ArrayList<>();
    private final List<String> setFile = new ArrayList<>();
    private final List<String> setJSON = new ArrayList<>();
    private final List<String> setString = new ArrayList<>();
    private boolean strict;
    private final List<String> values = new ArrayList<>();
    private boolean withSubcharts;

    public LintCmd path(String path) {
        this.path = path;
        return this;
    }

    public LintCmd quiet() {
        this.quiet = true;
        return this;
    }

    public LintCmd set(String... set) {
        this.set.addAll(Arrays.asList(set));
        return this;
    }

    public LintCmd setFile(String... setFile) {
        this.setFile.addAll(Arrays.asList(setFile));
        return this;
    }

    public LintCmd setJSON(String setJSON) {
        this.setJSON.addAll(Arrays.asList(setJSON));
        return this;
    }

    public LintCmd setString(String... setString) {
        this.setString.addAll(Arrays.asList(setString));
        return this;
    }

    public LintCmd strict() {
        this.strict = true;
        return this;
    }

    public LintCmd values(String... values) {
        this.values.addAll(Arrays.asList(values));
        return this;
    }

    public LintCmd withSubcharts() {
        this.withSubcharts = true;
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

    public LintCmd() {
        super();
        this.cmds.add(COMMAND_NAME);
    }

    public LintCmd buildArgs() {
        this.args = new ArrayList<>();
        if (path != null) {
            this.args.add(path);
        }
        if (quiet) {
            this.args.add("--quiet");
        }
        if (!set.isEmpty()) {
            for (String s : set) {
                this.args.add("--set");
                this.args.add(s);
            }
        }
        if (!setFile.isEmpty()) {
            List<String> setFiles = super.createTempFiles(setFile.toArray(String[]::new));
            for (String value : setFiles) {
                this.args.add("--set-file");
                this.args.add(value);
            }
        }
        if (!setJSON.isEmpty()) {
            List<String> setJSONs = super.createTempFiles(setJSON.toArray(String[]::new));
            for (String value : setJSONs) {
                this.args.add("--set-json");
                this.args.add(value);
            }
        }
        if (!setString.isEmpty()) {
            List<String> setStrings = super.createTempFiles(setString.toArray(String[]::new));
            for (String value : setStrings) {
                this.args.add("--set-string");
                this.args.add(value);
            }
        }
        if (strict) {
            this.args.add("--strict");
        }
        if (!values.isEmpty()) {
            List<String> valueFiles = super.createTempFiles(values.toArray(String[]::new));
            for (String value : valueFiles) {
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

}
