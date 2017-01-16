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
                @Index(name = "app_id_index", columnList = "app_id"),
                @Index(name = "create_at_index", columnList = "create_at"),
                @Index(name = "update_at_index", columnList = "update_at"),
                @Index(name = "country_index", columnList = "country_code"),
                @Index(name = "category_index", columnList = "category"),
                @Index(name = "collection_index", columnList = "collection"),
                @Index(name = "app_index_index", columnList = "app_index"),
                @Index(name = "visible_index", columnList = "visible_index")
        }
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppIndexMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "app_id")
    private String appId;
    @Column(name = "country_code")
    private String countryCode;
    private CategoryEnum category;
    private CollectionEnum collection;
    @Column(name = "app_index")
    private int index;
    private String icon;
    private boolean visible; //

    @Column(name = "create_at")
    private Date createAt;
    @Column(name = "update_at")
    private Date updateAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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
}
