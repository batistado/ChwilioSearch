package com.chwilio.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import com.chwilio.model.Tweet;

public interface SearchQueryService {
	public List<Tweet> searchQuery(String query, String page) throws SolrServerException, IOException;
}
