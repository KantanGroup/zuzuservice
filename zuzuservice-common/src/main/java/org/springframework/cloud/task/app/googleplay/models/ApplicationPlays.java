package org.springframework.cloud.task.app.googleplay.models;

import java.util.List;

/**
 * @author tuanta17
 */
public class ApplicationPlays {
    private List<ApplicationPlay> results;

    public List<ApplicationPlay> getResults() {
        return results;
    }

    public void setResults(List<ApplicationPlay> results) {
        this.results = results;
    }
}
