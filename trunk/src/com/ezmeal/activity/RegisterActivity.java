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

package com.ezmeal.activity;

import com.ezmeal.main.R;
import com.ezmeal.server.Communication_API;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RegisterActivity extends Activity implements OnClickListener, TextWatcher {
	private Button submitBtn, backBtn;
	private TextView resultText, headerTitle;
	private EditText uname, pwd, conPwd, nname;
	private ProgressBar progressBar;
	private Handler refreshHandler;
	private Thread postDataThread;
	private static int serverResp;
	private boolean isChanged = false;
	
	//messages for result
	private String EMPTY_USERNAME      = "Your ITSC account is required.";
	private String EMPTY_PASSWD        = "Password is required.";
	private String EMPTY_NICKNAME      = "Nick name is required.";
	private String INVALID_ITSC        = "The ITSC is invalid. Have you entered Space,' or \"?";
	private String SHORT_PASSWD        = "The length of password should be no less than 6 characters.";
	private String LONG_PASSWD         = "The length of password should be no more than 20 characters.";
	private String SPACE_IN_PASSWD     = "No space is allowed in password.";
	private String INCONSISTENT_PASSWD = "The confirmed password is inconsistent.";
	private String SPACE_IN_NICKNAME   = "No space is allowed in nickname";
	private String EXISTED_ITSC        = "You have already signed up.";
	private String TIMEOUT             = "Connection error. Please try again later.";
	private String LOADING             = "Loading...";
	private String INVALID_PASSWD	   = "You can't use \' or \" in your password. "; 
	private String INVALID_NICKNAME	   = "You can't use \' or \" in your nickname. "; 
	private String SUCCESSFUL          = "You registration is successful.";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        progressBar = (ProgressBar) findViewById(R.id.progressBarRegister);
        refreshHandler = new Handler();
        
        //edit text
        uname = (EditText) findViewById(R.id.editTextRegUserName);
        uname.addTextChangedListener(this);
        pwd = (EditText) findViewById(R.id.editTextRegPassword);
        pwd.addTextChangedListener(this);
        conPwd = (EditText) findViewById(R.id.editTextRegConfirmPassword);
        conPwd.addTextChangedListener(this);
        nname = (EditText) findViewById(R.id.editTextRegNickName);
        nname.addTextChangedListener(this);
        
        headerTitle = (TextView) findViewById(R.id.labelHeader);
        headerTitle.setText("Sign Up");
        submitBtn = (Button) findViewById(R.id.buttonSubmitRegForm);
        submitBtn.setOnClickListener(this);
        backBtn = (Button) findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(this);
        resultText = (TextView) findViewById(R.id.labelRegResult);
    }
    
    /**
     * Check if the user input is valid.
     */
    private boolean checkInput(String uname, String passwd,String confPasswd,
    		String nname) {
    	//check if the username field is empty
    	resultText.setTextColor(0xffff0000);  //red
    	if (uname.length() == 0) {
    		resultText.setText(EMPTY_USERNAME);
    		return false;
    	}
    	//check if the password field is empty
    	else if (passwd.length() == 0) {
    		resultText.setText(EMPTY_PASSWD);
    		return false;
    	}
    	//check if the nick name field is empty
    	else if (nname.length() == 0) {
    		resultText.setText(EMPTY_NICKNAME);
    		return false;
    	}
    	//check if the username contains any space(s)
    	else if (uname.indexOf(" ") >= 0) {
    		resultText.setText(INVALID_ITSC);
    		return false;
    	}
    	else if (uname.indexOf("'") >= 0) {
    		resultText.setText(INVALID_ITSC);
    		return false;
    	}
    	else if (uname.indexOf("\"") >= 0) {
    		resultText.setText(INVALID_ITSC);
    		return false;
    	}
    	//check if the password is too short
    	else if (passwd.length() < 6) {
    		resultText.setText(SHORT_PASSWD);
    		return false;
    	}
    	//check if the password is too long
    	else if (passwd.length() > 20) {
    		resultText.setText(LONG_PASSWD);
    		return false;
    	}
    	//check if the password contains any space(s)
    	else if (passwd.indexOf(" ") >= 0) {
    		resultText.setText(SPACE_IN_PASSWD);
    		return false;
    	}
    	//check if the password contains any space(s)
    	else if (passwd.indexOf("'") >= 0) {
    		resultText.setText(INVALID_PASSWD);
    		return false;
    	}
    	//check if the password contains any space(s)
    	else if (passwd.indexOf("\"") >= 0) {
    		resultText.setText(INVALID_PASSWD);
    		return false;
    	}
    	//check if confirmed password is consistent
    	else if (!passwd.equals(confPasswd)) {
    		resultText.setText(INCONSISTENT_PASSWD);
    		return false;
    	}
    	//check if the nickname contains any space(s)
    	else if (nname.indexOf(" ") >= 0) {
    		resultText.setText(SPACE_IN_NICKNAME);
    		return false;
    	}
    	//check if the nickname contains any space(s)
    	else if (nname.indexOf("'") >= 0) {
    		resultText.setText(INVALID_NICKNAME);
    		return false;
    	}
    	//check if the nickname contains any space(s)
    	else if (nname.indexOf("\"") >= 0) {
    		resultText.setText(INVALID_NICKNAME);
    		return false;
    	}
    	resultText.setTextColor(0xffffffff);  //white
    	return true;
    }
    
    /**
     * Post the registration form to the server.
     */
    private void postRegData() {
    	//get input
    	final String username = uname.getText().toString();
    	final String password = pwd.getText().toString();
    	final String confirmedPassword = conPwd.getText().toString();
    	final String nickname = nname.getText().toString();
    	
    	if (checkInput(username, password, confirmedPassword, nickname)) {
    		postDataThread = new Thread(new Runnable() {
	    		public void run() {
	    			RegisterActivity.serverResp =
	    					Communication_API.register(username, password, nickname);
	    			
	    			//Refresh the data of this app
	    			refreshHandler.post(new Runnable() {
	    				public void run() {
	    		    		if (serverResp == -1) {
	    		    			resultText.setTextColor(0xffff0000); //red
	    		    			resultText.setText(TIMEOUT);
	    		    		}
	    		    		else if (serverResp == 1) {
	    		    			//finish the register activity (this)
	    		    			resultText.setTextColor(0xffffffff); //white
	    		    			resultText.setText(SUCCESSFUL);
	    		    			submitBtn.setEnabled(false);  //cannot re-submit
	    		    			
	    		    			//stay on the register page for 1 seconds
	    		    			new Thread() {
	    		    	        	@Override
	    		    	        	public void run() {
	    		    	        		try {
	    		    	        			int waited = 0;
	    		    	        			while (waited < 1000) {
	    		    	        				sleep(100);
	    		    	        				waited += 100;
	    		    	        			}
	    		    	        		} catch (InterruptedException e) {
	    		    	               //do nothing
	    		    	        		}
	    		    	        		
	    		    	        		//finish the register activity
	    		    	        		finally {
	    		    	        			finish();
	    		    	        		}
	    		    	        	}
	    		    	        }.start();
	    		    	        
	    		    		} else {
	    		    			resultText.setTextColor(0xffff0000); //red
	    		    			resultText.setText(EXISTED_ITSC);
	    		    		}
	    		    		progressBar.setVisibility(View.GONE);
	    				}
	    			});
	    		}
	    	});
	    	postDataThread.start();
    	}
    	else {
    		progressBar.setVisibility(View.GONE);
    	}
    }
    
    public void onClick(View view) {
    	if (view == submitBtn) {
    		resultText.setTextColor(0xffffffff); //white
    		resultText.setText(LOADING);
    		progressBar.setVisibility(View.VISIBLE);
    		postRegData();
    	}
    	else if (view == backBtn) {
    		//Pop up an alert dialog if something has been modified
    		if (isChanged) {
    			final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    			alertDialog.setTitle("Alert!");
    			alertDialog.setMessage("Are you sure to exit this page?");
    			alertDialog.setButton("Yes, please.", new DialogInterface.OnClickListener() {
    			      public void onClick(DialogInterface dialog, int which) {
    			    	  finish();
    			    } });
    			alertDialog.setButton2("Oops, NO!", new DialogInterface.OnClickListener() {
  			      public void onClick(DialogInterface dialog, int which) {
  			    	  alertDialog.dismiss();
  			        } });
    			alertDialog.show();
    		}
    		
    		//Else, directly finish the activity, and go back to the welcome page.
    		else {
    		    finish();
    		}
    	}
    }

	public void afterTextChanged(Editable ed) {
		isChanged = true;
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// Nothing	
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// Nothing
	}
}
