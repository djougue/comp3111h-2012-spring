package com.ezmeal.server;

public class User {
	private int user_id;
	private String user_name;
	private String user_password;
	private String user_nickname;
	private boolean user_spicy;
	private boolean user_vege;
	private boolean user_meat;
	
	User(){
		user_id=0;
		user_spicy=false;
		user_vege=false;
		user_meat=false;
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

	public boolean isUser_spicy() {
		return user_spicy;
	}

	public void setUser_spicy(boolean user_spicy) {
		this.user_spicy = user_spicy;
	}

	public boolean isUser_vege() {
		return user_vege;
	}

	public void setUser_vege(boolean user_vege) {
		this.user_vege = user_vege;
	}

	public boolean isUser_meat() {
		return user_meat;
	}

	public void setUser_meat(boolean user_meat) {
		this.user_meat = user_meat;
	}
	

	
	
}
