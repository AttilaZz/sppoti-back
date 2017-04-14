package com.fr.entities;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by wdjenane on 23/02/2017.
 */

@MappedSuperclass
public class AbstractCommonEntity {

    private static final long serialVersionUID = -6893399491928930624L;

    /**
     * technical id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * version.
     */
    @Version
    @Column(name = "version", nullable = false)
    private Integer version = -1;

    /**
     * fonctionnal id.
     */
    @Column(nullable = false, unique = true, updatable = false)
    private int uuid = UUID.randomUUID()
            .hashCode() * UUID.randomUUID().hashCode();

    /**
     * {@inheritDoc}.
     */
    @Override
    public String toString() {
//        return ToStringBuilder.reflectionToString(this);
        return new ToStringBuilder(this).append("id", id).toString();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractCommonEntity that = (AbstractCommonEntity) o;

        if (uuid != that.uuid) return false;
        if (!id.equals(that.id)) return false;
        return version.equals(that.version);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        int result = 31;
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        result = 31 * result + uuid;
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }
}
