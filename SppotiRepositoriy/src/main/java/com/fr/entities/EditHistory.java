package com.fr.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE on Aug 20, 2016
 */

@Entity
@JsonInclude(Include.NON_EMPTY)
public class EditHistory {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 500)
    private String text;

    @Column(nullable = false)
    private String datetimeEdited = new DateTime().toString();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    private CommentEntity comment;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sport_id")
    @JsonIgnore
    private Sport sport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public CommentEntity getComment() {
        return comment;
    }

    public void setComment(CommentEntity comment) {
        this.comment = comment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDatetimeEdited() {
        return datetimeEdited;
    }

    public void setDatetimeEdited(String datetimeEdited) {
        this.datetimeEdited = datetimeEdited;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

}
