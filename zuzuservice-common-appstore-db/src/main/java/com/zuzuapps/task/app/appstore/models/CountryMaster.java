package com.zuzuapps.task.app.appstore.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "master_country_language_s",
        indexes = {
                @Index(name = "country_language_index", columnList = "country_code,language_code")
        }
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private short id;
    @Column(name = "language_code", length = 3)
    private String languageCode;
    @Column(name = "language_name", length = 30)
    private String languageName;
    @Column(name = "country_code", length = 3)
    private String countryCode;
    @Column(name = "country_name", length = 30)
    private String countryName;
    private int type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createAt;

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
