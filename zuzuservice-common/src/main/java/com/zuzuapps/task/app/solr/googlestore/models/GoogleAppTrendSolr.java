package com.zuzuapps.task.app.solr.googlestore.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

/**
 * @author tuanta17
 */
@SolrDocument(solrCoreName = "app-trend-index")
public class GoogleAppTrendSolr {
    @Indexed
    @Id
    private String id;
    @Indexed
    private int index;
    @Indexed(name = "app_id")
    private String appId;
    @Indexed(name = "country_code")
    private String countryCode;
    @Indexed
    private String category;
    @Indexed
    private String collection;
    @Indexed
    private float point;
    @Indexed
    private float score;
    @Indexed
    private boolean free;
    @Indexed
    private String price;
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

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        if (point != 0.0)
            this.point = point;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
        setPoint(score);
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
