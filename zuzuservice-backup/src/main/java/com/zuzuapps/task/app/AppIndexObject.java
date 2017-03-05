package com.zuzuapps.task.app;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author tuanta17
 */
@JsonIgnoreProperties({"responseHeader", "_version_"})
public class AppIndexObject {
    private AppIndexResponse response;

    public AppIndexResponse getResponse() {
        return response;
    }

    public void setResponse(AppIndexResponse response) {
        this.response = response;
    }
}
