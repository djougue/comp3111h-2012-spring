package com.ezmeal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShakeActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView textview = new TextView(this);
        textview.setText("SHAKE button testing...");
        setContentView(textview);
    }
}