package com.zuzuapps.task.app.elasticsearch.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author tuanta17
 */
@Document(indexName = "app-trend-index", type = "app-trend-index", shards = 1, replicas = 0, refreshInterval = "-1")
public class AppTrendElasticSearch {
    @Id
    private String id;
    @Field(type = FieldType.Long)
    private int index;
    private String title;
    @Field(type = FieldType.String)
    private String appId;
    @Field(type = FieldType.String)
    private String countryCode;
    @Field(type = FieldType.String)
    private String category;
    @Field(type = FieldType.String)
    private String collection;
    private String icon;
    @Field(type = FieldType.Long)
    private Date createAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
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
