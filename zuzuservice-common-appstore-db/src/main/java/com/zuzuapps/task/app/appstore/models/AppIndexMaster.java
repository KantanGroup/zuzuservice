package com.zuzuapps.task.app.appstore.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zuzuapps.task.app.googlestore.common.GoogleCategoryEnum;
import com.zuzuapps.task.app.googlestore.common.GoogleCollectionEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "master_application_index_s",
        indexes = {
                @Index(name = "app_id_index", columnList = "app_id,country_code,category,collection"),
                @Index(name = "create_at_index", columnList = "create_at"),
                @Index(name = "app_index_index", columnList = "app_index")
        }
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppIndexMaster {
    @Id
    private String id;

    @Column(name = "app_id", length = 128, nullable = false)
    private String appId;
    @Column(name = "country_code", length = 2, nullable = false)
    private String countryCode;
    @Column(name = "category", length = 1, nullable = false)
    private GoogleCategoryEnum category;
    @Column(name = "collection", length = 1, nullable = false)
    private GoogleCollectionEnum collection;
    @Column(name = "app_index")
    private short index;
    private String icon;
    @Column(name = "developerId")
    private String developerId;
    @Column(name = "score")
    private float score;
    @Column(name = "free")
    private boolean free;
    @Column(name = "price", length = 64)
    private String price;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public GoogleCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(GoogleCategoryEnum category) {
        this.category = category;
    }

    public GoogleCollectionEnum getCollection() {
        return collection;
    }

    public void setCollection(GoogleCollectionEnum collection) {
        this.collection = collection;
    }

    public short getIndex() {
        return index;
    }

    public void setIndex(short index) {
        this.index = index;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
