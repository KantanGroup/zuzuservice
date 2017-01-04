package com.zuzuapps.task.app.queue.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "queue_app_application_comment_s")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppCommentQueue {
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
