package com.ezmeal.activity;

import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;
import com.ezmeal.main.Utility;
import com.ezmeal.server.Communication_API;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ForgotPasswdActivity extends Activity implements OnClickListener {
	private Communication_API api = new Communication_API();
	private Button backBtn, submitBtn;
	private TextView headerTitle, remarks, resultText;
	private EditText username, confirmCode, password, confirmedPassword;
	private RelativeLayout resetPasswdLayout;
	private boolean isRequestSent;
	private ProgressBar progressBar;
	private Handler refreshHandler = new Handler();
	private Thread postDataThread;
	private static int serverResp;
	
	//messages for result
	private String EMPTY_USERNAME      = "The user name cannot be empty.";
	private String INVALID_ITSC        = "The ITSC is invalid. Have you entered spaces?";
	private String EMPTY_CODE          = "Confirmation code cannot be empty.";
	private String INVALID_CODE        = "Confirmation code contains invalid characters.";
	private String INVALID_PASSWD      = "Password contains invalid charachters.";
	private String SHORT_PASSWD        = "The length of password should be no less than 6 characters.";
	private String LONG_PASSWD         = "The length of password should be no more than 20 characters.";
	private String INCONSISTENT_PASSWD = "The confirmed password is inconsistent.";
	private String TIMEOUT             = "Connection timeout. Please try again later.";
	private String USER_NOT_EXISTED    = "The user does not exist.";
	private String WRONG_CONFIRM_CODE  = "The confirmation code is invalid";
	private String LOADING             = "Loading...";
	private String SUCCESSFUL          = "You successfully reset your password.";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_passwd);
        
        //set page header
        backBtn = (Button) findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(this);
        headerTitle = (TextView) findViewById(R.id.labelHeader);
        headerTitle.setText("Forgot Password");
        
        //set the edit text box
        username = (EditText) findViewById(R.id.editTextForgotPasswdUserName);
        String uname = ((UserApp) this.getApplication()).getTmpUserName();
        username.setText(uname);
        confirmCode = (EditText) findViewById(R.id.editTextForgotPasswdConfirmationCode);
        password = (EditText) findViewById(R.id.editTextForgotPasswdNewPassword);
        confirmedPassword = (EditText) findViewById(R.id.editTextForgotPasswdConfirmPassword);
        
        //set remarks
        remarks = (TextView) findViewById(R.id.labelForgotPasswdRemarks);
        
        //set result text
        resultText = (TextView) findViewById(R.id.labelForgotPasswdResult);
        
        //set progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBarForgotPasswd);
        
        //set the submit button
        submitBtn = (Button) findViewById(R.id.buttonForgotPasswdSubmit);
        submitBtn.setOnClickListener(this);
        
        //reset password area should be invisible
        resetPasswdLayout = (RelativeLayout) findViewById(R.id.relativeLayoutForgotPasswdReset);
        resetPasswdLayout.setVisibility(View.GONE);
        
        //initialize other variables
        isRequestSent = false;
    }
	
	/**
	 * Check if the form of the user name is correct.
	 * @param uname = user name
	 * @return true if the user name is in a correct format.
	 */
	private boolean checkUserName(String uname) {
		resultText.setTextColor(0xffff0000);  //red, for error
		//check if the user name field is empty
    	if (uname.length() == 0) {
    		resultText.setText(EMPTY_USERNAME);
    		return false;
    	}
    	//check if the user name contains any invalid character(s)
    	else if (!Utility.checkInput(uname)) {
    		resultText.setText(INVALID_ITSC);
    		return false;
    	}
    	resultText.setTextColor(0xffffffff); //white
		return true;
	}
	
	/**
	 * Check if the input format is correct for the confirmation code and password.
	 * @param code = confirmation code
	 * @param pwd = new password
	 * @param conpwd = confirmed password
	 * @return true if the input format is correct.
	 */
	private boolean checkPassword(String code, String pwd, String conpwd) {
		resultText.setTextColor(0xffff0000);  //red, for error
		//check if the confirmation code field is empty
    	if (code.length() == 0) {
    		resultText.setText(EMPTY_CODE);
    		return false;
    	}
    	//check if the confirmation code contains any invalid character(s)
    	else if (!Utility.checkInput(code)) {
    		resultText.setText(INVALID_CODE);
    		return false;
    	}
    	//check if the password is too short
    	else if (pwd.length() < 6) {
    		resultText.setText(SHORT_PASSWD);
    		return false;
    	}
    	//check if the password is too long
    	else if (pwd.length() > 20) {
    		resultText.setText(LONG_PASSWD);
    		return false;
    	}
    	//check if the password contains any invalid character(s)
    	else if (!Utility.checkInput(pwd)) {
    		resultText.setText(INVALID_PASSWD);
    		return false;
    	}
    	//check if confirmed password is consistent
    	else if (!pwd.equals(conpwd)) {
    		resultText.setText(INCONSISTENT_PASSWD);
    		return false;
    	}
    	resultText.setTextColor(0xffffffff); //white
		return true;
	}
	
	/**
	 * Apply for a confirmation code.
	 */
	private void applyConfirmationCode() {
		//get input
    	final String uname = username.getText().toString();
    	
    	if (checkUserName(uname)) {
    		postDataThread = new Thread(new Runnable() {
	    		public void run() {
	    			serverResp = api.get_confirmcode(uname);
	    			
	    			//Refresh the data of this app
	    			refreshHandler.post(new Runnable() {
	    				public void run() {
	    		    		if (serverResp == 0) {
	    		    			resultText.setTextColor(0xffff0000); //red
	    		    			resultText.setText(TIMEOUT);
	    		    		}
	    		    		else if (serverResp == 1) {
	    		    			resultText.setText("");
	    		    			resetPasswdLayout.setVisibility(View.VISIBLE);
	    		    			remarks.setText("A confirmation code has been sent to your HKUST mailbox.");
	    		    			username.setEnabled(false);
	    		    			isRequestSent = true;
	    		    		} else {
	    		    			resultText.setTextColor(0xffff0000); //red
	    		    			resultText.setText(USER_NOT_EXISTED);
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
	
	/**
	 * Send the new password to the server.
	 */
	private void resetPassword() {
		//get input
    	final String uname = username.getText().toString();
    	final String code = confirmCode.getText().toString();
    	final String pwd = password.getText().toString();
    	final String conpwd = confirmedPassword.getText().toString();
    	
    	if (checkPassword(code, pwd, conpwd)) {
    		postDataThread = new Thread(new Runnable() {
	    		public void run() {
	    			ForgotPasswdActivity.serverResp = api.reset_password(uname, code, pwd);
	    			
	    			//Refresh the data of this app
	    			refreshHandler.post(new Runnable() {
	    				public void run() {
	    		    		if (serverResp == 0) {
	    		    			resultText.setTextColor(0xffff0000); //red
	    		    			resultText.setText(TIMEOUT);
	    		    		}
	    		    		else if (serverResp == -2) {
	    		    			resultText.setTextColor(0xffff0000); //red
	    		    			resultText.setText(WRONG_CONFIRM_CODE);
	    		    		}
	    		    		else {
	    		    			//finish the register activity (this)
	    		    			resultText.setTextColor(0xffffffff); //white
	    		    			resultText.setText(SUCCESSFUL);
	    		    			submitBtn.setEnabled(false);  //cannot re-submit
	    		    			
	    		    			//stay on the register page for 2 seconds
	    		    			new Thread() {
	    		    	        	@Override
	    		    	        	public void run() {
	    		    	        		try {
	    		    	        			int waited = 0;
	    		    	        			while (waited < 2000) {
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
	
	/**
	 * On click listener
	 */
    public void onClick(View view) {
    	/** Back Button **/
    	if (view == backBtn) {
    		//Pop up an alert dialog if confirmation code has been sent
    		if (isRequestSent) {
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
    	
    	/** Submit Button **/
    	else if (view == submitBtn) {
    		resultText.setTextColor(0xffffffff); //white
    		resultText.setText(LOADING);
    		progressBar.setVisibility(View.VISIBLE);
    		if (isRequestSent) {
    			resetPassword();
    		}
    		else {
    			applyConfirmationCode();
    		}
    	}
    }
}

