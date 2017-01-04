package com.zuzuapps.task.app.queue.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "queue_application_log_s",
        indexes = {
                @Index(name = "app_id_index", columnList = "app_id"),
                @Index(name = "create_at_index", columnList = "create_at"),
                @Index(name = "update_at_index", columnList = "update_at"),
                @Index(name = "score_index", columnList = "score"),
                @Index(name = "price_index", columnList = "price"),
                @Index(name = "free_index", columnList = "free")
        }
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppQueue {
    @Id
    @Column(name = "app_id")
    private String appId;
    private String url;
    private String icon;
    private int score;
    private int price;
    private boolean free;
    @Column(name = "developer_email")
    private String developerEmail;
    @Column(name = "developer_website")
    private String developerWebsite;
    private String updated;
    private String version;
    @Column(name = "min_installs")
    private int minInstalls;
    @Column(name = "max_installs")
    private int maxInstalls;
    private String genre;
    @Column(name = "genre_id")
    private String genreId;
    @Column(name = "offers_iap")
    private boolean offersIAP;
    @Column(name = "ad_supported")
    private boolean adSupported;
    @Column(name = "android_version_text")
    private String androidVersionText;
    @Column(name = "android_version")
    private String androidVersion;
    @Column(name = "content_rating")
    private String contentRating;
    private boolean preregister;
    @Column(name = "playstore_url")
    private String playstoreUrl;
    private String permissions;
    private String similar;
    private String reviews;

    @Column(name = "create_at")
    private Date createAt;
    @Column(name = "update_at")
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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
        return updated.toUpperCase();
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

    public int getMinInstalls() {
        return minInstalls;
    }

    public void setMinInstalls(int minInstalls) {
        this.minInstalls = minInstalls;
    }

    public int getMaxInstalls() {
        return maxInstalls;
    }

    public void setMaxInstalls(int maxInstalls) {
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
