package org.hzhq.myutil.utils.helm.response;

import lombok.Data;

/**
 * @author guokang
 * @date 2022/11/15 20:25
 */
@Data
public class HelmList {
    private String name;
    private String namespace;
    private String revision;
    private String updated;
    private String status;
    private String chart;
    private String app_version;
}
