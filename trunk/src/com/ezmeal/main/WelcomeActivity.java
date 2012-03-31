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

import com.ezmeal.activity.ForgotPasswdActivity;
import com.ezmeal.activity.RegisterActivity;
import com.ezmeal.server.Communication_API;
import com.ezmeal.server.User;

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
	private TextView forgotPasswdLink;
	private TextView resultText;
	private ProgressBar progressBar;
	private Handler refreshHandler;
	private Thread postDataThread;
	public static int serverResp;
	
	//constant strings
	private String INVALID_INPUT = "Invalid user name or password!";
	private String LOADING       = "Loading...";
	private String BLANK_ERROR   = "Both user name and password are required.";
	private String NETWORK_ERROR = "No network connection.";
	private String TIMEOUT       = "Connection timeout. Please try again later.";
	private String NONE          = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        refreshHandler = new Handler();
      
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
        forgotPasswdLink = (TextView) findViewById(R.id.labelForgotPassword);
        forgotPasswdLink.setOnClickListener(this);
        resultText = (TextView) findViewById(R.id.labelLoginResult);
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

    /**
     * Click Listener
     */
    public void onClick(View view) {
    	if (!isNetworkAvailable())
    		return;
    	
    	if (view == loginBtn) {
    		resultText.setTextColor(0xffffffff); //white
    		resultText.setText(LOADING);
    		progressBar.setVisibility(View.VISIBLE);
    		
    		//add user name and password
    	    EditText uname = (EditText) findViewById(R.id.editTextUserName);
    	    final String username = uname.getText().toString();

    	    EditText pword = (EditText) findViewById(R.id.editTextPassword);
    	    final String password = pword.getText().toString();
    	    
    	    final Activity thisActivity = this;
    	    
    	    //Post the login info to the server. Wait for the response from the server.
    	    //This procedure will be executed in the background.
    	    if (checkInput(username, password)) {
    	    	postDataThread = new Thread(new Runnable() {
    	    		public void run() {
    	    			WelcomeActivity.serverResp =
    	    					Communication_API.check_password(username, password);
    	    			
    	    			//Refresh the data of this app
    	    			refreshHandler.post(new Runnable() {
    	    				public void run() {
    	    		    		if (serverResp == -1) {
    	    		    			resultText.setTextColor(0xffff0000); //red
    	    		    			resultText.setText(TIMEOUT);
    	    		    		}
    	    		    		else if (serverResp == 1) {
    	    		    			((UserApp) thisActivity.getApplication()).setUserName(username);
    	    		    			((UserApp) thisActivity.getApplication()).setPassword(password);
    	    		    			
    	    		    			//get and set user info
    	    		    			User user = Communication_API.get_user_info(username, password);
    	    		    			((UserApp) thisActivity.getApplication()).setNickName(user.getUser_nickname());
    	    		    			boolean[] taste = {user.isUser_spicy(), user.isUser_meat(), user.isUser_vege()};
    	    		    			((UserApp) thisActivity.getApplication()).setTaste(taste);
    	    		    			((UserApp) thisActivity.getApplication()).setShake(true);
    	    		    			
    	    		    			//start the main activity
    	    		    			Intent intent = new Intent();
    	    		    			intent.setClassName("com.ezmeal.main", "com.ezmeal.main.MainActivity");
    	    		    			startActivity(intent);
    	    		    			
    	    		    			//finish the welcome activity (this)
    	    		    			finish();
    	    		    		} else {
    	    		    			resultText.setTextColor(0xffff0000); //red
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
    		resultText.setText(NONE);   //Clear the result text
    	} else if (view == forgotPasswdLink) {
			Intent intent = new Intent(view.getContext(), ForgotPasswdActivity.class);
    		startActivityForResult(intent, 0);
    		resultText.setText(NONE);   //Clear the result text
		}
    }
}

