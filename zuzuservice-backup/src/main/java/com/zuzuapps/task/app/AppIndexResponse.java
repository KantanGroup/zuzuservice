package com.zuzuapps.task.app;

import com.zuzuapps.task.app.export.solr.models.AppIndexSolr;

import java.util.List;

/**
 * @author tuanta17
 */
public class AppIndexResponse {
    private long numFound;
    private String start;
    private List<AppIndexSolr> docs;

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

    public List<AppIndexSolr> getDocs() {
        return docs;
    }

    public void setDocs(List<AppIndexSolr> docs) {
        this.docs = docs;
    }
}
