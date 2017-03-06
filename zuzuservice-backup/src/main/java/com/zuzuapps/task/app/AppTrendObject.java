package com.zuzuapps.task.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author tuanta17
 */
@JsonIgnoreProperties({"responseHeader", "_version_", "icon", "title", "developer_id"})
public class AppTrendObject {
    private AppTrendResponse response;

    public AppTrendResponse getResponse() {
        return response;
    }

    public void setResponse(AppTrendResponse response) {
        this.response = response;
    }
}
