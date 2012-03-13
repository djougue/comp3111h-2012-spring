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
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
 
public class WelcomeActivity extends Activity implements OnClickListener {
	private Button loginBtn, regBtn;
	private TextView resultText;
	private String INVALID_INPUT = "Invalid user name or password!";
	private String LOADING = "Loading...";
	private String BLANK_ERROR = "Both user name and password are required.";
	
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
     * Post the login data (username and password) to the server
     */
    private void postLoginData() {
    	//add user name and password
	    EditText uname = (EditText) findViewById(R.id.editTextUserName);
	    String username = uname.getText().toString();

	    EditText pword = (EditText) findViewById(R.id.editTextPassword);
	    String password = pword.getText().toString();
	   
	    //check if the user name or password is blank
	    if (username.length() == 0 || password.length() == 0) {
		    resultText.setTextColor(0xffff0000);
		    resultText.setText(BLANK_ERROR);
	    }
	    //check if user name or password contains invalid character (i.e. space)
	    else if (username.indexOf(" ") >= 0 || password.indexOf(" ") >= 0) {
		    resultText.setTextColor(0xffff0000);
		    resultText.setText(INVALID_INPUT);
	    }
	    //check on the server side
	    else if (Communication_API.check_password(username, password)) {
		    Intent intent = new Intent();
		    intent.setClassName("com.ezmeal.main", "com.ezmeal.main.MainActivity");
		    resultText.setText("");
		    startActivity(intent);
		    finish(); //finish the welcome activity
	    } else {
	    	resultText.setTextColor(0xffff0000);
	    	resultText.setText(INVALID_INPUT);
	    }
    }

    public void onClick(View view) {
    	if (view == loginBtn) {
    		resultText.setTextColor(0xffffffff);
    		resultText.setText(LOADING);
    		postLoginData();
    	} else {
    		Intent intent = new Intent(view.getContext(), RegisterActivity.class);
    		startActivityForResult(intent, 0);
    	}
    }
}

