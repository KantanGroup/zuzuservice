package com.zuzuapps.task.app.elasticsearch.models;

import com.zuzuapps.task.app.googleplay.models.ScreenshotPlay;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tuanta17
 */
@Document(indexName = "app-screenshot-index", type = "app-screenshot-index", shards = 1, replicas = 0, refreshInterval = "-1")
public class AppScreenshotElasticSearch {
    @Id
    private String id;
    @Field( type = FieldType.Nested)
    private List<ScreenshotPlay> screenshotPlays;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ScreenshotPlay> getScreenshotPlays() {
        if (screenshotPlays == null) {
            screenshotPlays = new ArrayList<ScreenshotPlay>();
        }
        return screenshotPlays;
    }

    public void setScreenshotPlays(List<ScreenshotPlay> screenshotPlays) {
        this.screenshotPlays = screenshotPlays;
    }
}
