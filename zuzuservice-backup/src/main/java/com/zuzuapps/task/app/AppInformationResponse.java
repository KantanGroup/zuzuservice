package com.zuzuapps.task.app;

import com.zuzuapps.task.app.export.solr.models.AppInformationSolr;

import java.util.List;

/**
 * @author tuanta17
 */
public class AppInformationResponse {
    private long numFound;
    private String start;
    private List<AppInformationSolr> docs;

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

    public List<AppInformationSolr> getDocs() {
        return docs;
    }

    public void setDocs(List<AppInformationSolr> docs) {
        this.docs = docs;
    }
}
