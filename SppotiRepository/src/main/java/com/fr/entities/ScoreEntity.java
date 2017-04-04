package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * This entity save sppoti scores.
 * <p>
 * Created by wdjenane on 04/04/2017.
 */
@Entity
@Table(name = "SCORE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoreEntity
        extends AbstractCommonEntity {

    @Column(nullable = false)
    private Integer host;

    @Column(nullable = false)
    private Integer visitor;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "scoreEntity")
    @JoinColumn(name = "sppoti_id")
    private SppotiEntity sppotiEntity;

    public SppotiEntity getSppotiEntity() {
        return sppotiEntity;
    }

    public void setSppotiEntity(SppotiEntity sppotiEntity) {
        this.sppotiEntity = sppotiEntity;
    }

    public Integer getHost() {
        return host;
    }

    public void setHost(Integer host) {
        this.host = host;
    }

    public Integer getVisitor() {
        return visitor;
    }

    public void setVisitor(Integer visitor) {
        this.visitor = visitor;
    }
}
