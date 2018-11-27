package com.chwilio.model;

import java.util.Date;

public class TweetDetails extends Tweet {
    private String city;
    private String lang;
    private Date date;
    private String topic;
    private String username;
    private String tweetUrl;
    private String userProfileImage;

	public TweetDetails(String id, String text, String city, String lang, Date date, String topic, String username, String tweetUrl, String userProfileImage) {
    	super(id, text);
    	this.city = city;
    	this.lang = lang;
    	this.date = date;
    	this.topic = topic;
    	this.username = username;
    	this.tweetUrl = tweetUrl;
    	this.userProfileImage = userProfileImage;
    }

	public String getUserProfileImage() {
		return userProfileImage;
	}

	public void setUserProfileImage(String userProfileImage) {
		this.userProfileImage = userProfileImage;
	}

	public String getTweetUrl() {
		return tweetUrl;
	}

	public void setTweetUrl(String tweetUrl) {
		this.tweetUrl = tweetUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
