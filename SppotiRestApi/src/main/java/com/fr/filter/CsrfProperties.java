package com.fr.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 5/23/17.
 */

@ConfigurationProperties(prefix = "spring.app.csrf")
@Component
public class CsrfProperties
{
	
	/** Domain name in which token is set. */
	private String domain;
	
	/** Set to tru in production and test. */
	private boolean secureConnexion;
	
	/** csrf path */
	private String path;
	
	/** csrf comment. */
	private String comment;
	
	/** Cookie expiry. */
	private int maxAge;
	
	public String getComment()
	{
		return this.comment;
	}
	
	public void setComment(final String comment)
	{
		this.comment = comment;
	}
	
	public int getMaxAge()
	{
		return this.maxAge;
	}
	
	public void setMaxAge(final int maxAge)
	{
		this.maxAge = maxAge;
	}
	
	public String getDomain()
	{
		return this.domain;
	}
	
	public void setDomain(final String domain)
	{
		this.domain = domain;
	}
	
	public boolean isSecureConnexion()
	{
		return this.secureConnexion;
	}
	
	public void setSecureConnexion(final boolean secureConnexion)
	{
		this.secureConnexion = secureConnexion;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	public void setPath(final String path)
	{
		this.path = path;
	}
}
