package com.chwilio.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chwilio.config.SolrConfig;
import com.chwilio.model.*;

@Service
public class SearchService implements SearchQueryService {
	@Autowired
	private SolrConfig solr;
	
	private String id;
	private String text;
	private String city;
	private String lang;
	private Date date;
	private String topic;
	private String username;
	private String tweetUrl;
	private String userProfileImage;
	private String tQuery;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> searchQuery(String query, String page) throws SolrServerException, IOException{
		final SolrClient client = solr.getSolrClient();
		
		//  Computing the start from the page number
		Integer pageNumber = Integer.parseInt(page);
		Integer start = (pageNumber-1)*10;
		
		TranslationService tService = new TranslationService(query);
		try {
			tQuery = tService.translateQuery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final Map<String, String> queryParamMap = new HashMap<String, String>();
		if(tQuery != null) {
			queryParamMap.put("q", tQuery.toString());
		}else {
		queryParamMap.put("q", query.toString());
		}
		queryParamMap.put("start", start.toString());
		queryParamMap.put("rows", "10");
		MapSolrParams queryParams = new MapSolrParams(queryParamMap);

		final QueryResponse response = client.query("IRF18P4", queryParams);
		final SolrDocumentList documents = response.getResults();
		
		List<Tweet> searchResults = new ArrayList<Tweet>();
		Map<String, Object> result = new HashMap<String,Object>();
		
		
		for(SolrDocument document : documents) {
			
			id = document.containsKey("id") ? document.getFieldValue("id").toString() : null;
			text = document.containsKey("text") ? ((ArrayList<String>) document.getFieldValue("text")).get(0) : null;
			date = document.containsKey("tweet_date") ? ((ArrayList<Date>) document.getFieldValue("tweet_date")).get(0) : null;
			username = document.containsKey("user.name") ? ((ArrayList<String>) document.getFieldValue("user.name")).get(0) : null;
			userProfileImage = document.containsKey("user.profile_image_url") ? ((ArrayList<String>) document.getFieldValue("user.profile_image_url")).get(0) : null;
					
			searchResults.add(new Tweet(id, text, date, username, userProfileImage));
		}
		
		result.put("numberOfTweets", response.getResults().getNumFound());
		result.put("tweets", searchResults);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public TweetDetails getTweetDetails(String id) throws SolrServerException, IOException {
		final SolrClient client = solr.getSolrClient();
		
		final Map<String, String> queryParamMap = new HashMap<String, String>();
		queryParamMap.put("q", "id:" + id);
		MapSolrParams queryParams = new MapSolrParams(queryParamMap);

		final QueryResponse response = client.query("IRF18P4", queryParams);
		final SolrDocumentList documents = response.getResults();
		
		SolrDocument document = documents.get(0);
		id = document.containsKey("id") ? document.getFieldValue("id").toString() : null;
		text = document.containsKey("text") ? ((ArrayList<String>) document.getFieldValue("text")).get(0) : null;
		city = document.containsKey("city") ? ((ArrayList<String>) document.getFieldValue("city")).get(0) : null;
		lang = document.containsKey("lang") ? document.getFieldValue("tweet_lang").toString() : null;
		date = document.containsKey("tweet_date") ? ((ArrayList<Date>) document.getFieldValue("tweet_date")).get(0) : null;
		topic = document.containsKey("topic") ? ((ArrayList<String>) document.getFieldValue("topic")).get(0) : null;
		username = document.containsKey("user.name") ? ((ArrayList<String>) document.getFieldValue("user.name")).get(0) : null;
		tweetUrl = "https://twitter.com/statuses/" + id;
		userProfileImage = document.containsKey("user.profile_image_url") ? ((ArrayList<String>) document.getFieldValue("user.profile_image_url")).get(0) : null;
		
		return new TweetDetails(id, text, city, lang, date, topic, username, tweetUrl, userProfileImage);	
	}
	
	public Map<String, String> getTranslatedTweet(Map<String, String> tweet) throws SolrServerException, IOException {
		TranslationService tService = new TranslationService(tweet.get("text"));
		try {
			tweet.put("translatedText", tService.translateText(tweet.get("lang")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweet;
	}
}
