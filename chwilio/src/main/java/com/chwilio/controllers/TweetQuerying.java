package com.chwilio.controllers;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chwilio.beans.*;
import com.chwilio.config.*;
import com.chwilio.resources.TweetResource;

@Controller
public class TweetQuerying {
	@RequestMapping(method = RequestMethod.GET, value = "/tweets/all")
	@ResponseBody
	public List<Tweet> getAllStudents(@RequestParam("query") String query) throws SolrServerException, IOException {
		return TweetResource.searchQuery(query);
	}
}
