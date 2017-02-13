package com.zuzuapps.task.app.solr.models;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tuanta17
 */
@SolrDocument(solrCoreName = "app-screenshot-index")
public class AppScreenshotSolr {
    @Indexed
    @Id
    private String id;
    @Field(value = "origins")
    private List<String> screenshotOrigins;
    @Field(value = "sources")
    private List<String> screenshotSources;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getScreenshotOrigins() {
        if (screenshotOrigins == null) {
            screenshotOrigins = new ArrayList<String>();
        }
        return screenshotOrigins;
    }

    public void setScreenshotOrigins(List<String> screenshotOrigins) {
        this.screenshotOrigins = screenshotOrigins;
    }

    public List<String> getScreenshotSources() {
        if (screenshotSources == null) {
            screenshotSources = new ArrayList<String>();
        }
        return screenshotSources;
    }

    public void setScreenshotSources(List<String> screenshotSources) {
        this.screenshotSources = screenshotSources;
    }
}
