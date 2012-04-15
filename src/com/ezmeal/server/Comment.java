package com.ezmeal.server;

import java.sql.*;
import java.text.SimpleDateFormat;

public class Comment {
	private String title;
	private String content;
	private String author;
	private String dish;
	private Timestamp time;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDish() {
		return dish;
	}
	public void setDish(String dish) {
		this.dish = dish;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public void setTime(String timeString) {
		Timestamp ts=Timestamp.valueOf(timeString);
		this.time = ts;
	}

}
