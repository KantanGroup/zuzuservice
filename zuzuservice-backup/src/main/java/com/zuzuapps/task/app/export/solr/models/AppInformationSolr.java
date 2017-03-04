package com.zuzuapps.task.app.export.solr.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;
import java.util.List;

/**
 * @author tuanta17
 */
@SolrDocument(solrCoreName = "app-information-index")
public class AppInformationSolr {
    @Indexed
    @Id
    private String id;
    @JsonProperty("app_id")
    @Indexed(name = "app_id", type = "string")
    private String appId;
    @Indexed(type = "string")
    private String title;
    @Indexed(type = "string")
    private String summary;
    @JsonProperty("developer_id")
    @Indexed(name = "developer_id", type = "string")
    private String developerId;
    @JsonProperty("development_url")
    @Indexed(name = "development_url", type = "string")
    private String developerUrl;
    @Indexed(type = "string")
    private String icon;
    @Indexed(type = "double")
    private float score;
    @Indexed(type = "string")
    private String price;
    @Indexed(type = "boolean")
    private boolean free;
    @JsonProperty("developer_email")
    @Indexed(name = "developer_email", type = "string")
    private String developerEmail;
    @JsonProperty("developer_website")
    @Indexed(name = "developer_website", type = "string")
    private String developerWebsite;
    @Indexed(type = "string")
    private String updated;
    @Indexed(type = "string")
    private String version;
    @JsonProperty("min_installs")
    @Indexed(name = "min_installs", type = "long")
    private long minInstalls;
    @JsonProperty("max_installs")
    @Indexed(name = "max_installs", type = "long")
    private long maxInstalls;
    @Indexed(type = "string")
    private String genre;
    @JsonProperty("description_html")
    @Indexed(name = "description_html", type = "string")
    private String descriptionHTML;
    @JsonProperty("family_genre")
    @Indexed(name = "family_genre", type = "string")
    private String familyGenre;
    @JsonProperty("android_version_text")
    @Indexed(name = "android_version_text", type = "string")
    private String androidVersionText;
    @JsonProperty("android_version")
    @Indexed(name = "android_version", type = "string")
    private String androidVersion;
    @JsonProperty("content_rating")
    @Indexed(name = "content_rating", type = "string")
    private String contentRating;
    @Indexed
    private List<String> screenshots;
    @Indexed(type = "string")
    private String video;
    @JsonProperty("playstore_url")
    @Indexed(name = "playstore_url", type = "string")
    private String playstoreUrl;
    @JsonProperty("create_at")
    @Indexed(name = "create_at", type = "date")
    private Date createAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getDeveloperUrl() {
        return developerUrl;
    }

    public void setDeveloperUrl(String developerUrl) {
        this.developerUrl = developerUrl;
    }

    public String getIcon() {
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

    public String getDeveloperEmail() {
        return developerEmail;
    }

    public void setDeveloperEmail(String developerEmail) {
        this.developerEmail = developerEmail;
    }

    public String getDeveloperWebsite() {
        return developerWebsite;
    }

    public void setDeveloperWebsite(String developerWebsite) {
        this.developerWebsite = developerWebsite;
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

    public long getMinInstalls() {
        return minInstalls;
    }

    public void setMinInstalls(long minInstalls) {
        this.minInstalls = minInstalls;
    }

    public long getMaxInstalls() {
        return maxInstalls;
    }

    public void setMaxInstalls(long maxInstalls) {
        this.maxInstalls = maxInstalls;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescriptionHTML() {
        return descriptionHTML;
    }

    public void setDescriptionHTML(String descriptionHTML) {
        this.descriptionHTML = descriptionHTML;
    }

    public String getFamilyGenre() {
        return familyGenre;
    }

    public void setFamilyGenre(String familyGenre) {
        this.familyGenre = familyGenre;
    }

    public String getAndroidVersionText() {
        return androidVersionText;
    }

    public void setAndroidVersionText(String androidVersionText) {
        this.androidVersionText = androidVersionText;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getPlaystoreUrl() {
        return playstoreUrl;
    }

    public void setPlaystoreUrl(String playstoreUrl) {
        this.playstoreUrl = playstoreUrl;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
