package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * DTO of the {@link ScoreDTO entity}.
 * <p>
 * Created by wdjenane on 04/04/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoreDTO
        extends AbstractCommonDTO{

    @NotEmpty
    private Integer sppotiId;
    @NotEmpty
    private Integer host;
    @NotEmpty
    private Integer visitor;

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
}
