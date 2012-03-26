package com.ezmeal.activity;

import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;
import com.ezmeal.main.WelcomeActivity;
import com.ezmeal.server.Communication_API;
import com.ezmeal.server.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	private TextView headerTitle, oldpasswdLabel, newpasswdLabel, confirmpasswdLabel, resultText;
	private Button backBtn, submitBtn;
	private EditText username, nickname, oldpasswd, newpasswd, confirmpasswd;
	private CheckBox changePasswd;
	private ToggleButton shakeBtn;
	private ProgressBar progressBar;
	private Handler refreshHandler;
	private Thread postDataThread;
	private static int serverResp;
	
	//messages for result
	private String EMPTY_PASSWD        = "Password is required.";
	private String EMPTY_NICKNAME      = "Nick name is required.";
	private String SHORT_PASSWD        = "The length of password should be no less than 6 characters.";
	private String LONG_PASSWD         = "The length of password should be no more than 20 characters.";
	private String SPACE_IN_PASSWD     = "No space is allowed in password.";
	private String INCONSISTENT_PASSWD = "The confirmed password is inconsistent.";
	private String SPACE_IN_NICKNAME   = "No space is allowed in nickname";
	private String WRONG_OLD_PASSWD    = "Your old password is not correct.";
	private String TIMEOUT             = "Connection error. Please try again later.";
	private String LOADING             = "Loading...";
	private String SUCCESSFUL          = "You have successfully changed your settings.";
	private String ERROR_MSG           = "Error occurred to settings modification.";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        //set the header
        headerTitle = (TextView) findViewById(R.id.labelHeader);
        headerTitle.setText("Settings");
        backBtn = (Button) findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(this);
        submitBtn = (Button) findViewById(R.id.buttonSettingsSubmit);
        submitBtn.setOnClickListener(this);
        
        changePasswd = (CheckBox) findViewById(R.id.checkBoxSettingsChangePassword);
        changePasswd.setOnCheckedChangeListener(this);
        oldpasswd = (EditText) findViewById(R.id.editTextSettingsOldPassword);
        oldpasswdLabel = (TextView) findViewById(R.id.labelSettingsOldPassword);
        newpasswd = (EditText) findViewById(R.id.editTextSettingsNewPassword);
        newpasswdLabel = (TextView) findViewById(R.id.labelSettingsNewPassword);
        confirmpasswd = (EditText) findViewById(R.id.editTextSettingsConfirmPassword);
        confirmpasswdLabel = (TextView) findViewById(R.id.labelSettingsConfirmPassword);
        DisablePasswd();
        
        shakeBtn = (ToggleButton) findViewById(R.id.toggleButtonSettingsShake);
        boolean isShake = ((UserApp) this.getApplication()).isShake();
        shakeBtn.setChecked(isShake);
        
        refreshHandler = new Handler();
        progressBar = (ProgressBar) findViewById(R.id.progressBarSettings);
        resultText = (TextView) findViewById(R.id.labelSettingsResult);
        
        //get user name and nickname
        username = (EditText) findViewById(R.id.editTextSettingsUserName);
        String uname = ((UserApp) this.getApplication()).getUserName();
        username.setText(uname);
        nickname = (EditText) findViewById(R.id.editTextSettingsNickName);
        String nname = ((UserApp) this.getApplication()).getNickName();
        nickname.setText(nname);
    }
    
    private void EnablePasswd() {
    	oldpasswd.setEnabled(true);  //editable
    	oldpasswdLabel.setTextColor(0xff000000); //black
    	newpasswd.setEnabled(true);  //editable
    	newpasswdLabel.setTextColor(0xff000000); //black
    	confirmpasswd.setEnabled(true);  //editable
    	confirmpasswdLabel.setTextColor(0xff000000); //black
    }
    
    private void DisablePasswd() {
    	oldpasswd.setEnabled(false);  //not editable
    	oldpasswdLabel.setTextColor(0xffcccccc); //grey
    	newpasswd.setEnabled(false);  //not editable
    	newpasswdLabel.setTextColor(0xffcccccc); //grey
    	confirmpasswd.setEnabled(false);  //not editable
    	confirmpasswdLabel.setTextColor(0xffcccccc); //grey
    }
    
    /**
     * Check if the user input is valid.
     */
    private boolean checkInput(String passwd,String confPasswd, String nname) {
    	//check if the username field is empty
    	resultText.setTextColor(0xffff0000);  //red color
    	
    	if (changePasswd.isChecked()) {
    	    //check if the password field is empty
    	    if (passwd.length() == 0) {
    		    resultText.setText(EMPTY_PASSWD);
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
        	//check if confirmed password is consistent
        	else if (!passwd.equals(confPasswd)) {
        		resultText.setText(INCONSISTENT_PASSWD);
        		return false;
        	}
    	}
    	
    	//check if the nick name field is empty
    	if (nname.length() == 0) {
    		resultText.setText(EMPTY_NICKNAME);
    		return false;
    	}

    	//check if the nickname contains any space(s)
    	else if (nname.indexOf(" ") >= 0) {
    		resultText.setText(SPACE_IN_NICKNAME);
    		return false;
    	}
    	return true;
    }
    
    /**
     * Post the settings form to the server.
     */
    private void postSettingsData() {
    	//get input
    	final String uname = username.getText().toString();
    	final String nname = nickname.getText().toString();
    	final String oldpwd, newpwd, cpwd;
    	final boolean[] taste = ((UserApp) this.getApplication()).getTaste();
    	final Activity thisActivity = this;
    	final boolean isShake = shakeBtn.isChecked();
    	
    	if (changePasswd.isChecked()) {
    		oldpwd = oldpasswd.getText().toString();
        	newpwd = newpasswd.getText().toString();
        	cpwd = confirmpasswd.getText().toString();
    	}
    	else {
    		oldpwd = ((UserApp) this.getApplication()).getPassword();
        	newpwd = ((UserApp) this.getApplication()).getPassword();
        	cpwd = ((UserApp) this.getApplication()).getPassword();
    	}
    	
    	if (checkInput(newpwd, cpwd, nname)) {
    		postDataThread = new Thread(new Runnable() {
	    		public void run() {
	    			SettingsActivity.serverResp =
	    					Communication_API.change_user_setting(uname, newpwd, nname, taste[0]?1:0, taste[2]?1:0, taste[1]?1:0);
	    			
	    			//Refresh the data of this app
	    			refreshHandler.post(new Runnable() {
	    				public void run() {
	    				    if(serverResp == 0) {
	    				    	resultText.setTextColor(0xffffffff); //white
	    		    			resultText.setText(TIMEOUT);
	    				    }
	    					else if (serverResp == 1) {
	    		    			resultText.setTextColor(0xffffffff); //white
	    		    			resultText.setText(SUCCESSFUL);
	    		    			
	    		    			//change local data
	    		    			((UserApp) thisActivity.getApplication()).setPassword(newpwd);
	    		    			((UserApp) thisActivity.getApplication()).setNickName(nname);
	    		    			((UserApp) thisActivity.getApplication()).setShake(isShake);
	    		    			
	    		    		} else {
	    		    			resultText.setTextColor(0xffff0000); //red
	    		    			resultText.setText(ERROR_MSG);
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
    
    private void checkPasswd() {
    	postDataThread = new Thread(new Runnable() {
    		public void run() {
    			SettingsActivity.serverResp =
    					Communication_API.check_password(username.getText().toString(), oldpasswd.getText().toString());
    			
    			//Refresh the data of this app
    			refreshHandler.post(new Runnable() {
    				public void run() {
    		    		if (serverResp == -1) {
    		    			resultText.setTextColor(0xffff0000); //red
    		    			resultText.setText(TIMEOUT);
    		    		}
    		    		else if (serverResp == 1) {
    		    			postSettingsData();
    		    		} else {
    		    			resultText.setTextColor(0xffff0000); //red
    		    			resultText.setText(WRONG_OLD_PASSWD);
    		    		}
    		    		progressBar.setVisibility(View.GONE);
    				}
    			});
    		}
    	});
    	postDataThread.start();
    }
    
    /**
     * Click Listener
     */
    public void onClick(View view) {
        if (view == submitBtn) {
        	resultText.setTextColor(0xffffffff); //white
    		resultText.setText(LOADING);
    		progressBar.setVisibility(View.VISIBLE);
        	if (changePasswd.isChecked()) {
        		checkPasswd();
        	}
        	else {
        		postSettingsData();
        	}
        }
    	else if (view == backBtn) {
    		finish();
    	}
    }

    /**
     * Checked Changed Listener
     */
	public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
		if (btn == changePasswd) {
			if (isChecked) {
				EnablePasswd();
			}
			else {
				DisablePasswd();
			}
		}
	}
}