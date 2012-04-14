package com.ezmeal.server;

public class Dish {
	private int dish_id;
	private String dish_name;
	private String dish_canteen;
	private float dish_price;
	private boolean dish_spicy;
	private boolean dish_vege;
	private boolean dish_meat;
	
	Dish(){
		dish_id=0;
		dish_name=null;
		dish_canteen=null;
		dish_price=0;
		dish_spicy=false;
		dish_vege=false;
		dish_meat=false;
		dish_available_time=Available_Time.Dinner;
	}
	
	public boolean isDish_spicy() {
		return dish_spicy;
	}

	public void setDish_spicy(boolean dish_spicy) {
		this.dish_spicy = dish_spicy;
	}
	
	public void setDish_spicy(int dish_spicy) {
		this.dish_spicy = (dish_spicy!=0)?true:false;
	}

	public boolean isDish_vege() {
		return dish_vege;
	}

	public void setDish_vege(boolean dish_vege) {
		this.dish_vege = dish_vege;
	}
	
	public void setDish_vege(int dish_vege) {
		this.dish_vege = (dish_vege!=0)?true:false;
	}

	public boolean isDish_meat() {
		return dish_meat;
	}

	public void setDish_meat(boolean dish_meat) {
		this.dish_meat = dish_meat;
	}
	
	public void setDish_meat(int dish_meat) {
		this.dish_meat = (dish_meat!=0)?true:false;
	}
	
	private Available_Time dish_available_time;
	
	public Available_Time getDish_available_time() {
		return dish_available_time;
	}

	public void setDish_available_time(Available_Time dish_available_time) {
		this.dish_available_time = dish_available_time;
	}
	
	public void setDish_available_time(String s) {
		if (s=="Breakfast")
			this.dish_available_time = Available_Time.Breakfast;
		else if (s=="Lunch")
			this.dish_available_time = Available_Time.Lunch;
		else if (s=="Tea")
			this.dish_available_time = Available_Time.Tea;
		else if (s=="Dinner")
			this.dish_available_time = Available_Time.Dinner;
		else 
			this.dish_available_time = Available_Time.All;
	}
	
	public float getDish_price() {
		return dish_price;
	}

	public void setDish_price(float dish_price) {
		this.dish_price = dish_price;
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
