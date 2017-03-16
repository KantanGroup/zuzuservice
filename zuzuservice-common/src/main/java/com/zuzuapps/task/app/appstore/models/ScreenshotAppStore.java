package com.zuzuapps.task.app.appstore.models;

/**
 * @author tuanta17
 */
public class ScreenshotAppStore {
    private String appId;
    private int type; //icon, screen shot
    private String source;
    private String original;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
