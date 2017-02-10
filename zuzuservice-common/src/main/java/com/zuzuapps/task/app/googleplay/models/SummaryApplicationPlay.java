package com.zuzuapps.task.app.googleplay.models;

/**
 * @author tuanta17
 */
public class SummaryApplicationPlay {
    private String appId;
    private String url;
    private String title;
    private String summary;
    private DeveloperPlay developer;
    private String icon;
    private float score;
    private String price;
    private boolean free;
    private String playstoreUrl;
    private String permissions;
    private String similar;
    private String reviews;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public DeveloperPlay getDeveloper() {
        return developer;
    }

    public void setDeveloper(DeveloperPlay developer) {
        this.developer = developer;
    }

    public String getIcon() {
        if (icon.startsWith("//")) {
            icon = "http:" + icon;
        }
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getPlaystoreUrl() {
        return playstoreUrl;
    }

    public void setPlaystoreUrl(String playstoreUrl) {
        this.playstoreUrl = playstoreUrl;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getSimilar() {
        return similar;
    }

    public void setSimilar(String similar) {
        this.similar = similar;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }
}
