package org.hzhq.myutil.utils;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-07 19:34
 * support
 *  install command
 *
 */
public class HelmCmd {


    private static final String HELM_COMMAND = "helm";


    protected static class RootCommand{

        protected List<String> args = new ArrayList<>(Collections.singleton(HELM_COMMAND));
        private String kubeconfig;

        private String kubeAPIServer;
        private String namespace;

        private String kubeToken;

        private String kubeTLSServerName;

        private boolean kubeInsecureSkipTLSVerify;

        private String registryConfig;

        private String repositoryCache;

        private String repositoryConfig;


        public RootCommand kubeconfig(String kubeconfig){
            this.kubeconfig = kubeconfig;
            return this;
        }

        public RootCommand kubeAPIServer(String kubeAPIServer){
            this.kubeAPIServer = kubeAPIServer;
            return this;
        }

        public RootCommand namespace(String namespace){
            this.namespace = namespace;
            return this;
        }

        public RootCommand kubeToken(String kubeToken){
            this.kubeToken = kubeToken;
            return this;
        }

        public RootCommand kubeTLSServerName(String kubeTLSServerName){
            this.kubeTLSServerName = kubeTLSServerName;
            return this;
        }

        public RootCommand kubeInsecureSkipTLSVerify(Boolean kubeInsecureSkipTLSVerify){
            this.kubeInsecureSkipTLSVerify = kubeInsecureSkipTLSVerify;
            return this;
        }

        public RootCommand registryConfig(String registryConfig){
            this.registryConfig = registryConfig;
            return this;
        }

        public RootCommand repositoryConfig(String repositoryConfig){
            this.repositoryConfig = repositoryConfig;
            return this;
        }

        public RootCommand repositoryCache(String repositoryCache){
            this.repositoryCache = repositoryCache;
            return this;
        }

        private RootCommand buildArgs(){
            if (kubeconfig != null){
                this.args.add("--kubeconfig");
                this.args.add(kubeconfig);
            }
            if (kubeAPIServer != null){
                this.args.add("--kube-apiserver");
                this.args.add(kubeAPIServer);
            }
            if (namespace != null){
                this.args.add("--namespace");
                this.args.add(namespace);
            }
            if (kubeToken != null) {
                this.args.add("--kube-token");
                this.args.add(kubeToken);
            }
            if (kubeTLSServerName != null) {
                this.args.add("--kube-tls-server-name");
                this.args.add(kubeTLSServerName);
            }
            if (kubeInsecureSkipTLSVerify) {
                this.args.add("--kube-insecure-skip-tls-verify");
            }
            if (registryConfig != null) {
                this.args.add("--registry-config");
                this.args.add(registryConfig);
            }
            if (repositoryCache != null){
                this.args.add("--repository-cache");
                this.args.add(repositoryCache);
            }
            if (repositoryConfig != null) {
                this.args.add("--repository-config");
                this.args.add(repositoryConfig);
            }
            return this;
        }

        public String buildCmd(){
            return String.join(" ", this.args);
        }


        public String exec(){
            return  String.join(" ", this.args);
        }

    }




    public static InstallCommand install() {
        return new InstallCommand();
    }


    public static class InstallCommand extends RootCommand{

        private static final String COMMAND_NAME = "install";
        private String releaseName;
        /**
         * chartName
         * location path ./app.tgz or ./app
         */
        private String chartName;
        /**
         * simulate install
         */
        private boolean dryRun;
        /**
         * verify the package before using it
         */
        private boolean verify;
        /**
         * skip tls certificate checks for the chart download
         */
        private boolean insecureSkipTLSVerify;
        /**
         * specify values in a YAML file or a URL (can specify multiple)
         */
        private String values;
        /**
         * key1=val1,key2=val2
         */
        private String set;
        /**
         * key1=path1, key2=path2
         */
        private String setFile;
        /**
         * key1=jsonval1, key2=jsonval2
         */
        private String setJson;
        /**
         * key1=val1,key2=val2
         */
        private String setString;


        public InstallCommand releaseName(String releaseName) {
            this.releaseName = releaseName;
            return this;
        }

        public InstallCommand chartName(String chartName) {
            this.chartName = chartName;
            return this;
        }

        public InstallCommand dryRun(){
            this.dryRun = true;
            return this;
        }

        public InstallCommand verify(){
            this.verify = true;
            return this;
        }

        public InstallCommand insecureSkipTLSVerify(){
            this.insecureSkipTLSVerify = true;
            return this;
        }

        public InstallCommand values(String values){
            this.values = values;
            return this;
        }

        public InstallCommand set(String set){
            this.set = set;
            return this;
        }


        public InstallCommand setFile(String setFile){
            this.setFile = setFile;
            return this;
        }

        public InstallCommand setJson(String setJson){
            this.setJson = setJson;
            return this;
        }

        public InstallCommand setString(String setString){
            this.setString = setString;
            return this;
        }


        private InstallCommand buildArgs() {
            this.args.add(COMMAND_NAME);
            if (releaseName != null) {
                this.args.add(releaseName);
            }
            if (chartName != null) {
                this.args.add(chartName);
            }
            if (dryRun){
                this.args.add("--dry-run");
            }
            if (verify){
                this.args.add("--verify");
            }
            if (insecureSkipTLSVerify){
                this.args.add("--insecure-skip-tls-verify");
            }
            if (values != null){
                this.args.add("--values");
                this.args.add(values);
            }
            if (set != null){
                this.args.add("--set");
                this.args.add(set);
            }
            if (setFile != null) {
                this.args.add("--set-file");
                this.args.add(set);
            }
            if (setJson != null) {
                this.args.add("--set-json");
                this.args.add(setJson);
            }
            if (setString != null) {
                this.args.add("--set-string");
                this.args.add(setString);
            }
            super.buildArgs();
            return this;
        }
        public String buildCmd(){
            this.buildArgs();
            return String.join(" ", this.args);
        }

        public String exec(){
            return super.exec();
        }

    }



}
