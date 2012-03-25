package com.ezmeal.activity;

import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener {
	private TextView headerTitle;
	private Button backBtn;
	private EditText username;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        //set the header title
        headerTitle = (TextView) findViewById(R.id.labelHeader);
        headerTitle.setText("Settings");
        
        //back button
        backBtn = (Button) findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(this);
        
        //get user name
        username = (EditText) findViewById(R.id.editTextSettingsUserName);
        String uname = ((UserApp) this.getApplication()).getUserName();
        username.setText(uname);
        
    }
    
    /**
     * Click Listener
     */
    public void onClick(View view) {
    	if (view == backBtn) {
    		finish();
    	}
    }
}