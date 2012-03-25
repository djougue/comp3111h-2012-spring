package com.ezmeal.activity;

import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	private TextView headerTitle, oldpasswdLabel, newpasswdLabel, confirmpasswdLabel;
	private Button backBtn;
	private EditText username, oldpasswd, newpasswd, confirmpasswd;
	private CheckBox changePasswd;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        //set the header
        headerTitle = (TextView) findViewById(R.id.labelHeader);
        headerTitle.setText("Settings");
        backBtn = (Button) findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(this);
        
        changePasswd = (CheckBox) findViewById(R.id.checkBoxSettingsChangePassword);
        changePasswd.setOnCheckedChangeListener(this);
        oldpasswd = (EditText) findViewById(R.id.editTextSettingsOldPassword);
        oldpasswdLabel = (TextView) findViewById(R.id.labelSettingsOldPassword);
        newpasswd = (EditText) findViewById(R.id.editTextSettingsNewPassword);
        newpasswdLabel = (TextView) findViewById(R.id.labelSettingsNewPassword);
        confirmpasswd = (EditText) findViewById(R.id.editTextSettingsConfirmPassword);
        confirmpasswdLabel = (TextView) findViewById(R.id.labelSettingsConfirmPassword);
        DisablePasswd();
        
        //get user name
        username = (EditText) findViewById(R.id.editTextSettingsUserName);
        String uname = ((UserApp) this.getApplication()).getUserName();
        username.setText(uname);
        
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
     * Click Listener
     */
    public void onClick(View view) {
    	if (view == backBtn) {
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