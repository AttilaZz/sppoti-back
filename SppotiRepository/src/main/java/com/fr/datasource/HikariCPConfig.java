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
	
	/** Driver class name. */
	private String driverClassName;
	
	/** Databse poolsize. */
	private int poolSize;
	
	/** Database username. */
	private String username;
	
	/** Database password. */
	private String password;
	
	/** Mysql host name. */
	private String host;
	
	/** Database name. */
	private String dbName;
	
	/** is datasource for production or developpement. */
	private Boolean production;
	
	/**
	 * @return Hikary datasource.
	 */
	@Bean(destroyMethod = "close")
	public HikariDataSource dataSource()
	{
		final HikariDataSource ds = new HikariDataSource();
		
		final String url = "jdbc:mysql://" + this.host + ":3306/" + this.dbName;
		
		ds.setMaximumPoolSize(this.poolSize);
		ds.setDriverClassName(this.driverClassName);
		
		ds.setUsername(this.username);
		ds.setPassword(this.password);
		ds.setJdbcUrl(url);
		
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
	
	public int getPoolSize()
	{
		return this.poolSize;
	}
	
	public void setPoolSize(final int poolSize)
	{
		this.poolSize = poolSize;
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
	
	public Boolean getProduction()
	{
		return this.production;
	}
	
	public void setProduction(final Boolean production)
	{
		this.production = production;
	}
	
	public String getHost() {
		return this.host;
	}
	
	public void setHost(final String host) {
		this.host = host;
	}
	
	public String getDbName() {
		return this.dbName;
	}
	
	public void setDbName(final String dbName) {
		this.dbName = dbName;
	}
}
