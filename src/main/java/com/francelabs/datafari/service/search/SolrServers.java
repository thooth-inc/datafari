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
package com.francelabs.datafari.service.search;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.francelabs.datafari.alerts.AlertsManager;
import com.francelabs.datafari.utils.ScriptConfiguration;
import com.francelabs.datafari.utils.SolrConfiguration;

public class SolrServers {

	private final static Logger LOGGER = Logger.getLogger(AlertsManager.class.getName());

	public enum Core {
		FILESHARE {
			@Override
			public String toString() {
				return "FileShare";
			}
		},
		STATISTICS {
			@Override
			public String toString() {
				return "Statistics";
			}
		},
		PROMOLINK {
			@Override
			public String toString() {
				return "Promolink";
			}
		}
	}

	private static Map<Core, SolrClient> solrClients = new HashMap<Core, SolrClient>();

	public static SolrClient getSolrServer(final Core core, final String protocol) throws Exception {
		if (!solrClients.containsKey(core)) {
			try {
				SolrClient solrClient;
				if (ScriptConfiguration.getProperty("SOLRCLOUD").equals("true")) {
					solrClient = new CloudSolrClient(SolrConfiguration.getProperty(SolrConfiguration.SOLRHOST) + ":"
							+ SolrConfiguration.getProperty(SolrConfiguration.ZOOKEEPERPORT));
					((CloudSolrClient) solrClient).setDefaultCollection(core.toString());
				} else {
					solrClient = new HttpSolrClient(protocol + "//" + SolrConfiguration.getProperty(SolrConfiguration.SOLRHOST) + ":"
							+ SolrConfiguration.getProperty(SolrConfiguration.SOLRPORT) + "/"
							+ SolrConfiguration.getProperty(SolrConfiguration.SOLRWEBAPP) + "/" + core.toString());
				}
				solrClients.put(core, solrClient);
			} catch (final Exception e) {
				LOGGER.error("Cannot instanciate Solr Client for core : " + core.toString(), e);
				throw new Exception("Cannot instanciate Solr Client for core : " + core.toString());
			}
		}
		return solrClients.get(core);
	}

}
