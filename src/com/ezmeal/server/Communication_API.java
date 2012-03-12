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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.util.Log;
import android.widget.Toast;

public class Communication_API {
	static JSONArray jArray;
	static String result = null;
	static InputStream is = null;
	static StringBuilder sb=null;
	
	public void fetch_all_user_information()
	{
	}
	
	private static void send_cmd(String cmd)
	{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		//http post
		try{
		     HttpClient httpclient = new DefaultHttpClient();
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
		       result=sb.toString();
		}
		catch(Exception e)
		{
			Log.e("log_tag", "Error converting result "+e.toString());
		}
	}
	
	private void receive_data()
	{
		/*
		//paring data
		int user_id;
		String user_name;
		String user_password;
		try
		{
		      jArray = new JSONArray(result);
		      JSONObject json_data=null;
		      for(int i=0;i<jArray.length();i++)
		      {
		             json_data = jArray.getJSONObject(i);
		             user_id=json_data.getInt("id");
		             user_name=json_data.getString("name");
		             user_password=json_data.getString("password");
		      }
		}
		catch(JSONException e1)
		{
			Log.e("log_tag", "Error converting result "+e1.toString());
			e1.printStackTrace();
			//Toast.makeText(getBaseContext(), "No User Found" ,Toast.LENGTH_LONG).show();
		} 
		catch (ParseException e1) 
		{
			Log.e("log_tag", "Error parsing result "+e1.toString());
			e1.printStackTrace();
		}
		*/	
	}
	
	public static boolean check_password(String name, String password)
	{
		send_cmd("check_password.php?name="+name+"&password="+password);
		try
		{			  
			  jArray = new JSONArray(result);
			  if (jArray.length()>0)
				 return true;	      
		}
		catch(JSONException e1)
		{
			e1.printStackTrace();
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
			
		return false;
	}
}
