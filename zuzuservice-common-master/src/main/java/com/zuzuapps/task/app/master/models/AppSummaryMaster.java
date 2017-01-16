package com.zuzuapps.task.app.master.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "master_summary_s",
        indexes = {
                @Index(name = "create_at_index", columnList = "create_at"),
                @Index(name = "update_at_index", columnList = "update_at"),
                @Index(name = "score_index", columnList = "score"),
                @Index(name = "price_index", columnList = "price"),
                @Index(name = "free_index", columnList = "free")
        }
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppSummaryMaster {
    @Id
    @Column(name = "app_id")
    private String appId;
    private String url;
    private String icon;
    private int score;
    private String price;
    private boolean free;

    @Column(name = "create_at")
    private Date createAt;
    @Column(name = "update_at")
    private Date updateAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "developerId", nullable = false, insertable = false, updatable = false)
    private AppDeveloperMaster developer;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public AppDeveloperMaster getDeveloper() {
        return developer;
    }

    public void setDeveloper(AppDeveloperMaster developer) {
        this.developer = developer;
    }
}
