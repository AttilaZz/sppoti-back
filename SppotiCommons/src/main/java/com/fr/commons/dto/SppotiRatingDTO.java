package com.fr.commons.dto;

/**
 * Created by djenanewail on 3/12/17.
 */
public class SppotiRatingDTO extends AbstractCommonDTO{

    Integer sppotiId;
    Integer sppotiRaterId;
    Integer sppotiRatedId;

    public Integer getSppotiId() {
        return sppotiId;
    }

    public void setSppotiId(Integer sppotiId) {
        this.sppotiId = sppotiId;
    }

    public Integer getSppotiRaterId() {
        return sppotiRaterId;
    }

    public void setSppotiRaterId(Integer sppotiRaterId) {
        this.sppotiRaterId = sppotiRaterId;
    }

    public Integer getSppotiRatedId() {
        return sppotiRatedId;
    }

    public void setSppotiRatedId(Integer sppotiRatedId) {
        this.sppotiRatedId = sppotiRatedId;
    }
}
