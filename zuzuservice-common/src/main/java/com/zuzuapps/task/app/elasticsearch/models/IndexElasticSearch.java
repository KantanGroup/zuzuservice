package com.zuzuapps.task.app.elasticsearch.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author tuanta17
 */
@Document(indexName = "app-index", type = "app-index", shards = 1, replicas = 0, refreshInterval = "-1")
public class IndexElasticSearch {
    @Id
    private String id;
    @Field(type = FieldType.Integer)
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
