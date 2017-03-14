package com.fr.commons.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Created by djenanewail on 3/12/17.
 */
public class SppotiRatingDTO extends AbstractCommonDTO {

    @NotNull
    Integer sppotiId;

    //set automatically using the connected user.
    Integer sppoterRaterId;

    @NotNull
    Integer sppoterRatedId;

    @NotNull
    @Max(value = 10)
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
