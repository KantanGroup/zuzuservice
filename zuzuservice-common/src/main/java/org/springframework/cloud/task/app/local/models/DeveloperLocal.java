package org.springframework.cloud.task.app.local.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "developments")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeveloperLocal {
    @Id
    @Column(name = "developer_id")
    private String devId;
    private String url;

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
