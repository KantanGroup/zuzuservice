package com.zuzuapps.task.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author tuanta17
 */
@JsonIgnoreProperties({"responseHeader", "_version_"})
public class AppInformationObject {
    private AppInformationResponse response;

    public AppInformationResponse getResponse() {
        return response;
    }

    public void setResponse(AppInformationResponse response) {
        this.response = response;
    }
}
