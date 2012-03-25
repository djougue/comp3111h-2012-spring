package com.ezmeal.server;

public class Dish {
	private int dish_id;
	private String dish_name;
	private String dish_canteen;
	
	Dish(){
		dish_id=0;
	}
	
	public int getDish_id() {
		return dish_id;
	}
	public void setDish_id(int dish_id) {
		this.dish_id = dish_id;
	}
	public String getDish_name() {
		return dish_name;
	}
	public void setDish_name(String dish_name) {
		this.dish_name = dish_name;
	}
	public String getDish_canteen() {
		return dish_canteen;
	}
	public void setDish_canteen(String dish_canteen) {
		this.dish_canteen = dish_canteen;
	}
	
	
}
