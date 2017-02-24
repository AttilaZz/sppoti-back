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
    @Column(name = "id")
    private Long id;

    /**
     * version.
     */
    @Version
    @Column(name = "version")
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
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
        result = (prime * result) + ((this.version == null) ? 0 : this.version.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AbstractCommonEntity)) {
            return false;
        }
        final AbstractCommonEntity other = (AbstractCommonEntity) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!this.version.equals(other.version)) {
            return false;
        }
        return true;
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
