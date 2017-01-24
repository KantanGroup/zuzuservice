package com.zuzuapps.task.app.master.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author tuanta17
 */
@Embeddable
public class AppIndexId implements Serializable {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
