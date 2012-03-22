 /*
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

import com.ezmeal.activity.RegisterActivity;
import com.ezmeal.server.Communication_API;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
 
public class WelcomeActivity extends Activity implements OnClickListener {
	private Button loginBtn, regBtn;
	private TextView resultText;
	private ProgressBar progressBar;
	private Handler refreshHandler = new Handler();
	Thread postDataThread;
	public static boolean isLoginSucc;
	
	//constant strings
	private String INVALID_INPUT = "Invalid user name or password!";
	private String LOADING       = "Loading...";
	private String BLANK_ERROR   = "Both user name and password are required.";
	private String NETWORK_ERROR = "No network connection.";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
      
        //Adjust login window location according to screen dimension
        Display display = getWindowManager().getDefaultDisplay();
        int screenHeight = display.getHeight();
        ImageView logoImage = (ImageView) findViewById(R.id.imageLogo);
        int logoHeight = logoImage.getHeight();
        logoImage.setPadding(0, screenHeight/4-logoHeight/2, 0, 0);
      
        loginBtn = (Button) findViewById(R.id.buttonLogin);
        loginBtn.setOnClickListener(this);
        regBtn = (Button) findViewById(R.id.buttonRegister);
        regBtn.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        resultText = (TextView) findViewById(R.id.labelLoginResult);
      
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
        		} catch (InterruptedException e) {
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


    /**
     * Check the network connectivity.
     * 
     * @return true if network connectivity is fine; other wise false.
     */
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    
	    if (activeNetworkInfo == null) {
	    	resultText.setTextColor(0xffff0000);
		    resultText.setText(NETWORK_ERROR);
		    progressBar.setVisibility(View.INVISIBLE);
		    return false;
	    }
	    return true;
	}
    
    /**
     * Check if the user input is valid.
     * @param username
     * @param password
     * @return true if no error; otherwise false
     */
    private boolean checkInput(String username, String password) {
	    //check if the user name or password is blank
	    if (username.length() == 0 || password.length() == 0) {
	    	resultText.setTextColor(0xffff0000);
		    resultText.setText(BLANK_ERROR);
		    progressBar.setVisibility(View.INVISIBLE);
		    return false;
	    }
	    //check if user name or password contains invalid character (i.e. space)
	    else if (username.indexOf(" ") >= 0 || password.indexOf(" ") >= 0) {
	    	resultText.setTextColor(0xffff0000);
		    resultText.setText(INVALID_INPUT);
		    progressBar.setVisibility(View.INVISIBLE);
		    return false;
	    }
	    return true;
    }

    public void onClick(View view) {
    	if (!isNetworkAvailable())
    		return;
    	
    	if (view == loginBtn) {
    		resultText.setTextColor(0xffffffff);
    		resultText.setText(LOADING);
    		progressBar.setVisibility(View.VISIBLE);
    		
    		//add user name and password
    	    EditText uname = (EditText) findViewById(R.id.editTextUserName);
    	    final String username = uname.getText().toString();

    	    EditText pword = (EditText) findViewById(R.id.editTextPassword);
    	    final String password = pword.getText().toString();
    	    
    	    final Activity thisActivity = this;
    	    
    	    if (checkInput(username, password)) {
    	    	postDataThread = new Thread(new Runnable() {
    	    		public void run() {
    	    			WelcomeActivity.isLoginSucc =
    	    					Communication_API.check_password(username, password);
    	    			
    	    			//Refresh the data of this app
    	    			refreshHandler.post(new Runnable() {
    	    				public void run() {
    	    		    		if (isLoginSucc) {
    	    		    			((UserApp) thisActivity.getApplication()).setUserName(username);
    	    		    			((UserApp) thisActivity.getApplication()).setPassword(password);
    	    		    			
    	    		    			//start the main activity
    	    		    			Intent intent = new Intent();
    	    		    			intent.setClassName("com.ezmeal.main",
    	    		    					"com.ezmeal.main.MainActivity");
    	    		    			startActivity(intent);
    	    		    			
    	    		    			//finish the welcome activity
    	    		    			finish();
    	    		    		} else {
    	    		    			resultText.setTextColor(0xffff0000);
    	    		    			resultText.setText(INVALID_INPUT);
    	    		    		}
    	    		    		progressBar.setVisibility(View.INVISIBLE);
    	    				}
    	    			});
    	    		}
    	    	});
    	    	postDataThread.start();
    	    }
    	} else if (view == regBtn) {
    		Intent intent = new Intent(view.getContext(), RegisterActivity.class);
    		startActivityForResult(intent, 0);
    	}
    }
}

