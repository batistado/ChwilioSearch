package com.chwilio.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chwilio.beans.*;
import com.chwilio.resources.TweetResource;

@Controller
public class TweetQuerying {
	@RequestMapping(method = RequestMethod.GET, value = "/tweets/all")
	@ResponseBody
	public List<Tweet> getAllStudents(@RequestParam("query") String query) throws SolrServerException, IOException {
		return new TweetResource().searchQuery(query);
	}
}
