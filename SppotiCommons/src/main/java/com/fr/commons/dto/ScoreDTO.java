package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * DTO of the {@link ScoreDTO entity}.
 * <p>
 * Created by wdjenane on 04/04/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoreDTO
        extends AbstractCommonDTO {

    /**
     * Sppoti Id.
     */
    @NotNull
    private Integer sppotiId;
    /**
     * Host team score.
     */
    @NotNull
    private Integer host;
    /**
     * Adverse team score.
     */
    @NotNull
    private Integer visitor;

    private String status;

    public Integer getSppotiId() {
        return sppotiId;
    }

    public void setSppotiId(Integer sppotiId) {
        this.sppotiId = sppotiId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
