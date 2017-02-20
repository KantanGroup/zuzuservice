package com.zuzuapps.task.app.master.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "master_application_comment_s")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppCommentMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
