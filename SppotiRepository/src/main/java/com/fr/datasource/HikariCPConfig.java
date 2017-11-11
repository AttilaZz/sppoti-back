package com.fr.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by djenanewail on 3/18/17.
 */

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class HikariCPConfig
{
	private final Logger LOGGER = LoggerFactory.getLogger(HikariCPConfig.class);
	
	private String driverClassName;
	private String username;
	private String password;
	private String passwordFile;
	private String host;
	private String dbName;
	private String url;
	private int poolSize;
	private int port;
	
	@Bean(destroyMethod = "close")
	public HikariDataSource dataSource()
	{
		final HikariDataSource ds = new HikariDataSource();
		
		String jdbcUrl = this.url;
		if (StringUtils.isEmpty(this.url)) {
			jdbcUrl = "jdbc:mysql://" + this.host + ":3306/" + this.dbName;
		}
		
		ds.setMaximumPoolSize(this.poolSize);
		ds.setDriverClassName(this.driverClassName);
		
		ds.setUsername(this.username);
		ds.setPassword(this.password);
		
		if (StringUtils.hasText(this.passwordFile)) {
			this.LOGGER.info("Reading password from a file: " + this.passwordFile);
			ds.setPassword(getPasswordFromFile());
		}
		
		ds.setJdbcUrl(jdbcUrl);
		
		return ds;
	}
	
	private String getPasswordFromFile() {
		
		String password = null;
		
		try (Stream<String> stream = Files.lines(Paths.get(this.passwordFile))) {
			
			final Optional<String> pass = stream.findFirst();
			if (pass.isPresent()) {
				password = pass.get().trim();
			}
			
		} catch (final IOException e) {
			this.LOGGER.error("Can't read file ", e);
		}
		
		return password;
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
	
	public String getPasswordFile() {
		return this.passwordFile;
	}
	
	public void setPasswordFile(final String passwordFile) {
		this.passwordFile = passwordFile;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public void setPort(final int port) {
		this.port = port;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(final String url) {
		this.url = url;
	}
}
