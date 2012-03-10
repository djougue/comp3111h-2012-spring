package com.ezmeal.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ezmeal.main.R;

public class DetailActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity_layout);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String s = extras.getString("value");
			//TextView view = (TextView) findViewById(R.id.detailsText);
			//view.setText(s);
		}

	}
}