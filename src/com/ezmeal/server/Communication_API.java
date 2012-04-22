package com.ezmeal.server;

import java.io.BufferedReader;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.util.Log;

public class Communication_API {
	private JSONArray jArray;
    private String result = null;
	private InputStream is = null;
	private StringBuilder sb=null;
	private int timeoutConnection = 10000;  //timeout = 10 seconds
	private int timeoutSocket = 10000;
	private boolean isTimeout = false;
	
	//For debugging
	static String testing;
		
	/**
	 * Send requests to and fetch results from the ezMeal main server
	 * @param cmd
	 */
	private void send_cmd(String cmd)
	{
		isTimeout = false;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		//http post
		try{
			//set time out
		     HttpParams httpParameters = new BasicHttpParams();
			 HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			 HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			 HttpClient httpclient = new DefaultHttpClient(httpParameters);
		     HttpPost httppost = new HttpPost("http://143.89.220.19/COMP3111H/"+cmd);
		     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		     HttpResponse response = httpclient.execute(httppost);
		     HttpEntity entity = response.getEntity();
		     is = entity.getContent();
		}
		catch (ConnectTimeoutException e)
		{
			Log.e("log_tag", "Error in http connection"+e.toString());
			isTimeout = true;
		}
		catch (SocketTimeoutException e)
		{
			Log.e("log_tag", "Error in http connection"+e.toString());
			isTimeout = true;
		}
		catch(Exception e)
		{
		     Log.e("log_tag", "Error in http connection"+e.toString());
		}
		//convert response to string
		try{
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		    sb = new StringBuilder();
		    sb.append(reader.readLine() + "\n");
		       
		       String line="0";
		       while ((line = reader.readLine()) != null) 
		       {
		       	sb.append(line + "\n");
		       }
		       is.close();
		       result = new String();
		       result=sb.toString();
		}
		catch(Exception e)
		{
			Log.e("log_tag", "Error converting result "+e.toString());
		}
	}

	/**
	 * Send requests to and fetch results from the HKUST ihome server
	 * @param destination
	 * @param cmd
	 */
	private void send_cmd_ihome(String destination, String cmd)
	{
		isTimeout = false;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		//http post
		try{
			//set time out
		     HttpParams httpParameters = new BasicHttpParams();
			 HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			 HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			 
			 HttpClient httpclient = new DefaultHttpClient(httpParameters);
			 HttpPost httppost;
			 
			 //Choose to send to Lu's or Tu's ihome
			 if (destination=="xlu")
				 httppost = new HttpPost("http://ihome.ust.hk/~xlu/cgi-bin/"+cmd);
			 else
				 httppost = new HttpPost("http://ihome.ust.hk/~jtu/cgi-bin/"+cmd);
			 
		     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		     HttpResponse response = httpclient.execute(httppost);
		     HttpEntity entity = response.getEntity();
		     is = entity.getContent();
		}
		catch (ConnectTimeoutException e)
		{
			Log.e("log_tag", "Error in http connection"+e.toString());
			isTimeout = true;
		}
		catch (SocketTimeoutException e)
		{
			Log.e("log_tag", "Error in http connection"+e.toString());
			isTimeout = true;
		}
		catch(Exception e)
		{
		     Log.e("log_tag", "Error in http connection"+e.toString());
		}
		//convert response to string
		try{
		      BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		       sb = new StringBuilder();
		       sb.append(reader.readLine() + "\n");
		
		       String line="0";
		       while ((line = reader.readLine()) != null) 
		       {
		       	sb.append(line + "\n");
		       }
		       is.close();
		       result = new String();
		       result=sb.toString();
		}
		catch(Exception e)
		{
			Log.e("log_tag", "Error converting result "+e.toString());
		}
	}
	
	/**
	 * @return true if connection is timeout
	 */
	public boolean isConnectionTimeout() {
		return isTimeout;
	}
	/*******************************************************************************************
	 * 									Methods Related to Users
	 ******************************************************************************************/

	//if register successfully, return 1
	//if the user_name is already inside the database, return -2
	//if unknown error occurs, return -1
	public int register(String name, String password, String nickname)
	{
		result = null;
		send_cmd("fetch_user_info.php?name="+name);
		try
		{
			  //Check whether there exists users with such user_name
			  if(result != null){
				  jArray = new JSONArray(result);
				  if (jArray.length()>0)
					  return -2;//User with the same user_name already exists	      
			  }
			  else
				  return 0;  //timeout

		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
		
		//send_cmd_ihome("xlu","register.php?name="+name+"&password="+password+"&nickname="+nickname);
		//need testing
		send_cmd_ihome("jtu","signup_ac.php?name="+name+"&password="+password+"&nickname="+nickname);
		
		return 1;
	}
	
	//if the password is correct, return 1
	//else return -1
	public int check_password(String name, String password)
	{
		result = null;
		
		send_cmd("check_password.php?name="+name.trim()+"&password="+password.trim());
		try
		{
			  if(result != null){
				  jArray = new JSONArray(result);
				  if (jArray.length()>0)
				 return 1;	      
			  }
			  else
				  return -1;
		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
			
		return -1;
	}
	
	//if change setting successfully, return 1
	//if the user_name does not exist inside the database, return -2
	//if unknown error occurs, return -1
	public int change_user_setting(String name, String password, String nickname, int spicy, int vege, int meat)
	{
		result = null;
		send_cmd("fetch_user_info.php?name="+name);
		try
		{
			  //Check whether there exists users with such user_name
			  if(result != null){
				  jArray = new JSONArray(result);
				  if (jArray.length()<=0)
					  return -2;//User with the same user_name does not exist    
			  }

		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
		
		send_cmd("change_user_setting.php?name="+name+"&password="+password+"&nickname="
				+nickname+"&spicy="+spicy+"&vege="+vege+"&meat="+meat);
		return check_password(name, password);
	}
	
	public User get_user_info(String name, String password)
	{
		User user=new User();
		send_cmd("fetch_user_info.php?name="+name);
		try
		{
			jArray = new JSONArray(result);
		    JSONObject json_data=null;

            json_data = jArray.getJSONObject(0);
            user.setUser_id(json_data.getInt("user_id"));
            user.setUser_name(json_data.getString("user_name"));
            user.setUser_nickname(json_data.getString("user_nickname"));
            user.setUser_spicy((json_data.getInt("user_spicy")==1)?true:false);
            user.setUser_vege((json_data.getInt("user_vege")==1)?true:false);
            user.setUser_meat((json_data.getInt("user_meat")==1)?true:false);

		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
		      
        return user;
	}
	

	/**
	 * Get confirmation code from the server for Forgot Password function.
	 * @param name = user name
	 * @return  1 if successful
	 *          0 if timeout
	 *         -1 if unknown error occurs
	 *         -2 if user name does not exist
	 */
	public int get_confirmcode(String name)
	{
		result = null;
		
		//Check whether there exists users with such user_name
		send_cmd("fetch_user_info.php?name="+name);
		try
		{
			  if(result != null){
				  jArray = new JSONArray(result);
				  if (jArray.length()<=0)
					  return -2; //user does not exist
			  }
			  else
				  return 0;  //timeout

		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
			return -2;  //user does not exist
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
			return -1;  //unknown error
		}
		send_cmd_ihome("jtu","get_confirmcode.php?name="+name);
		return 1;
	}
	
	/**
	 * Send the server a new password to reset.
	 * @param name = user name
	 * @param confirm_code = confirmation code
	 * @param passwd = new password
	 * @return  1 if successful
	 *          0 if timeout
	 *         -2 if confirmation code is invalid 
	 */
	public int reset_password(String name, String confirm_code, String passwd){
		result = null;
		send_cmd_ihome("jtu","conf_confirmcode.php?name="+name + "&passkey="+confirm_code + "&passwd="+passwd);
		if (result == null)
			return 0;
		else if (result.length() < 5)
			return -2;
		return 1;
	}
	 
	
	/*******************************************************************************************
	 * 									Methods Related to Dishes
	 ******************************************************************************************/
	public Dish fetch_dish(int index)
	{
		Dish dish=new Dish();
		result = null;
		send_cmd("fetch_dish.php?index="+index);
		try
		{
			if(result==null) return null;
		     jArray = new JSONArray(result);
		     JSONObject json_data=null;

             json_data = jArray.getJSONObject(0);
             dish.setDish_id(json_data.getInt("dish_id"));
             dish.setDish_name(json_data.getString("dish_name"));
             dish.setDish_canteen(json_data.getString("dish_canteen"));
             dish.setDish_price(Float.valueOf(json_data.getString("dish_price")).floatValue());
             dish.setDish_spicy(json_data.getInt("dish_spicy"));
             dish.setDish_vege(json_data.getInt("dish_vege"));
             dish.setDish_meat(json_data.getInt("dish_meat"));
             dish.setDish_available_time(json_data.getInt("dish_available_time"));
		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
		return dish;
	}
	
	public Dish random_dish()
	{
		
		Dish dish=new Dish();
		result = null;
		send_cmd("random_dish.php");
		try
		{
			if(result==null) return null;
		     jArray = new JSONArray(result);
		     JSONObject json_data=null;

             json_data = jArray.getJSONObject(0);
             dish.setDish_id(json_data.getInt("dish_id"));
             dish.setDish_name(json_data.getString("dish_name"));
             dish.setDish_canteen(json_data.getString("dish_canteen"));
             dish.setDish_price(Float.valueOf(json_data.getString("dish_price")).floatValue());
             dish.setDish_spicy(json_data.getInt("dish_spicy"));
             dish.setDish_vege(json_data.getInt("dish_vege"));
             dish.setDish_meat(json_data.getInt("dish_meat"));
             dish.setDish_image(json_data.getInt("dish_image"));
             dish.setDish_available_time(json_data.getInt("dish_available_time"));
		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
		return dish;
	}
	
	/*
	 * 	put dish_name="any" if do not want to search by name
	 *  put dish_canteen="any" if do not want to search by canteen
	 *  put dish_spicy=2 if do not want to search by dish_spicy
	 *	put dish_vege=2 if do not want to search by dish_vege
	 *	put dish_meat=2 if do not want to search by dish_meat
	 */
		
	public Dish search_dish(int index, String dish_name, 
			boolean LG7, boolean LG5, boolean Mc, boolean LG1, boolean G, boolean Cafe, boolean CF,
			int dish_spicy, int dish_vege, int dish_meat, //dish taste
			boolean Dinner, boolean Tea, boolean Lunch, boolean Breakfast //dish available time
			)
	
	{
		Dish dish=new Dish();
		result = null;
		String cmd="search_dish.php?index="+index+"&";
		boolean first=false; //indicate whether this is the first parameter to be passed
		boolean condition_specified=false; 	//indicate whether any condition has been specified for the search
										  	//if no condition is specified, use fetch_dish()
		
		//search by name
		if (dish_name=="any")
		{
			//do nothing
		}
		else
		{
			try{
				String name=URLEncoder.encode(dish_name,"utf-8"); 
				cmd+="name="+name;
				first=false;
				condition_specified=true;
			}
			catch(Exception e){
			}
		}
		
		//search by canteen
		int canteen=0;
		
		if (LG7)
			canteen |= 1;
		if (LG5)
			canteen |= 2;
		if (Mc)
			canteen |= 4;
		if (LG1)
			canteen |= 8;
		if (G)
			canteen |= 16;
		if (Cafe)
			canteen |= 32;
		if (CF)
			canteen |= 64;
		
		if ((canteen!=127) && (canteen!=0))//not select all, nor deselect all
		{
			cmd+="&canteen="+canteen;
			condition_specified=true;
		}
		
		//search by spicy
		if (dish_spicy<2)
		{
			cmd+="&spicy="+dish_spicy;
			condition_specified=true;
		}
		else
		{
			//do nothing
		}
		
		//search by vege
		if (dish_vege<2)
		{
			cmd+="&vege="+dish_vege;
			condition_specified=true;
		}
		else
		{
			//do nothing
		}
		
		//search by meat
		if (dish_meat<2)
		{
			cmd+="&meat="+dish_meat;
			condition_specified=true;
		}
		else
		{
			//do nothing
		}
		
		//search by available time
		int time=0;
		
		if (Dinner)
			time |= 1;
		if (Tea)
			time |= 2;
		if (Lunch)
			time |= 4;
		if (Breakfast)
			time |= 8;
		
		if ((time!=15) && (time!=0))//not select all, nor deselect all
		{
			cmd+="&time="+time;
			condition_specified=true;
		}
		
		//if any condition has been speficied, send out the request
		//else use fetch_dish()
		if (!condition_specified)
			cmd="fetch_dish.php?index="+index;			
		
		//for debugging
		testing=cmd;
		
		send_cmd(cmd);
		
		try
		{
			if(result==null) return null;
		     jArray = new JSONArray(result);
		     JSONObject json_data=null;

             json_data = jArray.getJSONObject(0);
             dish.setDish_id(json_data.getInt("dish_id"));
             dish.setDish_name(json_data.getString("dish_name"));
             dish.setDish_canteen(json_data.getString("dish_canteen"));
             dish.setDish_price(Float.valueOf(json_data.getString("dish_price")).floatValue());
             dish.setDish_spicy(json_data.getInt("dish_spicy"));
             dish.setDish_vege(json_data.getInt("dish_vege"));
             dish.setDish_meat(json_data.getInt("dish_meat"));
             dish.setDish_image(json_data.getInt("dish_image"));
             dish.setDish_available_time(json_data.getInt("dish_available_time"));
		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
		return dish;
	}
	

	/*******************************************************************************************
	 * 									Methods Related to Comments
	 ******************************************************************************************/
	public void add_comment(String title, String content, String user_name, String dish_name)
	{
		try{
			send_cmd("add_comment.php?title="+URLEncoder.encode(title,"utf-8")
					+"&content="+URLEncoder.encode(content,"utf-8")
					+"&author="+URLEncoder.encode(user_name,"utf-8")
					+"&dish="+URLEncoder.encode(dish_name,"utf-8"));
		}
		catch(Exception e){
			
		}
	}
	
	public Comment fetch_comment(int index, String dish)
	{
		Comment comment=new Comment();
		result = null;

		try{
			send_cmd("fetch_comment.php?index=" + index + "&dish="+URLEncoder.encode(dish,"utf-8"));
		}
		catch(Exception e){
			//do nothing
		}
		
		try
		{
			if(result==null) return null;
		    jArray = new JSONArray(result);
		    JSONObject json_data=null;
            json_data = jArray.getJSONObject(0);
            /*
            comment.setDish_id(json_data.getInt("dish_id"));
            dish.setDish_name(json_data.getString("dish_name"));
            dish.setDish_canteen(json_data.getString("dish_canteen"));
            dish.setDish_price(Float.valueOf(json_data.getString("dish_price")).floatValue());
            dish.setDish_spicy(json_data.getInt("dish_spicy"));
            dish.setDish_vege(json_data.getInt("dish_vege"));
            dish.setDish_meat(json_data.getInt("dish_meat"));
            dish.setDish_available_time(json_data.getInt("dish_available_time"));
            */
            comment.setTitle(json_data.getString("comment_title"));
            comment.setContent(json_data.getString("comment_content"));
            comment.setAuthor(json_data.getString("comment_author_name"));
            comment.setTime(json_data.getString("comment_time"));
		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
			return null;
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
			return null;
		}
		return comment;
	}
	
	/*******************************************************************************************
	 * 									Methods Related to Blacklist
	 ******************************************************************************************/
	public void add_blacklist(String user_name, String dish_name)
	{
		try{
			send_cmd("add_blacklist.php?"
					+"&username="+URLEncoder.encode(user_name,"utf-8")
					+"&dishname="+URLEncoder.encode(dish_name,"utf-8"));
		}
		catch(Exception e){
			
		}
	}
	
	//Return all the dishes that are on the blacklist of a particular user
	//Attention: The returned dish has only information of the dish_name
	public Dish fetch_blacklist(int index, String user_name)
	{
		Dish dish=new Dish();
		result = null;

		try{
			testing="fetch_blacklist.php?index=" + index 
					+ "&username=" + URLEncoder.encode(user_name,"utf-8");
			
			send_cmd("fetch_blacklist.php?index=" + index 
					+ "&username=" + URLEncoder.encode(user_name,"utf-8"));
			
		}
		catch(Exception e){
			
		}
		
		try
		{
			if(result==null) return null;
		     jArray = new JSONArray(result);
		     JSONObject json_data=null;

             json_data = jArray.getJSONObject(0);
             
             dish.setDish_name(json_data.getString("blacklist_dishname"));
		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
		return dish;
	}
	
	/*******************************************************************************************
	 * 									Methods Related to Ratings
	 ******************************************************************************************/
	//Add the rate if the user has not rated this dish before
	//Change the rating value if he/she has rated it
	public void rate_dish(String user_name, String dish_name, int rate)
	{
		try{
			send_cmd("rate_dish.php?"
					+"&username="+URLEncoder.encode(user_name,"utf-8")
					+"&dishname="+URLEncoder.encode(dish_name,"utf-8")
					+"&rate="+rate);
		}
		catch(Exception e){
			
		}
	}
	
	//Return the average rate of a dish
	//Return -1 if error occurs
	public float fetch_rate(String dish_name)
	{
		result = null;

		try{
			send_cmd("fetch_rate.php?" + "dishname=" + URLEncoder.encode(dish_name,"utf-8"));
			
		}
		catch(Exception e){
			
		}
		
		try
		{
			if(result==null) return -1;
		     jArray = new JSONArray(result);
		     JSONObject json_data=null;

             json_data = jArray.getJSONObject(0);
             
             return(Float.valueOf(json_data.getString("avg(rate_value)")).floatValue());
		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return -1;
	}
	
	
	//Return the rate of a dish from a particular user
	//Return -1 if error occurs
	public int fetch_user_rate(String user_name, String dish_name)
	{
		result = null;

		try{
			testing="fetch_user_rating.php?" 	
					  +	"username=" + URLEncoder.encode(user_name,"utf-8")
					  + "&dishname=" + URLEncoder.encode(dish_name,"utf-8");
			send_cmd("fetch_user_rating.php?" 	
					  +	"username=" + URLEncoder.encode(user_name,"utf-8")
					  + "&dishname=" + URLEncoder.encode(dish_name,"utf-8"));
			
		}
		catch(Exception e){
			
		}
		
		try
		{
			if(result==null) return -1;
			testing=result;
		     jArray = new JSONArray(result);
		     JSONObject json_data=null;

             json_data = jArray.getJSONObject(0);
             return(json_data.getInt("rate_value"));
		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return -1;
	}
	
	//Return the rate of a dish from a particular user
	//Return -1 if error occurs
	public int fetch_number_of_rater(String dish_name)
	{
		result = null;

		try{
			testing="fetch_number_rater.php?dishname=" + URLEncoder.encode(dish_name,"utf-8");
			send_cmd("fetch_number_rater.php?dishname=" + URLEncoder.encode(dish_name,"utf-8"));
			
		}
		catch(Exception e){
			
		}
		
		try
		{
			if(result==null) return -1;
			testing=result;
		     jArray = new JSONArray(result);
		     JSONObject json_data=null;

             json_data = jArray.getJSONObject(0);
             return(json_data.getInt("count(*)"));
		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
		return -1;
	}
}
