package com.zuzuapps.task.app.master.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author tuanta17
 */
@Embeddable
public class AppLanguageId implements Serializable {
    @Column(name = "app_id", length = 128, nullable = false)
    private String appId;
    @Column(name = "language_code", length = 2, nullable = false)
    private String languageCode;

    public AppLanguageId() {
    }

    public AppLanguageId(String appId, String languageCode) {
        this.appId = appId;
        this.languageCode = languageCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}