package com.ezmeal.activity;

import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyTasteActivity extends Activity implements OnClickListener {
	private TextView headerTitle;
	private Button backBtn;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytaste);
        
        //set the header title
        headerTitle = (TextView) findViewById(R.id.labelHeader);
        headerTitle.setText("My Taste");
        
        //back button
        backBtn = (Button) findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(this);
        
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