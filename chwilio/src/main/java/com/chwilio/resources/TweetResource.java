package com.chwilio.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;

import com.chwilio.beans.*;
import com.chwilio.config.SolrConfig;

public class TweetResource {
	public static List<Tweet> searchQuery(String query) throws SolrServerException, IOException{
		final SolrClient client = SolrConfig.getSolrClient();
		
		final Map<String, String> queryParamMap = new HashMap<String, String>();
		queryParamMap.put("q", query.toString());
		MapSolrParams queryParams = new MapSolrParams(queryParamMap);

		final QueryResponse response = client.query("IRF18P4", queryParams);
		final SolrDocumentList documents = response.getResults();
		
		List<Tweet> searchResults = new ArrayList<Tweet>();
		for(SolrDocument document : documents) {
		  searchResults.add(new Tweet(document.getFieldValue("id").toString(), ((ArrayList<String>) document.getFieldValue("text")).get(0)));
		}
		
		return searchResults;
	}

}
