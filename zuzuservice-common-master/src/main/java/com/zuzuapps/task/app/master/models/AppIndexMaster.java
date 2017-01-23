package com.zuzuapps.task.app.master.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zuzuapps.task.app.common.CategoryEnum;
import com.zuzuapps.task.app.common.CollectionEnum;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "app_id", length = 50, nullable = false)
    private String appId;
    @Column(name = "country_code", length = 2, nullable = false)
    private String countryCode;
    @Column(name = "category", length = 1, nullable = false)
    private CategoryEnum category;
    @Column(name = "collection", length = 1, nullable = false)
    private CollectionEnum collection;
    @Column(name = "app_index")
    private short index;
    private String icon;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private Date createAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public CollectionEnum getCollection() {
        return collection;
    }

    public void setCollection(CollectionEnum collection) {
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
