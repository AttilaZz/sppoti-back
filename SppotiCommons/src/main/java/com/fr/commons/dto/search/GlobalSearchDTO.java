package com.fr.commons.dto.search;

import com.fr.commons.enumeration.GenderEnum;

import java.util.Date;

/**
 * This DTO contains all criteria sent from user to for a global search.
 *
 * Created by wdjenane on 22/06/2017.
 */
public class GlobalSearchDTO {
    private GenderEnum gender;
    private Integer minAge;
    private Integer maxAge;
    private Integer sport;
    private Date startDate;
    private String name;

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getSport() {
        return sport;
    }

    public void setSport(Integer sport) {
        this.sport = sport;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
