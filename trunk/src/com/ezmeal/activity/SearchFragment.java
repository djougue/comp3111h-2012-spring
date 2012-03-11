package com.ezmeal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SearchFragment extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("Search tab testing...");
        setContentView(textview);
    }
}