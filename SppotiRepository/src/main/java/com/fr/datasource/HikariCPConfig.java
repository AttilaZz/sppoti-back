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
public class HikariCPConfig{

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
     *
     * @return Hikary datasource.
     */
    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() {
        final HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(poolSize);
        ds.setDriverClassName(driverClassName);
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
