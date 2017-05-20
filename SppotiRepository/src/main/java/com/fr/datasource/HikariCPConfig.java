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
	
	/** Datasource url. */
	private String url;
	
	/** Database username. */
	private String username;
	
	/** Database password. */
	private String password;
	
	/** is datasource for production or developpement. */
	private Boolean production;
	
	/**
	 * @return Hikary datasource.
	 */
	@Bean(destroyMethod = "close")
	public HikariDataSource dataSource()
	{
		final HikariDataSource ds = new HikariDataSource();
		
		final String dbName, userName, password, hostname, port, jdbcUrl;
		if (this.production) {
			//Get Configuration From System in prod environment
			dbName = System.getProperty("RDS_DB_NAME");
			userName = System.getProperty("RDS_USERNAME");
			password = System.getProperty("RDS_PASSWORD");
			hostname = System.getProperty("RDS_HOSTNAME");
			port = System.getProperty("RDS_PORT");
			jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" +
					password;
		} else {
			userName = this.username;
			password = this.password;
			jdbcUrl = this.url;
		}
		
		ds.setMaximumPoolSize(this.poolSize);
		ds.setDriverClassName(this.driverClassName);
		
		ds.setJdbcUrl(jdbcUrl);
		ds.setUsername(userName);
		ds.setPassword(password);
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
	
	public Boolean getProduction()
	{
		return this.production;
	}
	
	public void setProduction(final Boolean production)
	{
		this.production = production;
	}
}
