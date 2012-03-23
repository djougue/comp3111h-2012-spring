package com.ezmeal.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.ezmeal.main.R;
import com.ezmeal.shake.ShakeListener;

public class DetailActivity extends FragmentActivity {

	ShakeListener mShaker;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity_layout);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int integer = extras.getInt("value");
			TextView view = (TextView) findViewById(R.id.detailsText);
			view.setText("This is dish "+ integer);
		}
		
		mShaker = new ShakeListener(this);
		
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {  
		    public void onShake() {  
				TextView view = (TextView) findViewById(R.id.detailsText);
				view.setText("Shaking");
		    }  
		});
	}

	@Override
	protected void onPause(){
		mShaker.pause();
		super.onPause();
	}
	
	@Override
	protected void onResume(){
		mShaker.resume();
		super.onResume();
	}

	
	
	
}