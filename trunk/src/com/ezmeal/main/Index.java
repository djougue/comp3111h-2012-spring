package com.ezmeal.main;

import com.ezmeal.widget.*;
import android.app.Activity;
import android.os.Bundle;

public class Index extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Use XML layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
    }
}