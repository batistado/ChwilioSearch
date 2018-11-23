package com.chwilio.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolrConfig {
	private static final String baseUrl = "http://localhost:8983/solr";
	private static SolrClient client = null;
	
	public static SolrClient getSolrClient() {
		if (client == null)
			client = new HttpSolrClient.Builder(baseUrl).withConnectionTimeout(10000).withSocketTimeout(60000).build();
		return client;
	}
}