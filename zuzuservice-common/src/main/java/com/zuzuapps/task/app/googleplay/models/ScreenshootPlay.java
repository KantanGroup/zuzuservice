package com.zuzuapps.task.app.googleplay.models;

/**
 * @author tuanta17
 */
public class ScreenshootPlay {
    private long id;
    private String appId;
    private int type;
    private String source;
    private String original;
    private String watermark;
    private String resize;

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

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }

    public String getResize() {
        return resize;
    }

    public void setResize(String resize) {
        this.resize = resize;
    }
}
