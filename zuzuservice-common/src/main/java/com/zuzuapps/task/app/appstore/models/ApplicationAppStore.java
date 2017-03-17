package com.zuzuapps.task.app.appstore.models;

import java.util.List;

/**
 * @author tuanta17
 */
public class ApplicationAppStore {
    private long id;
    private String appId;
    private String title;
    private String description;
    private String icon;
    private List<String> genres;
    private List<Integer> genreIds;
    private String primaryGenre;
    private int primaryGenreId;
    private String contentRating;
    private List<String> languages;
    private long size;
    private String requiredOsVersion;
    private String released;
    private String updated;
    private String releaseNotes;
    private String version;
    private float price;
    private String currency;
    private boolean free;
    private String developerId;
    private String developer;
    private String developerUrl;
    private String developerWebsite;
    private float score;
    private String currentVersionScore;
    private int currentVersionReviews;
    private List<String> screenshots;
    private List<String> ipadScreenshots;
    private List<String> appletvScreenshots;
    private List<String> supportedDevices;
    private String playstoreUrl;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getPrimaryGenre() {
        return primaryGenre;
    }

    public void setPrimaryGenre(String primaryGenre) {
        this.primaryGenre = primaryGenre;
    }

    public int getPrimaryGenreId() {
        return primaryGenreId;
    }

    public void setPrimaryGenreId(int primaryGenreId) {
        this.primaryGenreId = primaryGenreId;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getRequiredOsVersion() {
        return requiredOsVersion;
    }

    public void setRequiredOsVersion(String requiredOsVersion) {
        this.requiredOsVersion = requiredOsVersion;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getDeveloperUrl() {
        return developerUrl;
    }

    public void setDeveloperUrl(String developerUrl) {
        this.developerUrl = developerUrl;
    }

    public String getDeveloperWebsite() {
        return developerWebsite;
    }

    public void setDeveloperWebsite(String developerWebsite) {
        this.developerWebsite = developerWebsite;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getCurrentVersionScore() {
        return currentVersionScore;
    }

    public void setCurrentVersionScore(String currentVersionScore) {
        this.currentVersionScore = currentVersionScore;
    }

    public int getCurrentVersionReviews() {
        return currentVersionReviews;
    }

    public void setCurrentVersionReviews(int currentVersionReviews) {
        this.currentVersionReviews = currentVersionReviews;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
    }

    public List<String> getIpadScreenshots() {
        return ipadScreenshots;
    }

    public void setIpadScreenshots(List<String> ipadScreenshots) {
        this.ipadScreenshots = ipadScreenshots;
    }

    public List<String> getAppletvScreenshots() {
        return appletvScreenshots;
    }

    public void setAppletvScreenshots(List<String> appletvScreenshots) {
        this.appletvScreenshots = appletvScreenshots;
    }

    public List<String> getSupportedDevices() {
        return supportedDevices;
    }

    public void setSupportedDevices(List<String> supportedDevices) {
        this.supportedDevices = supportedDevices;
    }

    public String getPlaystoreUrl() {
        return playstoreUrl;
    }

    public void setPlaystoreUrl(String playstoreUrl) {
        this.playstoreUrl = playstoreUrl;
    }
}
