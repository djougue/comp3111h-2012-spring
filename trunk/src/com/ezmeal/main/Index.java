package com.ezmeal.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;

public class Index extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Use XML layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        
        Button login = (Button) findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Main.class);
                startActivityForResult(myIntent, 0);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
            }
        });
    }
    /**
     * Pause Activity
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}