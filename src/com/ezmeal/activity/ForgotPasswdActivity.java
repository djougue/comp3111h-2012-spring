package com.ezmeal.activity;

import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;
import com.ezmeal.main.WelcomeActivity;
import com.ezmeal.server.Communication_API;

import android.app.Activity;
import android.content.Intent;
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
	private Button backBtn, submitBtn;
	private TextView headerTitle, remarks;
	private EditText username;
	private RelativeLayout resetPasswdLayout;
	private boolean isRequestSent;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_passwd);
        
        //set page header
        backBtn = (Button) findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(this);
        headerTitle = (TextView) findViewById(R.id.labelHeader);
        headerTitle.setText("Forgot Password");
        
        //set the text for user name edit box
        username = (EditText) findViewById(R.id.editTextForgotPasswdUserName);
        String uname = ((UserApp) this.getApplication()).getTmpUserName();
        username.setText(uname);
        
        //set remarks
        remarks = (TextView) findViewById(R.id.labelForgotPasswdRemarks);
        
        //set the submit button
        submitBtn = (Button) findViewById(R.id.buttonForgotPasswdSubmit);
        submitBtn.setOnClickListener(this);
        
        //reset password area should be invisible
        resetPasswdLayout = (RelativeLayout) findViewById(R.id.relativeLayoutForgotPasswdReset);
        resetPasswdLayout.setVisibility(View.GONE);
        
        //initialize other variables
        isRequestSent = false;
    }
	
    public void onClick(View view) {
    	if (view == backBtn) {
    		//Finish the activity, and go back to the welcome page.
    		finish();
    	}
    	else if (view == submitBtn) {
    		if (isRequestSent) {
    			//TODO
    		}
    		else {
    			//TODO
    			resetPasswdLayout.setVisibility(View.VISIBLE);
    			remarks.setText("A confirmation code has been sent to your HKUST mailbox.");
    			username.setEnabled(false);
    		}
    	}
    }
}

