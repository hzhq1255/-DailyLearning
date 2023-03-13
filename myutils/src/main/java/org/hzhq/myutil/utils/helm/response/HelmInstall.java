package org.hzhq.myutil.utils.helm.helm.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-08 14:31
 */
@NoArgsConstructor
@Data
public class HelmInstall {


    @JsonProperty("name")
    private String name;
    @JsonProperty("info")
    private Info info;
    @JsonProperty("chart")
    private Chart chart;
    @JsonProperty("manifest")
    private String manifest;
    @JsonProperty("hooks")
    private List<Hooks> hooks;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty("namespace")
    private String namespace;

    @NoArgsConstructor
    @Data
    public static class Info {
        @JsonProperty("first_deployed")
        private String firstDeployed;
        @JsonProperty("last_deployed")
        private String lastDeployed;
        @JsonProperty("deleted")
        private String deleted;
        @JsonProperty("description")
        private String description;
        @JsonProperty("status")
        private String status;
        @JsonProperty("notes")
        private String notes;
    }

    @NoArgsConstructor
    @Data
    public static class Chart {
        @JsonProperty("metadata")
        private Metadata metadata;
        @JsonProperty("lock")
        private Object lock;
        @JsonProperty("templates")
        private List<Templates> templates;
        @JsonProperty("values")
        private Map<String, Object> values;
        @JsonProperty("schema")
        private Object schema;
        @JsonProperty("files")
        private List<Files> files;
        @JsonProperty("manifest")
        private String manifest;

        @NoArgsConstructor
        @Data
        public static class Metadata {
            @JsonProperty("name")
            private String name;
            @JsonProperty("version")
            private String version;
            @JsonProperty("description")
            private String description;
            @JsonProperty("apiVersion")
            private String apiVersion;
            @JsonProperty("appVersion")
            private String appVersion;
            @JsonProperty("type")
            private String type;
        }



        @NoArgsConstructor
        @Data
        public static class Templates {
            @JsonProperty("name")
            private String name;
            @JsonProperty("data")
            private String data;
        }

        @NoArgsConstructor
        @Data
        public static class Files {
            @JsonProperty("name")
            private String name;
            @JsonProperty("data")
            private String data;
        }
    }

    @NoArgsConstructor
    @Data
    public static class Hooks {
        @JsonProperty("name")
        private String name;
        @JsonProperty("kind")
        private String kind;
        @JsonProperty("path")
        private String path;
        @JsonProperty("manifest")
        private String manifest;
        @JsonProperty("events")
        private List<String> events;
        @JsonProperty("last_run")
        private LastRun lastRun;

        @NoArgsConstructor
        @Data
        public static class LastRun {
            @JsonProperty("started_at")
            private String startedAt;
            @JsonProperty("completed_at")
            private String completedAt;
            @JsonProperty("phase")
            private String phase;
        }
    }
}
