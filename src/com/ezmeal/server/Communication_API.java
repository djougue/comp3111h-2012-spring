package com.ezmeal.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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
	JSONArray jArray;
	String result = null;
	InputStream is = null;
	StringBuilder sb=null;
	int timeoutConnection = 12000;
	int timeoutSocket = 12000;
		
	private void send_cmd(String cmd)
	{
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

	private void send_cmd_ihome(String destination, String cmd)
	{
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

		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
		
		send_cmd_ihome("xlu","register.php?name="+name+"&password="+password+"&nickname="+nickname);
			
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
			
		return 0;
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
	
	/*******************************************************************************************
	 * 									Methods Related to Dishes
	 ******************************************************************************************/
	public Dish fetch_dish(int index)
	{
		Dish dish=new Dish();
		result = null;
		send_cmd("fetch_dish.php");
		try
		{
			if(result==null) return null;
		     jArray = new JSONArray(result);
		     JSONObject json_data=null;

             json_data = jArray.getJSONObject(index);
             dish.setDish_id(json_data.getInt("dish_id"));
             dish.setDish_name(json_data.getString("dish_name"));
             dish.setDish_canteen(json_data.getString("dish_canteen"));
             dish.setDish_price(Float.valueOf(json_data.getString("dish_price")).floatValue());
             dish.setDish_spicy(json_data.getInt("dish_spicy"));
             dish.setDish_vege(json_data.getInt("dish_vege"));
             dish.setDish_meat(json_data.getInt("dish_meat"));
             dish.setDish_available_time(json_data.getString("dish_available_time"));
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
             dish.setDish_available_time(json_data.getString("dish_available_time"));
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
}
