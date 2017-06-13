package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;

/**
 * DTO of the {@link ScoreDTO entity}.
 * <p>
 * Created by wdjenane on 04/04/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoreDTO extends AbstractCommonDTO
{
	
	/** Sppoti Id. */
	@NotNull
	private String sppotiId;
	
	/** Host team score. */
	@NotNull
	private Integer host;
	
	/** Adverse team score. */
	@NotNull
	private Integer visitor;
	
	private String status;
	
	public String getSppotiId()
	{
		return this.sppotiId;
	}
	
	public void setSppotiId(final String sppotiId)
	{
		this.sppotiId = sppotiId;
	}
	
	public Integer getHost()
	{
		return this.host;
	}
	
	public void setHost(final Integer host)
	{
		this.host = host;
	}
	
	public Integer getVisitor()
	{
		return this.visitor;
	}
	
	public void setVisitor(final Integer visitor)
	{
		this.visitor = visitor;
	}
	
	public String getStatus()
	{
		return this.status;
	}
	
	public void setStatus(final String status)
	{
		this.status = status;
	}
}
