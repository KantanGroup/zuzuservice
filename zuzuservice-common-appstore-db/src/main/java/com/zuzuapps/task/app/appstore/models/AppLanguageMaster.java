package com.zuzuapps.task.app.appstore.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "master_application_language_s")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppLanguageMaster {

    @EmbeddedId
    private AppLanguageId id;
    private String path;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createAt;

    public AppLanguageId getId() {
        return id;
    }

    public void setId(AppLanguageId id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}