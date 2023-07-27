package org.hzhq.myutil.utils.helm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelmStatus {

    @JsonProperty("name")
    private String name;
    @JsonProperty("info")
    private Info info;
    /**
     * JSON Object
     */
    @JsonProperty("config")
    private Map<String, Object> config;
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
    @JsonIgnoreProperties(ignoreUnknown = true)
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
    @JsonIgnoreProperties(ignoreUnknown = true)
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
        @JsonIgnoreProperties(ignoreUnknown = true)
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
