package com.chwilio.model;

import java.util.Date;

public class Tweet {
	private String id;
    private String text;
    private Date date;
    private String username;
    private String userProfileImage;
    
    public Tweet(String id, String text, Date date, String username, String userProfileImage) {
		this.id = id;
		this.text = text;
    	this.date = date;
    	this.username = username;
    	this.userProfileImage = userProfileImage;
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
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserProfileImage() {
		return userProfileImage;
	}
	public void setUserProfileImage(String userProfileImage) {
		this.userProfileImage = userProfileImage;
	}
}
