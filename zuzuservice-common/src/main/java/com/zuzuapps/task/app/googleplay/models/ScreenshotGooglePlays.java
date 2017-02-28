package com.zuzuapps.task.app.googleplay.models;

import java.util.List;

/**
 * @author tuanta17
 */
public class ScreenshotGooglePlays {
    private String appId;
    private List<String> screenshots;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
    }
}
