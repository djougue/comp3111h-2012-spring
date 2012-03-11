/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ezmeal.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
 
public class WelcomeActivity extends Activity implements OnClickListener {
	Button loginBtn, regBtn;
	TextView resultText;
	String SERVER_ADDR = "http://www.sencide.com/blog";
	
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.welcome);
      
      //Adjust login window location according to screen dimension
      Display display = getWindowManager().getDefaultDisplay();
      int screenHeight = display.getHeight();
      ImageView logoImage = (ImageView) findViewById(R.id.imageViewLogo);
      int logoHeight = logoImage.getHeight();
      logoImage.setPadding(0, screenHeight/4-logoHeight/2, 0, 0);
      
      loginBtn = (Button) findViewById(R.id.buttonLogin);
      loginBtn.setOnClickListener(this);
      
      resultText = (TextView) findViewById(R.id.textViewResult);
      
      /*
      //Stay on the welcome page for 5 seconds
      Thread splashThread = new Thread() {
         @Override
         public void run() {
        	//display the welcome image for seconds
            try {
               int waited = 0;
               while (waited < 5000) {
                  sleep(100);
                  waited += 100;
               }
            }
            catch (InterruptedException e) {
               //do nothing
            } 
            
            //then start the main activity of ezMeal (i.e. jump to the home page)
            finally {
               finish();
               Intent intent = new Intent();
               intent.setClassName("com.ezmeal.main",
                              "com.ezmeal.main.MainActivity");
               startActivity(intent);
            }
         }
      };
      splashThread.start();
      */
   }
   
   public void postLoginData() {
       // Create a new HttpClient and Post Header
       HttpClient httpclient = new DefaultHttpClient();
       
       /* login.php returns true if username and password is found to the database */
       HttpPost httppost = new HttpPost(SERVER_ADDR + "/login.php");

       try {
           // Add user name and password
    	   EditText uname = (EditText)findViewById(R.id.editTextUserName);
           String username = uname.getText().toString();

           EditText pword = (EditText)findViewById(R.id.editTextPassword);
           String password = pword.getText().toString();
         
           List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
           nameValuePairs.add(new BasicNameValuePair("username", username));
           nameValuePairs.add(new BasicNameValuePair("password", password));
           httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

           // Execute HTTP Post Request
           Log.w("EZMEAL", "Execute HTTP Post Request");
           HttpResponse response = httpclient.execute(httppost);
            
           String str = inputStreamToString(response.getEntity().getContent()).toString();
           Log.w("EZMEAL", str);
            
           if(str.toString().equalsIgnoreCase("true")) {
        	   Log.w("EZMEAL", "TRUE");
        	   Intent intent = new Intent();
        	   intent.setClassName("com.ezmeal.main", "com.ezmeal.main.MainActivity");
        	   startActivity(intent);
           } else {
        	   Log.w("EZMEAL", "FALSE");
        	   resultText.setTextColor(0xffff0000);
        	   resultText.setText("Login failed!");
           }

       } catch (ClientProtocolException e) {
    	   e.printStackTrace();
       } catch (IOException e) {
    	   e.printStackTrace();
       }
   } 
  
   private StringBuilder inputStreamToString(InputStream is) {
	   String line = "";
	   StringBuilder total = new StringBuilder();
	   //Wrap a BufferedReader around the InputStream
	   BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	   //Read response until the end
	   try {
		   while ((line = rd.readLine()) != null)
			   total.append(line);
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
	   //Return full string
	   return total;
   }

   public void onClick(View view) {
	   if(view == loginBtn) {
		   resultText.setTextColor(0xffffffff);
		   resultText.setText("Loading...");
		   postLoginData();
	   }
   }
}
