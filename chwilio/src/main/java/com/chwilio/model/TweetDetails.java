package com.chwilio.model;

import java.util.Date;

public class TweetDetails extends Tweet {
    private String city;
    private String lang;
    private String topic;
    private String tweetUrl;

	public TweetDetails(String id, String text, String city, String lang, Date date, String topic, String username, String tweetUrl, String userProfileImage) {
    	super(id, text, date, username, userProfileImage);
    	this.city = city;
    	this.lang = lang;
    	this.topic = topic;
    	this.tweetUrl = tweetUrl;
    }

	public String getTweetUrl() {
		return tweetUrl;
	}

	public void setTweetUrl(String tweetUrl) {
		this.tweetUrl = tweetUrl;
	}

	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
