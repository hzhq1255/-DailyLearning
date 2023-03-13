package org.hzhq.myutil.utils.helm.helm.response;

import lombok.Data;

/**
 * helm发布历史：helm history RELEASE_NAME [flags]
 * @author yanzhen
 * @since 2023-03-03
 */
@Data
public class HelmHistory {
    private String revision;
    private String updated;
    private String status;
    private String chart;
    private String app_version;
    private String description;
}
