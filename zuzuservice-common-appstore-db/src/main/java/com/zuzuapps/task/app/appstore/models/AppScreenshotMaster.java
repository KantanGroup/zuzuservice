package com.zuzuapps.task.app.appstore.models;

import javax.persistence.*;
import java.util.Date;

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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createAt;

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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
