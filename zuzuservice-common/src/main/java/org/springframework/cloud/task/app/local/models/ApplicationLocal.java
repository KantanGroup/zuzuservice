package org.springframework.cloud.task.app.local.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "applications")
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationLocal {
    private String appId;
    private String url;
    private String title;
    private String summary;
    private DeveloperLocal developer;
    private String icon;
    private int score;
    private int price;
    private boolean free;
    private String developerEmail;
    private String developerWebsite;
    private String updated;
    private String version;
    private int minInstalls;
    private int maxInstalls;
    private String genre;
    private String genreId;
    private String description;
    private String descriptionHTML;
    // histogram: { type: new List(IntType) },
    private boolean offersIAP;
    private boolean adSupported;
    private String androidVersionText;
    private String androidVersion;
    private String contentRating;
    private List<ScreenshootLocal> screenshots;
    // comments: { type: new List(StringType) },
    //    recentChanges:
    private boolean preregister;
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

    public DeveloperLocal getDeveloper() {
        return developer;
    }

    public void setDeveloper(DeveloperLocal developer) {
        this.developer = developer;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionHTML() {
        return descriptionHTML;
    }

    public void setDescriptionHTML(String descriptionHTML) {
        this.descriptionHTML = descriptionHTML;
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

    public List<ScreenshootLocal> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<ScreenshootLocal> screenshots) {
        this.screenshots = screenshots;
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
