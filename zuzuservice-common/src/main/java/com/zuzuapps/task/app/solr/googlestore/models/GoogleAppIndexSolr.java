package com.zuzuapps.task.app.solr.googlestore.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

/**
 * @author tuanta17
 */
@SolrDocument(solrCoreName = "app-index")
public class GoogleAppIndexSolr {
    @Indexed
    @Id
    private String id;
    @Indexed
    private int index;
    @Indexed
    private String title;
    @Indexed(name = "app_id", type = "string")
    private String appId;
    @Indexed(name = "country_code", type = "string")
    private String countryCode;
    @Indexed
    private String category;
    @Indexed
    private String collection;
    @Indexed
    private String icon;
    @Indexed(name = "developer_id", type = "string")
    private String developerId;
    @Indexed
    private float point;
    @Indexed
    private float score;
    @Indexed
    private boolean free;
    @Indexed
    private String price;

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

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
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
}
