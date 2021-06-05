package com.browser.tools.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;


public class ConfigUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);

	private static final String CONF_PATH = "system.properties";

	public static String getPropertyKey(String key) {
		Properties st = getConxtions();
		return st.getProperty(key);
	}

	public static Properties getConxtions() {
		Properties properties = null;
		try {
			properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(CONF_PATH));
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(), e);
		}
		return properties;
	}
}
