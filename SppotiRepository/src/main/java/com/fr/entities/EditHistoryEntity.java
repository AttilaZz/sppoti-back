package com.fr.entities;

import javax.persistence.*;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Aug 20, 2016
 */

@Entity
@Table(name = "EDIT_HISTORY")
@JsonInclude(Include.NON_EMPTY)
public class EditHistoryEntity
        extends AbstractCommonEntity {
    @Column(length = 500)
    private String text;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetimeEdited = new Date();

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private PostEntity post;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    private CommentEntity comment;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id")
    @JsonIgnore
    private SportEntity sport;

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
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

    public Date getDatetimeEdited() {
        return datetimeEdited;
    }

    public void setDatetimeEdited(Date datetimeEdited) {
        this.datetimeEdited = datetimeEdited;
    }

    public SportEntity getSport() {
        return sport;
    }

    public void setSport(SportEntity sport) {
        this.sport = sport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EditHistoryEntity)) return false;
        if (!super.equals(o)) return false;

        EditHistoryEntity that = (EditHistoryEntity) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return datetimeEdited != null ? datetimeEdited.equals(that.datetimeEdited) : that.datetimeEdited == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (datetimeEdited != null ? datetimeEdited.hashCode() : 0);
        return result;
    }
}