package com.zuzuapps.task.app;

import com.zuzuapps.task.app.export.solr.models.AppTrendSolr;

import java.util.List;

/**
 * @author tuanta17
 */
public class AppTrendResponse {
    private long numFound;
    private String start;
    private List<AppTrendSolr> docs;

    public long getNumFound() {
        return numFound;
    }

    public void setNumFound(long numFound) {
        this.numFound = numFound;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public List<AppTrendSolr> getDocs() {
        return docs;
    }

    public void setDocs(List<AppTrendSolr> docs) {
        this.docs = docs;
    }
}
