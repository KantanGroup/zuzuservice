package com.zuzuapps.task.app.solr.appstore.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.List;

/**
 * @author tuanta17
 */
@SolrDocument(solrCoreName = "app-information-index")
public class AppleAppInformationSolr {
    @Indexed
    @Id
    private String id;
    @Indexed
    private long aid;
    @Indexed(name = "app_id")
    private String appId;
    @Indexed
    private String title;
    @Indexed
    private String description;
    @Indexed
    private String icon;
    @Indexed
    private List<String> genres;
    @Indexed(name = "genres_ids")
    private List<Integer> genreIds;
    @Indexed(name = "primary_genre")
    private String primaryGenre;
    @Indexed(name = "primary_genre_id")
    private String primaryGenreId;
    @Indexed
    private List<String> languages;
    @Indexed
    private long size;
    @Indexed(name = "required_os_version")
    private String requiredOsVersion;
    @Indexed
    private String released;
    @Indexed
    private String updated;
    @Indexed
    private String version;
    @Indexed
    private float price;
    @Indexed
    private String currency;
    @Indexed
    private boolean free;
    @Indexed(name = "developer_id")
    private String developerId;
    private String developer;
    @Indexed(name = "developer_url")
    private String developerUrl;
    @Indexed(name = "developer_website")
    private String developerWebsite;
    @Indexed
    private float score;
    @Indexed(name = "current_version_score")
    private String currentVersionScore;
    @Indexed(name = "current_version_reviews")
    private int currentVersionReviews;
    @Indexed
    private List<String> screenshots;
    @Indexed(name = "ipad_screenshots")
    private List<String> ipadScreenshots;
    @Indexed(name = "appletv_screenshots")
    private List<String> appletvScreenshots;
    @Indexed(name = "supported_devices")
    private List<String> supportedDevices;
    @Indexed(name = "playstore_url")
    private String playstoreUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
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

    public String getPrimaryGenreId() {
        return primaryGenreId;
    }

    public void setPrimaryGenreId(String primaryGenreId) {
        this.primaryGenreId = primaryGenreId;
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
