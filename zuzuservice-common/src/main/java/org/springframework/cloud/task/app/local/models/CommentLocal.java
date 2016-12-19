package org.springframework.cloud.task.app.local.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author tuanta17
 */
@Entity
@Table(name = "comments")
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentLocal {

}
