package com.fr.entities;

import com.fr.commons.enumeration.GlobalAppStatusEnum;

import javax.persistence.*;

/**
 * Created by djenanewail on 4/17/17.
 */
@Entity
@Table(name = "SPPOTI_ADVERSE")
public class SppotiAdverseEntity extends AbstractCommonEntity{

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GlobalAppStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sppoti_id", nullable = false)
    private SppotiEntity sppoti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    public GlobalAppStatusEnum getStatus() {
        return status;
    }

    public void setStatus(GlobalAppStatusEnum status) {
        this.status = status;
    }

    public SppotiEntity getSppoti() {
        return sppoti;
    }

    public void setSppoti(SppotiEntity sppoti) {
        this.sppoti = sppoti;
    }

    public TeamEntity getTeam() {
        return team;
    }

    public void setTeam(TeamEntity team) {
        this.team = team;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SppotiAdverseEntity that = (SppotiAdverseEntity) o;

        return status == that.status;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
