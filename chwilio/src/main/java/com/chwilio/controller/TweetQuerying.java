package com.chwilio.controller;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.chwilio.model.*;
import com.chwilio.service.SearchQueryService;

@RestController
public class TweetQuerying {
	@Autowired
	SearchQueryService searchService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/tweets/all")
	@ResponseBody
	public List<Tweet> getAllTweets(@RequestParam("query") String query) throws SolrServerException, IOException {
		return searchService.searchQuery(query);
	}
}
