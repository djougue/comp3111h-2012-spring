package com.ezmeal.main;

public class Utility {
	
	/**
	 * Check if the input contains invalid characters such as spaces, single and double
	 * quotation marks.
	 * @param str = the input in String format
	 * @return true if there is no invalid characters; otherwise, false.
	 */
	static public boolean checkInput(String str) {
        if (str.indexOf(" ") >= 0
        		|| str.indexOf("'") >= 0
        		|| str.indexOf("\"") >= 0) {
    		return false;
    	}
    	return true;
	}
}