package com.fr.entities;

import com.fr.commons.enumeration.GlobalAppStatusEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by djenanewail on 4/17/17.
 */
@Entity
@Table(name = "SPPOTI_ADVERSE")
public class SppotiAdverseEntity extends AbstractCommonEntity{

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GlobalAppStatusEnum status = GlobalAppStatusEnum.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sppoti_id", nullable = false)
    private SppotiEntity sppoti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @Column(name = "from_sppoti_admin")
    private Boolean fromSppotiAdmin = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creationDate = new Date();

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

    public Boolean getFromSppotiAdmin() {
        return fromSppotiAdmin;
    }

    public void setFromSppotiAdmin(Boolean fromSppotiAdmin) {
        this.fromSppotiAdmin = fromSppotiAdmin;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SppotiAdverseEntity entity = (SppotiAdverseEntity) o;

        if (status != entity.status) return false;
        if (fromSppotiAdmin != null ? !fromSppotiAdmin.equals(entity.fromSppotiAdmin) : entity.fromSppotiAdmin != null)
            return false;
        return creationDate != null ? creationDate.equals(entity.creationDate) : entity.creationDate == null;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (fromSppotiAdmin != null ? fromSppotiAdmin.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }
}
