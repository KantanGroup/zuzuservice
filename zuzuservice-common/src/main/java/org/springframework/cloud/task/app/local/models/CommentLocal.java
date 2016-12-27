package org.springframework.cloud.task.app.local.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "comments")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentLocal {
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
