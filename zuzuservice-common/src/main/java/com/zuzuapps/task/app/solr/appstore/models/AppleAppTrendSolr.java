package com.zuzuapps.task.app.solr.appstore.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

/**
 * @author tuanta17
 */
@SolrDocument(solrCoreName = "apple-app-trend-index")
public class AppleAppTrendSolr {
    @Indexed
    @Id
    private String tId;
    private long id;
    @Indexed(name = "app_id")
    private String appId;
    private int index;
    @Indexed(name = "country_code")
    private String countryCode;
    @Indexed(type = "string")
    private String category;
    @Indexed(type = "string")
    private String collection;
    @Indexed(name = "create_at", type = "date")
    private Date createAt;

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
