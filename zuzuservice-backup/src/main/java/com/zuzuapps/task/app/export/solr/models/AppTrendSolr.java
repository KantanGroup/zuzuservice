package com.zuzuapps.task.app.export.solr.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

/**
 * @author tuanta17
 */
@SolrDocument(solrCoreName = "app-trend-index")
public class AppTrendSolr {
    @Indexed
    @Id
    private String id;
    @Indexed(type = "string")
    private int index;
    @JsonProperty("app_id")
    @Indexed(name = "app_id", type = "string")
    private String appId;
    @JsonProperty("country_code")
    @Indexed(name = "country_code", type = "string")
    private String countryCode;
    @Indexed(type = "string")
    private String category;
    @Indexed(type = "string")
    private String collection;
    @Indexed(type = "double")
    private float score;
    @Indexed(type = "boolean")
    private boolean free;
    @Indexed(type = "string")
    private String price;
    @JsonProperty("create_at")
    @Indexed(name = "create_at", type = "date")
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

    public void setCreateAt(Date[] createAt) {
        this.createAt = createAt[0];
    }
}
