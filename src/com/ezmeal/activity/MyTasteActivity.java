package com.ezmeal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MyTasteActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView textview = new TextView(this);
        textview.setText("My Taste button testing...");
        setContentView(textview);
    }
}