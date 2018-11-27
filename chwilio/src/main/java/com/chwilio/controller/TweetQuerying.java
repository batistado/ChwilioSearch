package com.chwilio.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/tweets/all", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> getAllTweets(@RequestParam("query") String query, @RequestParam("page") String page) throws SolrServerException, IOException {
		return new ResponseEntity<Map<String,Object>>(searchService.searchQuery(query, page), HttpStatus.OK);
	}
}
