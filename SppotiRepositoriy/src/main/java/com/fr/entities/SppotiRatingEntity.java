package com.fr.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by djenanewail on 3/12/17.
 */
@Entity
@Table(name = "rating")
public class SppotiRatingEntity extends AbstractCommonEntity{

    @ManyToOne
    @JoinColumn(name = "sppoti_id", nullable = false)
    private SppotiEntity sppotiEntity;

    @ManyToOne
    @JoinColumn(name = "rated_sppoter", nullable = false)
    private UserEntity ratedSppoter;

    @ManyToOne
    @JoinColumn(name = "rater_sppoter", nullable = false)
    private UserEntity raterSppoter;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date ratingDate;

    @Column(nullable = false)
    private Integer starsCount = 0;

    public SppotiEntity getSppotiEntity() {
        return sppotiEntity;
    }

    public void setSppotiEntity(SppotiEntity sppotiEntity) {
        this.sppotiEntity = sppotiEntity;
    }

    public UserEntity getRatedSppoter() {
        return ratedSppoter;
    }

    public void setRatedSppoter(UserEntity ratedSppoter) {
        this.ratedSppoter = ratedSppoter;
    }

    public UserEntity getRaterSppoter() {
        return raterSppoter;
    }

    public void setRaterSppoter(UserEntity raterSppoter) {
        this.raterSppoter = raterSppoter;
    }

    public Date getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }

    public Integer getStarsCount() {
        return starsCount;
    }

    public void setStarsCount(Integer starsCount) {
        this.starsCount = starsCount;
    }
}
