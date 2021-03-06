package com.fr.commons.enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by djenanewail on 12/1/17.
 */
public enum FirebaseNotifPropEnum
{
	ATTR_ORIGIN, ATTR_CREDENTIALS, ATTR_METHODS, ATTR_AGE, ATTR_HEADER, ATTR_EXPOSE_HEADERS;
	
	private static final String PATH = "/properties/firebase-notification.properties";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseNotifPropEnum.class);
	
	private static Properties properties;
	
	private String value;
	
	private void init()
	{
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(FirebaseNotifPropEnum.class.getResourceAsStream(PATH));
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
