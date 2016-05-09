/*******************************************************************************
 * Copyright 2015 France Labs
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
 *******************************************************************************/
package com.francelabs.datafari.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Configuration reader
 *
 * @author France Labs
 *
 */
public class SolrConfiguration {

	// Properties
	public static final String SOLRHOST = "SOLRHOST";
	public static final String SOLRWEBAPP = "SOLRWEBAPP";
	public static final String SOLRPORT = "SOLRPORT";
	public static final String ZOOKEEPERPORT = "ZOOKEEPERPORT";
	public static final String SOLRPROTOCOL = "SOLRPROTOCOL";

	private static String configPropertiesFileName = "solr.properties";

	private static String configPropertiesFileNameRealPath;

	private static SolrConfiguration instance;
	private final Properties properties;

	private final static Logger LOGGER = Logger.getLogger(SolrConfiguration.class.getName());

	/**
	 * Set a property and save it the alerts.properties
	 *
	 * @param key
	 *            : the key that should be change
	 * @param value
	 *            : the new value of the key
	 * @return : true if there's an error and false if not
	 */
	public static synchronized boolean setProperty(final String key, final String value) {
		try {
			getInstance().properties.setProperty(key, value);
			final FileOutputStream fileOutputStream = new FileOutputStream(configPropertiesFileNameRealPath);
			instance.properties.store(fileOutputStream, null);
			fileOutputStream.close();
			return false;
		} catch (final IOException e) {
			LOGGER.error(e);
			return true;
		}
	}

	public static synchronized String getProperty(final String key) throws IOException {
		return (String) getInstance().properties.get(key);
	}

	/**
	 *
	 * Get the instance
	 *
	 */
	private static SolrConfiguration getInstance() throws IOException {
		if (null == instance) {
			instance = new SolrConfiguration();
		}
		return instance;
	}

	/**
	 *
	 * Read the properties file to get the parameters to create instance
	 *
	 */
	private SolrConfiguration() throws IOException {
		configPropertiesFileNameRealPath = System.getProperty("catalina.home") + File.separator + "conf" + File.separator + configPropertiesFileName;
		final File configFile = new File(configPropertiesFileNameRealPath);
		final InputStream stream = new FileInputStream(configFile);
		properties = new Properties();
		try {
			properties.load(stream);
		} catch (final IOException e) {
			LOGGER.error("Cannot read file : " + configFile.getAbsolutePath(), e);
			throw e;
		} finally {
			stream.close();
		}

	}

}