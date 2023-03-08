package org.hzhq.myutil.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-07 19:34
 * helm cli version v3.10.1
 */
public class HelmUtils {

    private LintCmd lint;

    private ListCmd list;

    private static class LintCmd {

    }

    public static class LintBuilder {


    }

    private static class ListCmd {
        private String all;
        private String allNamespaces;
        private String date;
        private String deployed;
        private String failed;
        private String filter;
        private String max;
        private String noHeaders;
        private String offset;
        private String output;
        private String pending;
        private String reverse;
        private String selector;
        private String qshort;
        private String superseded;
        private String timeFormat;
        private String uninstalled;
        private String uninstalling;

        private ListCmd(ListBuilder listBuilder) {
            this.all = listBuilder.all;
            this.allNamespaces = listBuilder.allNamespaces;
            this.date = listBuilder.date;
            this.deployed = listBuilder.deployed;
            this.failed = listBuilder.failed;
            this.filter = listBuilder.filter;
            this.max = listBuilder.max;
            this.noHeaders = listBuilder.noHeaders;
            this.offset  = listBuilder.offset;
            this.output = listBuilder.output;
            this.pending = listBuilder.pending;
            this.reverse = listBuilder.reverse;
            this.selector = listBuilder.selector;
            this.timeFormat = listBuilder.timeFormat;
            this.qshort = listBuilder.qshort;
            this.superseded = listBuilder.superseded;
            this.uninstalled = listBuilder.uninstalled;
            this.uninstalling = listBuilder.uninstalling;
        }

        public  String exec(){
            List<String> args = new ArrayList<>();
            run(String.join(" ", args));
            return "";
        }
    }

    private static String run(String args){
        StringBuilder builder = new StringBuilder("helm");
        return "";
    }


    public static ListBuilder listBuilder() {
        return new ListBuilder();
    }


    public static class ListBuilder {
        private String all;
        private String allNamespaces;
        private String date;
        private String deployed;
        private String failed;
        private String filter;
        private String max;
        private String noHeaders;
        private String offset;
        private String output;
        private String pending;
        private String reverse;
        private String selector;
        private String qshort;
        private String superseded;
        private String timeFormat;
        private String uninstalled;
        private String uninstalling;

        public ListBuilder withAll() {
            this.all = "--all";
            return this;
        }

        public ListBuilder withAllNamespaces() {
            this.allNamespaces = "--all-namespaces";
            return this;
        }

        public ListBuilder withDate() {
            this.date = "--date";
            return this;
        }

        public ListBuilder withDeployed() {
            this.deployed = "--deployed";
            return this;
        }

        public ListBuilder withFailed() {
            this.failed = "--failed";
            return this;
        }

        public ListBuilder withFilter(String arg) {
            if (arg != null) {
                this.filter = "--filter=" + arg;
            }
            return this;
        }

        public ListBuilder withMax(Integer arg) {
            if (arg != null) {
                this.filter = "--max=" + arg;
            }
            return this;
        }

        public ListBuilder withNoHeaders() {
            this.noHeaders = "--no-headers";
            return this;
        }

        public ListBuilder withOffset(Integer arg) {
            if (arg != null) {
                this.offset = "--offset=" + arg;
            }
            return this;
        }

        public ListBuilder withOutput(String arg) {
            if (arg != null) {
                this.output = "--output=" + arg;
            }
            return this;
        }

        public ListBuilder withPending(String arg){
            if (arg != null){
                this.pending = "--pending";
            }
            return this;
        }

        public ListBuilder withReverse(){
            this.reverse = "--reverse";
            return this;
        }

        public ListBuilder withSelector(String arg){
            if (arg != null){
                this.selector = "--selector=" + arg;
            }
            return this;
        }

        public ListBuilder withShort(){
            this.qshort = "--short";
            return this;
        }

        public ListBuilder withSuperseded(){
            this.superseded = "--superseded";
            return this;
        }

        public ListBuilder withTimeFormat(String arg){
            if (arg != null){
                this.timeFormat = "--time-format";
            }
            return this;
        }

        public ListBuilder withUninstalled(){
            this.uninstalled = "--uninstalled";
            return this;
        }

        public ListBuilder withUninstalling(){
            this.uninstalled = "--uninstalled";
            return this;
        }

        public ListCmd build(){
            return new ListCmd(this);
        }


    }


    private static class GlobalFlags {

        public String kubeApiServer;

        public String kubeAsUser;

        /**
         * array
         */
        public String kubeAsGroup;

        public String kubeCaFile;

        public String kubeContext;

        public Boolean kubeInsecureSkipTlsVerify;

        public String kubeTlsServerName;

        public String kubeToken;

        public String kubeconfig;

        public String namespace;

        public String registryConfig;

        public String repoistoryCache;

        public String repoistoryConfig;

    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private LintBuilder lint;

        private ListCmd list;

        public Builder withLint(LintBuilder lintBuilder) {
            this.lint = lintBuilder;
            return this;
        }

        public Builder withList(ListBuilder listBuilder) {
            return this;
        }

    }


}
