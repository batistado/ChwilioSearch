package com.chwilio.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
	private static String id;
    private static String text;
    private static String city;
    private static String lang;
    private static Date date;
    private static String topic;
	
	@SuppressWarnings("unchecked")
	public static List<Tweet> searchQuery(String query, String page) throws SolrServerException, IOException{
		final SolrClient client = SolrConfig.getSolrClient();
		
		//  Computing the start from the page number
		Integer pageNumber = Integer.parseInt(page);
		Integer start = (pageNumber-1)*10;
		
		
		final Map<String, String> queryParamMap = new HashMap<String, String>();
		queryParamMap.put("q", query.toString());
		queryParamMap.put("start", start.toString());
		queryParamMap.put("rows", "10");
		MapSolrParams queryParams = new MapSolrParams(queryParamMap);

		final QueryResponse response = client.query("IRF18P4", queryParams);
		final SolrDocumentList documents = response.getResults();
		
		List<Tweet> searchResults = new ArrayList<Tweet>();
		for(SolrDocument document : documents) {
			
			id = document.containsKey("id") ? document.getFieldValue("id").toString() : null;
			text = document.containsKey("text") ? ((ArrayList<String>) document.getFieldValue("text")).get(0) : null;
			city = document.containsKey("city") ? ((ArrayList<String>) document.getFieldValue("city")).get(0) : null;
			lang = document.containsKey("lang") ? document.getFieldValue("tweet_lang").toString() : null;
			date = document.containsKey("tweet_date") ? ((ArrayList<Date>) document.getFieldValue("tweet_date")).get(0) : null;
			topic = document.containsKey("topic") ? ((ArrayList<String>) document.getFieldValue("topic")).get(0) : null;
			
			searchResults.add(new Tweet(id, text, city, lang, date, topic));
			
//		  searchResults.add(new Tweet(document.getFieldValue("id").toString(), 
//				  		((ArrayList<String>) document.getFieldValue("text")).get(0),
//				  		((ArrayList<String>) document.getFieldValue("city")).get(0),
//				  		document.getFieldValue("tweet_lang").toString(),
//				  		((ArrayList<Date>) document.getFieldValue("tweet_date")).get(0)));
		}
		
		return searchResults;
	}

}
