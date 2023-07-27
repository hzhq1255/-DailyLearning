package org.hzhq.myutil.utils.helm;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-09 15:35
 */
public class HelmCmdBuilder {

    /**
     * usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     */
    public static CreateCmd createCmd() {
        return new CreateCmd();
    }

    /**
     * usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     */
    public static HistoryCmd historyCmd() {
        return new HistoryCmd();
    }

    /**
     * usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     */
    public static InstallCmd installCmd() {
        return new InstallCmd();
    }

    /**
     * usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     */
    public static LintCmd lintCmd() {
        return new LintCmd();
    }

    /**
     * usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     *
     */
    public static ListCmd listCmd() {
        return new ListCmd();
    }

    /**
     * usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     *
     */
    public static GetCmd getCmd() {
        return new GetCmd();
    }

    /**
     * usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     *
     */
    public static PackageCmd packageCmd() {
        return new PackageCmd();
    }

    /**
     * usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     *
     * public static RollbackCmd rollbackCmd(){ return new RollbackCmd(); }
     *
     * /** usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     */
    public static TemplateCmd templateCmd() {
        return new TemplateCmd();
    }

    /**
     * usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     *
     */
    public static UninstallCmd uninstallCmd() {
        return new UninstallCmd();
    }

    /**
     * usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     */
    public static UpgradeCmd upgradeCmd() {
        return new UpgradeCmd();
    }

    /**
     * usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     */
    public static RollbackCmd rollbackCmd() {
        return new RollbackCmd();
    }

    /**
     * usages 1. get command xxxCmd().arg1("arg1").arg2().buildCmd(); 2. execute command return string
     * xxxCmd().arg1("arg1").arg2().exec();
     */
    public static VerifyCmd verifyCmd() {
        return new VerifyCmd();
    }

    public static StatusCmd statusCmd() {
        return new StatusCmd();
    }

}
