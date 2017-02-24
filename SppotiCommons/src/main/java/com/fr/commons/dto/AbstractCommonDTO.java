package com.fr.commons.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by djenanewail on 2/24/17.
 */
public class AbstractCommonDTO {

    private Long id;

    private Integer version;

    private int uuid;

    /**
     * {@inheritDoc}.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
