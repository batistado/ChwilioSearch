package com.chwilio.beans;

import java.util.Date;

public class Tweet {
    private String id;
    private String text;
    private String city;
    private String lang;
    private Date date;
    private String topic;

	public Tweet(String id, String text, String city, String lang, Date date, String topic) {
    	this.id = id;
    	this.text = text;
    	this.city = city;
    	this.lang = lang;
    	this.date = date;
    	this.topic = topic;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
