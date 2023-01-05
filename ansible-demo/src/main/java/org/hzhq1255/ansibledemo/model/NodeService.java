package org.hzhq1255.ansibledemo.model;

import lombok.Data;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-01-04 17:10
 */
@Data
public class NodeService {

    private String nodeName;
    private String serviceName;

    private String statusDetail;
    private StatusEnum status;
    public enum StatusEnum{
        ACTIVE,
        RELOADING,
        INACTIVE,
        FAILED,
        ACTIVATING,
        DEACTIVATING,
    }

}
