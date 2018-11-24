package com.chwilio.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolrConfig {
	@Value("${solr.baseUrl}")
	private String baseUrl;
	
	public SolrClient getSolrClient() {
		return new HttpSolrClient.Builder(baseUrl).withConnectionTimeout(10000).withSocketTimeout(60000).build();
	}
}