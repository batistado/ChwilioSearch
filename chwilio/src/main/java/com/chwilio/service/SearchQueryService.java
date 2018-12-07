package com.chwilio.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;

import com.chwilio.model.TweetDetails;

public interface SearchQueryService {
	public Map<String, Object> searchQuery(String query, String page, Map<String, List<String>> filters) throws SolrServerException, IOException;
	public TweetDetails getTweetDetails(String id) throws SolrServerException, IOException;
	public Map<String, String> getTranslatedTweet(Map<String, String> tweet) throws SolrServerException, IOException;
	public Map<String, List<String>> getTrendingHashtags(Map<String, List<String>> filters) throws SolrServerException, IOException;
	public Map<String, Object> cityWiseTopics(Map<String, List<String>> filters) throws SolrServerException, IOException, ParseException;
	public Map<String, Object> getWordCloud() throws SolrServerException, IOException, ParseException;
}
