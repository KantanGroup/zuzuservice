package com.zuzuapps.task.app.master.models;

import javax.persistence.*;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "master_application_screenshot_s",
        indexes = {
                @Index(name = "app_id_index", columnList = "app_id")
        }
)
public class AppScreenshotMaster {
    @Id
    @Column(name = "app_id", length = 128)
    private String appId;
    @Column(columnDefinition = "TEXT")
    private String data;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
