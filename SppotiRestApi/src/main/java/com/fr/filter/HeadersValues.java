package com.fr.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by: Wail DJENANE on Jun 29, 2016
 */
public enum HeadersValues
{
	
	Origins, AllowCredentials, DenyCredentials, GetMethods, PostMethods, DeleteMethods, PutMethods, OptionsMethods,
	AllMethods, Max_Age, Allowed_Headers;
	
	private static final String PATH = "/headerConfig.properties";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HeadersValues.class);
	
	private static Properties properties;
	
	private String value;
	
	private void init()
	{
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(HeadersValues.class.getResourceAsStream(PATH));
			} catch (final Exception e) {
				LOGGER.info("Unable to load " + PATH + " file from classpath.", e);
				System.exit(1);
			}
		}
		this.value = (String) properties.get(this.toString());
	}
	
	public String getValue()
	{
		if (this.value == null) {
			init();
		}
		return this.value;
	}
	
}