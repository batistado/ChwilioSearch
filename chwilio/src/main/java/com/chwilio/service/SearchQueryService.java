package com.chwilio.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;

import com.chwilio.model.TweetDetails;

public interface SearchQueryService {
	public Map<String, Object> searchQuery(String query, String page) throws SolrServerException, IOException;
	public TweetDetails getTweetDetails(String id) throws SolrServerException, IOException;
	public Map<String, String> getTranslatedTweet(Map<String, String> tweet) throws SolrServerException, IOException;
}
