package com.fr.mail;
/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for email support.
 *
 * @author Oliver Gierke
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 * @since 1.2.0
 */
@ConfigurationProperties(prefix = "spring.mail")
@Component
public class MailProperties
{
	
	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	/**
	 * Additional JavaMail session properties.
	 */
	private final Map<String, String> properties = new HashMap<String, String>();
	/**
	 * SMTP server host.
	 */
	private String host;
	/**
	 * SMTP server port.
	 */
	private Integer port;
	/**
	 * Login user of the SMTP server.
	 */
	private String username;
	/**
	 * Login password of the SMTP server.
	 */
	private String password;
	/**
	 * Email from
	 */
	private String from;
	/**
	 * Protocol used by the SMTP server.
	 */
	private String protocol = "smtp";
	/**
	 * Default MimeMessage encoding.
	 */
	private Charset defaultEncoding = DEFAULT_CHARSET;
	/**
	 * Session JNDI name. When set, takes precedence to others mail settings.
	 */
	private String jndiName;
	
	/**
	 * Test that the mail server is available on startup.
	 */
	private boolean testConnection;
	
	public String getHost() {
		return this.host;
	}
	
	public void setHost(final String host) {
		this.host = host;
	}
	
	public Integer getPort() {
		return this.port;
	}
	
	public void setPort(final Integer port) {
		this.port = port;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(final String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(final String password) {
		this.password = password;
	}
	
	public String getProtocol() {
		return this.protocol;
	}
	
	public void setProtocol(final String protocol) {
		this.protocol = protocol;
	}
	
	public Charset getDefaultEncoding() {
		return this.defaultEncoding;
	}
	
	public void setDefaultEncoding(final Charset defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}
	
	public Map<String, String> getProperties() {
		return this.properties;
	}
	
	public String getJndiName() {
		return this.jndiName;
	}
	
	public void setJndiName(final String jndiName) {
		this.jndiName = jndiName;
	}
	
	public boolean isTestConnection() {
		return this.testConnection;
	}
	
	public void setTestConnection(final boolean testConnection) {
		this.testConnection = testConnection;
	}
	
	public String getFrom() {
		return this.from;
	}
	
	public void setFrom(final String from) {
		this.from = from;
	}
}
