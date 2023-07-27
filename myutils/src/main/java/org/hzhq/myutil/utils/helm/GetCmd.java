package org.hzhq.myutil.utils.helm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-09 20:22 <br/>
 *        helm get <br/>
 *        --help helm get all --help <br/>
 *        helm get hooks --help <br/>
 *        helm get manifest --help <br/>
 *        helm get notes --help <br/>
 *        helm get values --help <br/>
 */
public class GetCmd extends RootCmd {

    protected static final String COMMAND_GET = "get";

    protected String releaseName;

    public All allCmd() {
        return new All();
    }

    public Hooks hooksCmd() {
        return new Hooks();
    }

    public Manifest manifestCmd() {
        return new Manifest();
    }

    public Notes notesCmd() {
        return new Notes();
    }

    public Values valuesCmd() {
        return new Values();
    }

    public GetCmd() {
        super();
        this.cmds.add(COMMAND_GET);
    }

    protected Map<String, String> execToYamlMap() {
        Map<String, String> yamlMap = new HashMap<>();
        String regex =
            "(.*\\n)*USER-SUPPLIED VALUES:\\n(?<userSuppliedValues>(.*\\n)*)COMPUTED VALUES:\\n(?<computedValues>(.*\\n)*)HOOKS:\\n(?<hooks>(.*\\n)*)MANIFEST:\\n(?<manifest>(.*\\n)*)NOTES:\\n(?<notes>(.*\\n)*)";
        try {
            String output = this.exec();
            if (this instanceof All) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(output);
                if (matcher.matches()) {
                    String userSuppliedValues = matcher.group("userSuppliedValues");
                    yamlMap.put("values.yaml", userSuppliedValues);
                    String computedValues = matcher.group("computedValues");
                    yamlMap.compute("values.yaml",
                        (k, v) -> v == null || "".equals(v.strip()) || "null".equals(v.strip()) ? computedValues : v);
                    String hooks = matcher.group("hooks");
                    this.setSourceYamlContent(hooks, yamlMap);
                    String manifest = matcher.group("manifest");
                    this.setSourceYamlContent(manifest, yamlMap);
                    String notes = matcher.group("notes");
                    yamlMap.put("templates/NOTES.txt", notes);
                }
            } else if (this instanceof Values) {
                String content = output.replace("USER-SUPPLIED VALUES:", "");
                yamlMap.put("values.yaml", content.strip());
            } else if (this instanceof Notes) {
                String content = output.replace("NOTES:", "");
                yamlMap.put("templates/NOTES.txt", content.strip());
            } else {
                this.setSourceYamlContent(output, yamlMap);
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE,"failed exec helm get cmd content to yaml map", e);
        }
        return yamlMap;
    }

    public static class All extends GetCmd implements GlobalFlags<All> {

        private static final String COMMAND_NAME = "all";

        private int revision;

        private String template;

        public All releaseName(String releaseName) {
            this.releaseName = releaseName;
            return this;
        }

        public All revision(int revision) {
            this.revision = revision;
            return this;
        }

        public All template(String template) {
            this.template = template;
            return this;
        }

        @Override
        public All help() {
            this.help = true;
            return this;
        }

        @Override
        public All buildArgs() {
            this.args = new ArrayList<>();
            if (releaseName != null) {
                this.args.add(releaseName);
            }
            if (revision > 0) {
                this.args.add("--revision");
                this.args.add(Integer.toString(revision));
            }
            if (template != null) {
                this.args.add("--template");
                this.args.add(template);
            }
            super.buildArgs();

            return this;
        }

        public All() {
            super();
            this.cmds.add(COMMAND_NAME);
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
        public Map<String, String> execToYamlMap() {
            return super.execToYamlMap();
        }

        @Override
        public All burstLimit(Integer burstLimit) {
            return (All)super.burstLimit(burstLimit);
        }

        @Override
        public All debug() {
            return (All)super.debug();
        }

        @Override
        public All namespace(String namespace) {
            return (All)super.namespace(namespace);
        }

        @Override
        public All kubeToken(String kubeToken) {
            return (All)super.kubeToken(kubeToken);
        }

        @Override
        public All kubeconfig(String kubeconfig) {
            return (All)super.kubeconfig(kubeconfig);
        }

        @Override
        public All kubeAPIServer(String kubeAPIServer) {
            return (All)super.kubeAPIServer(kubeAPIServer);
        }

        @Override
        public All kubeAsUser(String kubeAsUser) {
            return (All)super.kubeAsUser(kubeAsUser);
        }

        @Override
        public All kubeAsGroup(String... kubeAsGroup) {
            return (All)super.kubeAsGroup(kubeAsGroup);
        }

        @Override
        public All kubeCAFile(String kubeCAFile) {
            return (All)super.kubeCAFile(kubeCAFile);
        }

        @Override
        public All kubeContext(String kubeContext) {
            return (All)super.kubeContext(kubeContext);
        }

        @Override
        public All kubeInsecureSkipTLSVerify() {
            return (All)super.kubeInsecureSkipTLSVerify();
        }

        @Override
        public All kubeTLSServerName(String kubeTLSServerName) {
            return (All)super.kubeTLSServerName(kubeTLSServerName);
        }

        @Override
        public All registryConfig(String registryConfig) {
            return (All)super.registryConfig(registryConfig);
        }

        @Override
        public All repositoryConfig(String repositoryConfig) {
            return (All)super.repositoryConfig(repositoryConfig);
        }

        @Override
        public All repositoryCache(String repositoryCache) {
            return (All)super.repositoryCache(repositoryCache);
        }
    }

    public static class Hooks extends GetCmd implements GlobalFlags<Hooks> {

        private static final String COMMAND_NAME = "hooks";

        private int revision;

        public Hooks releaseName(String releaseName) {
            this.releaseName = releaseName;
            return this;
        }

        public Hooks revision(int revision) {
            this.revision = revision;
            return this;
        }

        @Override
        public Hooks help() {
            this.help = true;
            return this;
        }

        @Override
        public Hooks burstLimit(Integer burstLimit) {
            return (Hooks)super.burstLimit(burstLimit);
        }

        @Override
        public Hooks debug() {
            return (Hooks)super.debug();
        }

        @Override
        public Hooks namespace(String namespace) {
            return (Hooks)super.namespace(namespace);
        }

        @Override
        public Hooks kubeToken(String kubeToken) {
            return (Hooks)super.kubeToken(kubeToken);
        }

        @Override
        public Hooks kubeconfig(String kubeconfig) {
            return (Hooks)super.kubeconfig(kubeconfig);
        }

        @Override
        public Hooks kubeAPIServer(String kubeAPIServer) {
            return (Hooks)super.kubeAPIServer(kubeAPIServer);
        }

        @Override
        public Hooks kubeAsUser(String kubeAsUser) {
            return (Hooks)super.kubeAsUser(kubeAsUser);
        }

        @Override
        public Hooks kubeAsGroup(String... kubeAsGroup) {
            return (Hooks)super.kubeAsGroup(kubeAsGroup);
        }

        @Override
        public Hooks kubeCAFile(String kubeCAFile) {
            return (Hooks)super.kubeCAFile(kubeCAFile);
        }

        @Override
        public Hooks kubeContext(String kubeContext) {
            return (Hooks)super.kubeContext(kubeContext);
        }

        @Override
        public Hooks kubeInsecureSkipTLSVerify() {
            return (Hooks)super.kubeInsecureSkipTLSVerify();
        }

        @Override
        public Hooks kubeTLSServerName(String kubeTLSServerName) {
            return (Hooks)super.kubeTLSServerName(kubeTLSServerName);
        }

        @Override
        public Hooks registryConfig(String registryConfig) {
            return (Hooks)super.registryConfig(registryConfig);
        }

        @Override
        public Hooks repositoryConfig(String repositoryConfig) {
            return (Hooks)super.repositoryConfig(repositoryConfig);
        }

        @Override
        public Hooks repositoryCache(String repositoryCache) {
            return (Hooks)super.repositoryCache(repositoryCache);
        }

        public Hooks() {
            super();
            this.cmds.add(COMMAND_NAME);
        }

        @Override
        public Hooks buildArgs() {
            this.args = new ArrayList<>();
            if (releaseName != null) {
                this.args.add(releaseName);
            }
            if (revision > 0) {
                this.args.add("--revision");
                this.args.add(Integer.toString(revision));
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
        public Map<String, String> execToYamlMap() {
            return super.execToYamlMap();
        }
    }

    public static class Manifest extends GetCmd {

        private static final String COMMAND_NAME = "manifest";

        private int revision;

        public Manifest releaseName(String releaseName) {
            this.releaseName = releaseName;
            return this;
        }

        public Manifest revision(int revision) {
            this.revision = revision;
            return this;
        }

        @Override
        public Manifest help() {
            this.help = true;
            return this;
        }

        public Manifest() {
            super();
            this.cmds.add(COMMAND_NAME);
        }

        @Override
        public Manifest buildArgs() {
            this.args = new ArrayList<>();
            if (releaseName != null) {
                this.args.add(releaseName);
            }
            if (revision > 0) {
                this.args.add("--revision");
                this.args.add(Integer.toString(revision));
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
        public Map<String, String> execToYamlMap() {
            return super.execToYamlMap();
        }

        @Override
        public Manifest burstLimit(Integer burstLimit) {
            return (Manifest)super.burstLimit(burstLimit);
        }

        @Override
        public Manifest debug() {
            return (Manifest)super.debug();
        }

        @Override
        public Manifest namespace(String namespace) {
            return (Manifest)super.namespace(namespace);
        }

        @Override
        public Manifest kubeToken(String kubeToken) {
            return (Manifest)super.kubeToken(kubeToken);
        }

        @Override
        public Manifest kubeconfig(String kubeconfig) {
            return (Manifest)super.kubeconfig(kubeconfig);
        }

        @Override
        public Manifest kubeAPIServer(String kubeAPIServer) {
            return (Manifest)super.kubeAPIServer(kubeAPIServer);
        }

        @Override
        public Manifest kubeAsUser(String kubeAsUser) {
            return (Manifest)super.kubeAsUser(kubeAsUser);
        }

        @Override
        public Manifest kubeAsGroup(String... kubeAsGroup) {
            return (Manifest)super.kubeAsGroup(kubeAsGroup);
        }

        @Override
        public Manifest kubeCAFile(String kubeCAFile) {
            return (Manifest)super.kubeCAFile(kubeCAFile);
        }

        @Override
        public Manifest kubeContext(String kubeContext) {
            return (Manifest)super.kubeContext(kubeContext);
        }

        @Override
        public Manifest kubeInsecureSkipTLSVerify() {
            return (Manifest)super.kubeInsecureSkipTLSVerify();
        }

        @Override
        public Manifest kubeTLSServerName(String kubeTLSServerName) {
            return (Manifest)super.kubeTLSServerName(kubeTLSServerName);
        }

        @Override
        public Manifest registryConfig(String registryConfig) {
            return (Manifest)super.registryConfig(registryConfig);
        }

        @Override
        public Manifest repositoryConfig(String repositoryConfig) {
            return (Manifest)super.repositoryConfig(repositoryConfig);
        }

        @Override
        public Manifest repositoryCache(String repositoryCache) {
            return (Manifest)super.repositoryCache(repositoryCache);
        }
    }

    public static class Notes extends GetCmd implements GlobalFlags<Notes> {

        private static final String COMMAND_NAME = "notes";

        private int revision;

        public Notes releaseName(String releaseName) {
            this.releaseName = releaseName;
            return this;
        }

        public Notes revision(int revision) {
            this.revision = revision;
            return this;
        }

        @Override
        public Notes help() {
            this.help = true;
            return this;
        }

        public Notes() {
            super();
            this.cmds.add(COMMAND_NAME);
        }

        @Override
        public Notes buildArgs() {
            this.args = new ArrayList<>();
            if (releaseName != null) {
                this.args.add(releaseName);
            }
            if (revision > 0) {
                this.args.add("--revision");
                this.args.add(Integer.toString(revision));
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
        public Notes burstLimit(Integer burstLimit) {
            return (Notes)super.burstLimit(burstLimit);
        }

        @Override
        public Notes debug() {
            return (Notes)super.debug();
        }

        @Override
        public Notes namespace(String namespace) {
            return (Notes)super.namespace(namespace);
        }

        @Override
        public Notes kubeToken(String kubeToken) {
            return (Notes)super.kubeToken(kubeToken);
        }

        @Override
        public Notes kubeconfig(String kubeconfig) {
            return (Notes)super.kubeconfig(kubeconfig);
        }

        @Override
        public Notes kubeAPIServer(String kubeAPIServer) {
            return (Notes)super.kubeAPIServer(kubeAPIServer);
        }

        @Override
        public Notes kubeAsUser(String kubeAsUser) {
            return (Notes)super.kubeAsUser(kubeAsUser);
        }

        @Override
        public Notes kubeAsGroup(String... kubeAsGroup) {
            return (Notes)super.kubeAsGroup(kubeAsGroup);
        }

        @Override
        public Notes kubeCAFile(String kubeCAFile) {
            return (Notes)super.kubeCAFile(kubeCAFile);
        }

        @Override
        public Notes kubeContext(String kubeContext) {
            return (Notes)super.kubeContext(kubeContext);
        }

        @Override
        public Notes kubeInsecureSkipTLSVerify() {
            return (Notes)super.kubeInsecureSkipTLSVerify();
        }

        @Override
        public Notes kubeTLSServerName(String kubeTLSServerName) {
            return (Notes)super.kubeTLSServerName(kubeTLSServerName);
        }

        @Override
        public Notes registryConfig(String registryConfig) {
            return (Notes)super.registryConfig(registryConfig);
        }

        @Override
        public Notes repositoryConfig(String repositoryConfig) {
            return (Notes)super.repositoryConfig(repositoryConfig);
        }

        @Override
        public Notes repositoryCache(String repositoryCache) {
            return (Notes)super.repositoryCache(repositoryCache);
        }
    }

    public static class Values extends GetCmd implements GlobalFlags<Values> {
        private static final String COMMAND_NAME = "values";

        private boolean all;

        private int revision;

        private String output;

        public Values all() {
            this.all = true;
            return this;
        }

        public Values releaseName(String releaseName) {
            this.releaseName = releaseName;
            return this;
        }

        public Values output(String output) {
            this.output = output;
            return this;
        }

        public Values output(OutputType type) {
            this.output = type.name();
            return this;
        }

        public Values revision(int revision) {
            this.revision = revision;
            return this;
        }

        @Override
        public Values help() {
            this.help = true;
            return this;
        }

        public Values() {
            super();
            this.cmds.add(COMMAND_NAME);
        }

        @Override
        protected Values buildArgs() {
            this.args = new ArrayList<>();
            if (releaseName != null) {
                this.args.add(releaseName);
            }
            if (all) {
                this.args.add("--all");
            }
            if (revision > 0) {
                this.args.add("--revision");
                this.args.add(Integer.toString(revision));
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

        @Override
        public Values namespace(String namespace) {
            return (Values)super.namespace(namespace);
        }

        @Override
        public Values kubeToken(String kubeToken) {
            return (Values)super.kubeToken(kubeToken);
        }

        @Override
        public Values kubeconfig(String kubeconfig) {
            return (Values)super.kubeconfig(kubeconfig);
        }

        @Override
        public Values burstLimit(Integer burstLimit) {
            return (Values)super.burstLimit(burstLimit);
        }

        @Override
        public Values debug() {
            return (Values)super.debug();
        }

        @Override
        public Values kubeAPIServer(String kubeAPIServer) {
            return (Values)super.kubeAPIServer(kubeAPIServer);
        }

        @Override
        public Values kubeAsUser(String kubeAsUser) {
            return (Values)super.kubeAsUser(kubeAsUser);
        }

        @Override
        public Values kubeAsGroup(String... kubeAsGroup) {
            return (Values)super.kubeAsGroup(kubeAsGroup);
        }

        @Override
        public Values kubeCAFile(String kubeCAFile) {
            return (Values)super.kubeCAFile(kubeCAFile);
        }

        @Override
        public Values kubeContext(String kubeContext) {
            return (Values)super.kubeContext(kubeContext);
        }

        @Override
        public Values kubeInsecureSkipTLSVerify() {
            return (Values)super.kubeInsecureSkipTLSVerify();
        }

        @Override
        public Values kubeTLSServerName(String kubeTLSServerName) {
            return (Values)super.kubeTLSServerName(kubeTLSServerName);
        }

        @Override
        public Values registryConfig(String registryConfig) {
            return (Values)super.registryConfig(registryConfig);
        }

        @Override
        public Values repositoryConfig(String repositoryConfig) {
            return (Values)super.repositoryConfig(repositoryConfig);
        }

        @Override
        public Values repositoryCache(String repositoryCache) {
            return (Values)super.repositoryCache(repositoryCache);
        }

    }

}
