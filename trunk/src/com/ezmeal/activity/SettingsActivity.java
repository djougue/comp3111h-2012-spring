package com.ezmeal.activity;

import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	private EditText username;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        username = (EditText) findViewById(R.id.editTextSettingsUserName);
        
        String uname = ((UserApp) this.getApplication()).getUserName();
        username.setText(uname);
    }
}