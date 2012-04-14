package com.ezmeal.server;

public enum Available_Time{
	Breakfast, Lunch, Tea, Dinner, All;
	
	@Override public String toString(){		
		//Only capitalize the first letter
		String s=super.toString();
		return s.substring(0,1) + s.substring(1).toLowerCase();
	}
}

