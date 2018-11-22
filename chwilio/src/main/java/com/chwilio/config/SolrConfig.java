package com.chwilio.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class SolrConfig {
	public SolrClient getSolrClient() {
		return new HttpSolrClient.Builder("http://localhost:8983/solr").withConnectionTimeout(10000).withSocketTimeout(60000).build();
	}
}