package com.chwilio.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chwilio.config.SolrConfig;
import com.chwilio.model.*;

@Service
public class SearchService implements SearchQueryService {
	@Autowired
	private SolrConfig solr;
	
	@Override
	public List<Tweet> searchQuery(String query) throws SolrServerException, IOException{
		final SolrClient client = solr.getSolrClient();
		
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
