package com.zuzuapps.task.app.appstore.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "master_application_s",
        indexes = {
                @Index(name = "app_id_index", columnList = "app_id"),
                @Index(name = "developer_id_index", columnList = "developer_id")
        }
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppMaster {
    @Id
    @Column(name = "app_id", length = 128, nullable = false)
    private String appId;
    private String url;
    private String icon;
    private float score;
    @Column(name = "price", length = 64)
    private String price;
    private boolean free;
    @Column(name = "developer_id", length = 128)
    private String developerId;
    @Column(name = "developer_url")
    private String developerUrl;
    @Column(name = "developer_email", length = 64)
    private String developerEmail;
    @Column(name = "developer_website")
    private String developerWebsite;
    private String updated;
    @Column(name = "app_version", length = 64)
    private String appVersion;
    @Column(name = "min_installs")
    private long minInstalls;
    @Column(name = "max_installs")
    private long maxInstalls;
    @Column(name = "genre", length = 64)
    private String genre;
    @Column(name = "genre_id", length = 64)
    private String genreId;
    @Column(name = "family_genre", length = 64)
    private String familyGenre;
    @Column(name = "family_genre_id", length = 64)
    private String familyGenreId;
    @Column(name = "offers_iap")
    private boolean offersIAP;
    @Column(name = "ad_supported")
    private boolean adSupported;
    @Column(name = "android_version_text", length = 36)
    private String androidVersionText;
    @Column(name = "android_version", length = 36)
    private String androidVersion;
    @Column(name = "content_rating", length = 64)
    private String contentRating;
    private boolean preregister;
    private String video;
    @Column(name = "playstore_url")
    private String playstoreUrl;
    private String permissions;
    private String similar;
    private String reviews;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updateAt;

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

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
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

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getFamilyGenre() {
        return familyGenre;
    }

    public void setFamilyGenre(String familyGenre) {
        this.familyGenre = familyGenre;
    }

    public String getFamilyGenreId() {
        return familyGenreId;
    }

    public void setFamilyGenreId(String familyGenreId) {
        this.familyGenreId = familyGenreId;
    }

    public boolean isOffersIAP() {
        return offersIAP;
    }

    public void setOffersIAP(boolean offersIAP) {
        this.offersIAP = offersIAP;
    }

    public boolean isAdSupported() {
        return adSupported;
    }

    public void setAdSupported(boolean adSupported) {
        this.adSupported = adSupported;
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

    public boolean isPreregister() {
        return preregister;
    }

    public void setPreregister(boolean preregister) {
        this.preregister = preregister;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
