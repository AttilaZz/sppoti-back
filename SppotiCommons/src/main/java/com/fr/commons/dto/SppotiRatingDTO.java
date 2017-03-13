package com.fr.commons.dto;

/**
 * Created by djenanewail on 3/12/17.
 */
public class SppotiRatingDTO extends AbstractCommonDTO{

    Integer sppotiId;
    Integer sppoterRaterId;
    Integer sppoterRatedId;
    Integer stars;

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getSppotiId() {
        return sppotiId;
    }

    public void setSppotiId(Integer sppotiId) {
        this.sppotiId = sppotiId;
    }

    public Integer getSppoterRaterId() {
        return sppoterRaterId;
    }

    public void setSppoterRaterId(Integer sppoterRaterId) {
        this.sppoterRaterId = sppoterRaterId;
    }

    public Integer getSppoterRatedId() {
        return sppoterRatedId;
    }

    public void setSppoterRatedId(Integer sppoterRatedId) {
        this.sppoterRatedId = sppoterRatedId;
    }
}
