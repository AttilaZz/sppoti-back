package com.fr.commons.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by djenanewail on 2/24/17.
 */
public class AbstractCommonDTO {

    protected Integer version;

    protected Integer id;

    /**
     * {@inheritDoc}.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
