package com.zuzuapps.task.app.solr.googlestore.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;
import java.util.List;

/**
 * @author tuanta17
 */
@SolrDocument(solrCoreName = "app-information-index")
public class GoogleAppInformationSolr {
    @Indexed
    @Id
    private String id;
    @Indexed(name = "app_id")
    private String appId;
    @Indexed(type = "string")
    private String title;
    @Indexed(type = "string")
    private String summary;
    @Indexed(name = "developer_id")
    private String developerId;
    @Indexed(name = "development_url")
    private String developerUrl;
    @Indexed(type = "string")
    private String icon;
    @Indexed(type = "double")
    private float point;
    @Indexed(type = "double")
    private float score;
    @Indexed(type = "string")
    private String price;
    @Indexed(type = "boolean")
    private boolean free;
    @Indexed(name = "developer_email")
    private String developerEmail;
    @Indexed(name = "developer_website")
    private String developerWebsite;
    @Indexed(type = "string")
    private String updated;
    @Indexed(type = "string")
    private String version;
    @Indexed(name = "min_installs")
    private long minInstalls;
    @Indexed(name = "max_installs")
    private long maxInstalls;
    @Indexed(type = "string")
    private String genre;
    @Indexed(name = "description_html")
    private String descriptionHTML;
    @Indexed(name = "family_genre")
    private String familyGenre;
    @Indexed(name = "android_version_text")
    private String androidVersionText;
    @Indexed(name = "android_version")
    private String androidVersion;
    @Indexed(name = "content_rating")
    private String contentRating;
    @Indexed
    private List<String> screenshots;
    @Indexed(type = "string")
    private String video;
    @Indexed(name = "playstore_url")
    private String playstoreUrl;
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

    public void setPoint(float point) {
        if (point != 0.0)
            this.point = point;
    }

    public float getPoint() {
        return point;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
        setPoint(score);
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
