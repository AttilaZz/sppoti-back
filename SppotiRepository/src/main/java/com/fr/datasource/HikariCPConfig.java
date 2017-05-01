package com.fr.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by djenanewail on 3/18/17.
 */

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class HikariCPConfig
{
	
	/**
	 * Driver class name.
	 */
	private String driverClassName;
	
	/**
	 * Datasource url
	 */
	private String url;
	
	/**
	 * Database username
	 */
	private String username;
	
	/**
	 * Databse password.
	 */
	private String password;
	
	/**
	 * Databse poolsize.
	 */
	private int poolSize;
	
	/**
	 * @return Hikary datasource.
	 */
	@Bean(destroyMethod = "close")
	public HikariDataSource dataSource()
	{
		final HikariDataSource ds = new HikariDataSource();
		ds.setMaximumPoolSize(this.poolSize);
		ds.setDriverClassName(this.driverClassName);
		ds.setJdbcUrl(this.url);
		ds.setUsername(this.username);
		ds.setPassword(this.password);
		return ds;
	}
	
	public String getDriverClassName()
	{
		return this.driverClassName;
	}
	
	public void setDriverClassName(final String driverClassName)
	{
		this.driverClassName = driverClassName;
	}
	
	public String getUrl()
	{
		return this.url;
	}
	
	public void setUrl(final String url)
	{
		this.url = url;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername(final String username)
	{
		this.username = username;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public void setPassword(final String password)
	{
		this.password = password;
	}
	
	public int getPoolSize()
	{
		return this.poolSize;
	}
	
	public void setPoolSize(final int poolSize)
	{
		this.poolSize = poolSize;
	}
}
