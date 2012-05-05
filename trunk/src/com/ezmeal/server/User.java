package com.ezmeal.server;

public class User {
	private int user_id;
	private String user_name;
	private String user_password;
	private String user_nickname;
	private int user_spicy;
	private int user_vege;
	private int user_meat;
	
	User(){
		user_id=0;
		user_spicy=2;
		user_vege=2;
		user_meat=2;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public String getUser_nickname() {
		return user_nickname;
	}

	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}

	public int isUser_spicy() {
		return user_spicy;
	}

	public void setUser_spicy(int user_spicy) {
		this.user_spicy = user_spicy;
	}

	public int isUser_vege() {
		return user_vege;
	}

	public void setUser_vege(int user_vege) {
		this.user_vege = user_vege;
	}

	public int isUser_meat() {
		return user_meat;
	}

	public void setUser_meat(int user_meat) {
		this.user_meat = user_meat;
	}
	

	
	
}
