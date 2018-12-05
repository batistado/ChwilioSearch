package com.chwilio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import org.apache.solr.client.solrj.response.FacetField.Count;

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
	private HashSet<String> topics = new HashSet<>(Arrays.asList("politics", "environment", "crime", "infra", "social unrest"));
	
	private int trendingCount = 15;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> searchQuery(String query, String page, Map<String, List<String>> filters) throws SolrServerException, IOException{
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
			String language = " AND (";
			for(String lang : filters.get("langs")) {
				language = language+" lang:"+lang;
			}
			String location = " AND (";
			for(String city : filters.get("cities")) {
				location = location+" city:"+city;
			}
			String dateFilter = "";
			for(String date : filters.get("date")) {
				dateFilter = dateFilter + "tweet_date:[NOW-"+date+"DAYS/DAY TO NOW]";
				
			}
			queryParamMap.put("q",  "(" + tQuery.toString()+")" + language +")"+ location +")");
			queryParamMap.put("fq",dateFilter);
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
			text = document.containsKey("extended_tweet.full_text") ? ((ArrayList<String>) document.getFieldValue("extended_tweet.full_text")).get(0) : ((ArrayList<String>) document.getFieldValue("text")).get(0);
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
		text = document.containsKey("extended_tweet.full_text") ? ((ArrayList<String>) document.getFieldValue("extended_tweet.full_text")).get(0) : ((ArrayList<String>)document.getFieldValue("text")).get(0);
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
	
	
	@Override
	public Map<String, List<String>> getTrendingHashtags(Map<String, List<String>> filters) throws SolrServerException, IOException{
		final SolrClient client = solr.getSolrClient();
			
		final Map<String, String> queryParamMap = new HashMap<String, String>();
		String language = "(";
		for(String lang : filters.get("langs")) {
			language = language+" lang:"+lang;
		}
		String location = " AND (";
		for(String city : filters.get("cities")) {
			location = location+" city:"+city;
		}
		String dateFilter = "";
		for(String date : filters.get("date")) {
			dateFilter = dateFilter + "tweet_date:[NOW-"+date+"DAYS/DAY TO NOW]";
			
		}
		
		queryParamMap.put("q", language +")"+ location +")");
		queryParamMap.put("fq", dateFilter);
		queryParamMap.put("rows", "0");
		queryParamMap.put("facet", "on");
		queryParamMap.put("facet.field", "hashtags");
		MapSolrParams queryParams = new MapSolrParams(queryParamMap);

		final QueryResponse response = client.query("IRF18P4", queryParams);
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		List<String> trendingHashtags = new ArrayList<String>();
		
		int count = 0;
		for (Count c: response.getFacetField("hashtags").getValues()) {		
			if (count++ > trendingCount)
				break;
			trendingHashtags.add(c.getName());
		}
		
		result.put("trendingHashtags", trendingHashtags);
	
		return result;
	}
	
	@Override
	public Map<String, Object> cityWiseTopics(Map<String, List<String>> filters) throws SolrServerException, IOException{
		final SolrClient client = solr.getSolrClient();
		
		MapSolrParams queryParams;
		QueryResponse response;
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Map<String, Long>> cityMap = new HashMap<String, Map<String, Long>>();
		Map<String, Map<String, Long>> sentimentMap = new HashMap<String, Map<String, Long>>();
		Map<String, Map<String, Long>> tsMap = new HashMap<String, Map<String, Long>>();
		
		for (String city: filters.get("city")) {
			Map<String, String> queryParamMap = new HashMap<String, String>();
			queryParamMap.put("q", "city:" + city);
			queryParamMap.put("rows", "0");
			queryParamMap.put("facet", "on");
			queryParamMap.put("facet.field", "topic");
		    queryParams = new MapSolrParams(queryParamMap);
			response = client.query("IRF18P4", queryParams);
			
			for (Count c: response.getFacetField("topic").getValues()) {
				if (!this.topics.contains(c.getName()))
					continue;
				
				Map<String, Long> topicMap;
				if (cityMap.containsKey(city)) {
					topicMap = cityMap.get(city);
					topicMap.put(c.getName(), c.getCount());
					cityMap.put(city, topicMap);
				} else {
					topicMap = new HashMap<String, Long>();
					topicMap.put(c.getName(), c.getCount());
					cityMap.put(city, topicMap);
				}
			}
			
			queryParamMap.put("facet.field", "sentiment");
		    queryParams = new MapSolrParams(queryParamMap);
			response = client.query("IRF18P4", queryParams);
			
			for (Count c: response.getFacetField("sentiment").getValues()) {
				Map<String, Long> map;
				if (sentimentMap.containsKey(city)) {
					map = sentimentMap.get(city);
					map.put(c.getName(), c.getCount());
					sentimentMap.put(city, map);
				} else {
					map = new HashMap<String, Long>();
					map.put(c.getName(), c.getCount());
					sentimentMap.put(city, map);
				}
			}
			
			queryParamMap.put("facet.field", "tweet_date");
		    queryParams = new MapSolrParams(queryParamMap);
			response = client.query("IRF18P4", queryParams);
			
			for (Count c: response.getFacetField("tweet_date").getValues()) {
				Map<String, Long> map;
				if (tsMap.containsKey(city)) {
					map = tsMap.get(city);
					map.put(c.getName(), c.getCount());
					tsMap.put(city, map);
				} else {
					map = new HashMap<String, Long>();
					map.put(c.getName(), c.getCount());
					tsMap.put(city, map);
				}
			}
		}
		
		result.put("city", cityMap);
		result.put("sentiment", sentimentMap);
		result.put("timestamp", tsMap);
		return result;
		
	}
	
	@Override
	public Map<String, Object> getWordCloud() throws SolrServerException, IOException{
		final SolrClient client = solr.getSolrClient();
			
		final Map<String, String> queryParamMap = new HashMap<String, String>();
		queryParamMap.put("q", "*");
		queryParamMap.put("rows", "0");
		queryParamMap.put("facet", "on");
		queryParamMap.put("facet.field", "hashtags");
		MapSolrParams queryParams = new MapSolrParams(queryParamMap);

		final QueryResponse response = client.query("IRF18P4", queryParams);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Long> wordCloud = new HashMap<String, Long>();
		
		for (Count c: response.getFacetField("hashtags").getValues()) {		
			wordCloud.put(c.getName(), c.getCount());
		}
		
		result.put("wordCloud", wordCloud);
	
		return result;
	}
	
}
