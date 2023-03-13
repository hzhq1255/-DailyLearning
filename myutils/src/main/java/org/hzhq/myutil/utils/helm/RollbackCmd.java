package org.hzhq.myutil.utils.helm.helm;

import java.time.Duration;
import java.util.List;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-09 上午1:59
 * helm rollback --help
 */
public class RollbackCmd extends RootCmd implements GlobalFlags<RollbackCmd>  {
    private static final String COMMAND_NAME = "rollback";
    private String releaseName;
    private int revision;
    private boolean cleanupOnFail;
    private boolean dryRun;
    private boolean force;
    private int historyMax;
    private boolean noHooks;
    private boolean recreatePods;
    private Duration timeout;
    private boolean wait;
    private boolean waitForJobs;

    public RollbackCmd releaseName(String releaseName) {
        this.releaseName = releaseName;
        return this;
    }

    public RollbackCmd revision(int revision) {
        this.revision = revision;
        return this;
    }

    public RollbackCmd cleanupOnFail() {
        this.cleanupOnFail = true;
        return this;
    }

    public RollbackCmd dryRun() {
        this.dryRun = true;
        return this;
    }

    public RollbackCmd force() {
        this.force = true;
        return this;
    }

    public RollbackCmd historyMax(int historyMax) {
        this.historyMax = historyMax;
        return this;
    }

    public RollbackCmd noHooks() {
        this.noHooks = true;
        return this;
    }

    public RollbackCmd recreatePods() {
        this.recreatePods = true;
        return this;
    }

    public RollbackCmd timeout(Duration timeout) {
        this.timeout = timeout;
        return this;
    }

    public RollbackCmd waitArg() {
        this.wait = true;
        return this;
    }

    public RollbackCmd waitForJobs() {
        this.waitForJobs = true;
        return this;
    }

    @Override
    public RollbackCmd burstLimit(Integer burstLimit) {
        return ((RollbackCmd) super.burstLimit(burstLimit));
    }

    @Override
    public RollbackCmd debug() {
        return ((RollbackCmd) super.debug());
    }

    @Override
    public RollbackCmd help() {
        return ((RollbackCmd) super.help());
    }

    @Override
    public RollbackCmd kubeAsGroup(String... kubeAsGroup) {
        return ((RollbackCmd) super.kubeAsGroup(kubeAsGroup));
    }

    @Override
    public RollbackCmd kubeCAFile(String kubeCAFile) {
        return ((RollbackCmd) super.kubeCAFile(kubeCAFile));
    }

    @Override
    public RollbackCmd kubeContext(String kubeContext) {
        return ((RollbackCmd) super.kubeContext(kubeContext));
    }

    @Override
    public RollbackCmd kubeconfig(String kubeconfig) {
        super.kubeconfig(kubeconfig);
        return this;
    }

    @Override
    public RollbackCmd kubeAPIServer(String kubeAPIServer) {
        super.kubeAPIServer(kubeAPIServer);
        return this;
    }

    @Override
    public RollbackCmd namespace(String namespace) {
        super.namespace(namespace);
        return this;
    }

    @Override
    public RollbackCmd kubeToken(String kubeToken) {
        super.kubeToken(kubeToken);
        return this;
    }

    @Override
    public RollbackCmd kubeTLSServerName(String kubeTLSServerName) {
        super.kubeTLSServerName(kubeTLSServerName);
        return this;
    }

    @Override
    public RollbackCmd kubeInsecureSkipTLSVerify() {
        super.kubeInsecureSkipTLSVerify();
        return this;
    }

    @Override
    public RollbackCmd kubeAsUser(String kubeAsUser) {
        return ((RollbackCmd) super.kubeAsUser(kubeAsUser));
    }

    @Override
    public RollbackCmd registryConfig(String registryConfig) {
        super.registryConfig(registryConfig);
        return this;
    }

    @Override
    public RollbackCmd repositoryConfig(String repositoryConfig) {
        super.repositoryConfig(repositoryConfig);
        return this;
    }

    @Override
    public RollbackCmd repositoryCache(String repositoryCache) {
        super.repositoryCache(repositoryCache);
        return this;
    }

    public RollbackCmd buildArgs() {
        this.args.add(COMMAND_NAME);
        if (releaseName != null) {
            this.args.add(releaseName);
        }
        if (revision > 0) {
            this.args.add(Integer.toString(revision));
        }
        if (cleanupOnFail) {
            this.args.add("--cleanup-on-fail");
        }
        if (dryRun) {
            this.args.add("--dry-run");
        }
        if (force) {
            this.args.add("--force");
        }
        if (historyMax > 0) {
            this.args.add("--history-max");
            this.args.add(Integer.toString(historyMax));
        }
        if (noHooks) {
            this.args.add("--no-hooks");
        }
        if (recreatePods) {
            this.args.add("--recreate-pods");
        }
        if (timeout != null) {
            this.args.add("--timeout");
            this.args.add(timeout.toString());
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

