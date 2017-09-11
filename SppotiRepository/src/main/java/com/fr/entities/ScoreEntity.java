package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fr.commons.enumeration.GlobalAppStatusEnum;

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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "scoreEntity", cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sppoti_id")
    private SppotiEntity sppotiEntity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GlobalAppStatusEnum scoreStatus = GlobalAppStatusEnum.PENDING;
    
    /**
     * to get trace of the connected user when using transformers.
     */
    private transient Long connectedUserId;
    
    public SppotiEntity getSppotiEntity() {
        return this.sppotiEntity;
    }

    public void setSppotiEntity(final SppotiEntity sppotiEntity) {
        this.sppotiEntity = sppotiEntity;
    }

    public Integer getHost() {
        return this.host;
    }

    public void setHost(final Integer host) {
        this.host = host;
    }

    public Integer getVisitor() {
        return this.visitor;
    }

    public void setVisitor(final Integer visitor) {
        this.visitor = visitor;
    }

    public GlobalAppStatusEnum getScoreStatus() {
        return this.scoreStatus;
    }

    public void setScoreStatus(final GlobalAppStatusEnum scoreStatus) {
        this.scoreStatus = scoreStatus;
    }
    
    public Long getConnectedUserId() {
        return this.connectedUserId;
    }
    
    public void setConnectedUserId(final Long connectedUserId) {
        this.connectedUserId = connectedUserId;
    }
    
    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        final ScoreEntity that = (ScoreEntity) o;

        if (this.host != null ? !this.host.equals(that.host) : that.host != null) return false;
        if (this.visitor != null ? !this.visitor.equals(that.visitor) : that.visitor != null) return false;
        return this.scoreStatus == that.scoreStatus;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (this.host != null ? this.host.hashCode() : 0);
        result = 31 * result + (this.visitor != null ? this.visitor.hashCode() : 0);
        result = 31 * result + (this.scoreStatus != null ? this.scoreStatus.hashCode() : 0);
        return result;
    }
}
