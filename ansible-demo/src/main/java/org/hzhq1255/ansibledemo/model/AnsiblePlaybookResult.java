package org.hzhq1255.ansibledemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-01-05 10:54
 */
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnsiblePlaybookResult {


    @JsonProperty("custom_stats")
    private CustomStatsDTO customStats;
    @JsonProperty("global_custom_stats")
    private GlobalCustomStatsDTO globalCustomStats;
    @JsonProperty("plays")
    private List<PlaysDTO> plays;
    @JsonProperty("stats")
    private Map<String, NodeStatsDTO> stats;

    @NoArgsConstructor
    @Data
    public static class CustomStatsDTO {
    }

    @NoArgsConstructor
    @Data
    public static class GlobalCustomStatsDTO {
    }

    @NoArgsConstructor
    @Data
    public static class NodeStatsDTO {
        @JsonProperty("changed")
        private Integer changed;
        @JsonProperty("failures")
        private Integer failures;
        @JsonProperty("ignored")
        private Integer ignored;
        @JsonProperty("ok")
        private Integer ok;
        @JsonProperty("rescued")
        private Integer rescued;
        @JsonProperty("skipped")
        private Integer skipped;
        @JsonProperty("unreachable")
        private Integer unreachable;
    }

    @NoArgsConstructor
    @Data
    public static class PlaysDTO {
        @JsonProperty("play")
        private PlayDTO play;
        @JsonProperty("tasks")
        private List<TasksDTO> tasks;

        @NoArgsConstructor
        @Data
        public static class PlayDTO {
            @JsonProperty("duration")
            private DurationDTO duration;
            @JsonProperty("id")
            private String id;
            @JsonProperty("name")
            private String name;

            @NoArgsConstructor
            @Data
            public static class DurationDTO {
                @JsonProperty("end")
                private String end;
                @JsonProperty("start")
                private String start;
            }
        }

        @NoArgsConstructor
        @Data
        public static class TasksDTO {
            @JsonProperty("hosts")
            private Map<String, NodeHostDTO> hosts;
            @JsonProperty("task")
            private TaskDTO task;
            @NoArgsConstructor
            @Data
            public static class NodeHostDTO {
                @JsonProperty("_ansible_no_log")
                private Object ansibleNoLog;
                @JsonProperty("action")
                private String action;
                @JsonProperty("ansible_facts")
                private AnsibleFactsDTO ansibleFacts;
                @JsonProperty("changed")
                private Boolean changed;
                @JsonProperty("cmd")
                private String cmd;
                @JsonProperty("delta")
                private String delta;
                @JsonProperty("end")
                private String end;
                @JsonProperty("invocation")
                private InvocationDTO invocation;
                @JsonProperty("msg")
                private String msg;
                @JsonProperty("rc")
                private Integer rc;
                @JsonProperty("start")
                private String start;
                @JsonProperty("stderr")
                private String stderr;
                @JsonProperty("stderr_lines")
                private List<String> stderrLines;
                @JsonProperty("stdout")
                private String stdout;
                @JsonProperty("stdout_lines")
                private List<String> stdoutLines;
                @JsonProperty("_ansible_verbose_always")
                private Boolean ansibleVerboseAlways;

                @NoArgsConstructor
                @Data
                public static class AnsibleFactsDTO {
                    @JsonProperty("discovered_interpreter_python")
                    private String discoveredInterpreterPython;
                }

                @NoArgsConstructor
                @Data
                public static class InvocationDTO {
                    @JsonProperty("module_args")
                    private ModuleArgsDTO moduleArgs;

                    @NoArgsConstructor
                    @Data
                    public static class ModuleArgsDTO {
                        @JsonProperty("_raw_params")
                        private String rawParams;
                        @JsonProperty("_uses_shell")
                        private Boolean usesShell;
                        @JsonProperty("argv")
                        private Object argv;
                        @JsonProperty("chdir")
                        private Object chdir;
                        @JsonProperty("creates")
                        private Object creates;
                        @JsonProperty("executable")
                        private Object executable;
                        @JsonProperty("removes")
                        private Object removes;
                        @JsonProperty("stdin")
                        private Object stdin;
                        @JsonProperty("stdin_add_newline")
                        private Boolean stdinAddNewline;
                        @JsonProperty("strip_empty_ends")
                        private Boolean stripEmptyEnds;
                        @JsonProperty("warn")
                        private Boolean warn;
                    }
                }
            }

            @NoArgsConstructor
            @Data
            public static class TaskDTO {
                @JsonProperty("duration")
                private DurationDTO duration;
                @JsonProperty("id")
                private String id;
                @JsonProperty("name")
                private String name;

                @NoArgsConstructor
                @Data
                public static class DurationDTO {
                    @JsonProperty("end")
                    private String end;
                    @JsonProperty("start")
                    private String start;
                }
            }
        }
    }
}
