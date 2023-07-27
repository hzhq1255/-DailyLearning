package org.hzhq.myutil.utils.helm;

import org.hzhq.myutil.exception.HelmException;

import java.time.Duration;
import java.util.ArrayList;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-09 上午2:02 helm uninstall --help
 */
public class UninstallCmd extends RootCmd implements GlobalFlags<UninstallCmd> {
    private static final String COMMAND_NAME = "uninstall";
    private String releaseName;
    private String description;
    private boolean dryRun;
    private boolean keepHistory;
    private boolean noHooks;
    private Duration timeout;
    private boolean wait;

    public UninstallCmd releaseName(String releaseName) {
        this.releaseName = releaseName;
        return this;
    }

    public UninstallCmd description(String description) {
        this.description = description;
        return this;
    }

    public UninstallCmd dryRun() {
        this.dryRun = true;
        return this;
    }

    public UninstallCmd keepHistory() {
        this.keepHistory = true;
        return this;
    }

    public UninstallCmd noHooks() {
        this.noHooks = true;
        return this;
    }

    public UninstallCmd timeout(Duration timeout) {
        this.timeout = timeout;
        return this;
    }

    public UninstallCmd waitArg() {
        this.wait = true;
        return this;
    }

    @Override
    public UninstallCmd burstLimit(Integer burstLimit) {
        return ((UninstallCmd)super.burstLimit(burstLimit));
    }

    @Override
    public UninstallCmd debug() {
        return ((UninstallCmd)super.debug());
    }

    @Override
    public UninstallCmd help() {
        return ((UninstallCmd)super.help());
    }

    @Override
    public UninstallCmd kubeAsGroup(String... kubeAsGroup) {
        return ((UninstallCmd)super.kubeAsGroup(kubeAsGroup));
    }

    @Override
    public UninstallCmd kubeCAFile(String kubeCAFile) {
        return ((UninstallCmd)super.kubeCAFile(kubeCAFile));
    }

    @Override
    public UninstallCmd kubeContext(String kubeContext) {
        return ((UninstallCmd)super.kubeContext(kubeContext));
    }

    @Override
    public UninstallCmd kubeconfig(String kubeconfig) {
        super.kubeconfig(kubeconfig);
        return this;
    }

    @Override
    public UninstallCmd kubeAPIServer(String kubeAPIServer) {
        super.kubeAPIServer(kubeAPIServer);
        return this;
    }

    @Override
    public UninstallCmd namespace(String namespace) {
        super.namespace(namespace);
        return this;
    }

    @Override
    public UninstallCmd kubeToken(String kubeToken) {
        super.kubeToken(kubeToken);
        return this;
    }

    @Override
    public UninstallCmd kubeTLSServerName(String kubeTLSServerName) {
        super.kubeTLSServerName(kubeTLSServerName);
        return this;
    }

    @Override
    public UninstallCmd kubeInsecureSkipTLSVerify() {
        super.kubeInsecureSkipTLSVerify();
        return this;
    }

    @Override
    public UninstallCmd registryConfig(String registryConfig) {
        super.registryConfig(registryConfig);
        return this;
    }

    @Override
    public UninstallCmd repositoryConfig(String repositoryConfig) {
        super.repositoryConfig(repositoryConfig);
        return this;
    }

    @Override
    public UninstallCmd repositoryCache(String repositoryCache) {
        super.repositoryCache(repositoryCache);
        return this;
    }

    @Override
    public UninstallCmd kubeAsUser(String kubeAsUser) {
        return ((UninstallCmd)super.kubeAsUser(kubeAsUser));
    }

    public UninstallCmd() {
        super();
        this.cmds.add(COMMAND_NAME);
    }

    public UninstallCmd buildArgs() {
        this.args = new ArrayList<>();
        if (releaseName != null) {
            this.args.add(releaseName);
        }
        if (description != null) {
            this.args.add("--description");
            this.args.add(description);
        }
        if (dryRun) {
            this.args.add("--dry-run");
        }
        if (keepHistory) {
            this.args.add("--keep-history");
        }
        if (noHooks) {
            this.args.add("--no-hooks");
        }
        if (timeout != null) {
            this.args.add("--timeout");
            this.args.add(timeout.toString());
        }
        if (wait) {
            this.args.add("--wait");
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

    public String execIgnoreNotFound() {
        try {
            return super.exec();
        } catch (HelmException e) {
            String errMsg = e.getMessage().toLowerCase();
            if (!errMsg.contains("not found")) {
                throw e;
            }
        }
        return "";
    }

}
