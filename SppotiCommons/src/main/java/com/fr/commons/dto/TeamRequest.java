package com.fr.commons.dto;

/**
 * Created by djenanewail on 1/23/17.
 */
public class TeamRequest {
    private String name;
    private String logoPath;
    private String coverPath;
    private Long[] memberIdList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public Long[] getMemberIdList() {
        return memberIdList;
    }

    public void setMemberIdList(Long[] memberIdList) {
        this.memberIdList = memberIdList;
    }
}