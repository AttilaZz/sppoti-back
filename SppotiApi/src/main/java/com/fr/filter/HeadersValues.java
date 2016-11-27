/**
 * 
 */
package com.fr.filter;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Created by: Wail DJENANE on Jun 29, 2016
 */
public enum HeadersValues {

	Origins, AllowCredentials, DenyCredentials, GetMethods, PostMethods, DeleteMethods, PutMethods, OptionsMethods, AllMethods, Max_Age, Allowed_Headers;

	private static final String PATH = "/headerConfig.properties";

	private static final Logger LOGGER = Logger.getLogger(HeadersValues.class);

	private static Properties properties;

	private String value;

	private void init() {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(HeadersValues.class.getResourceAsStream(PATH));
			} catch (Exception e) {
				LOGGER.info("Unable to load " + PATH + " file from classpath.", e);
				System.exit(1);
			}
		}
		value = (String) properties.get(this.toString());
	}

	public String getValue() {
		if (value == null) {
			init();
		}
		return value;
	}

}