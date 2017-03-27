/**
 *
 */
package com.fr.filter;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Created by: Wail DJENANE on Jun 29, 2016
 */
public enum HeadersAttributes {

    ATTR_ORIGIN, ATTR_CREDENTIALS, ATTR_METHODS, ATTR_AGE, ATTR_HEADER;

    private static final String PATH = "/headerConfig.properties";

    private static final Logger LOGGER = Logger.getLogger(HeadersAttributes.class);

    private static Properties properties;

    private String value;

    private void init() {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(HeadersAttributes.class.getResourceAsStream(PATH));
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
