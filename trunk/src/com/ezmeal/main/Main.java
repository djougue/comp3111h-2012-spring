package com.ezmeal.main;

import com.ezmeal.activity.*;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

public class Main extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.ezmeal.main.R.layout.homepage);
		
		/**
		 * Button activities
		 */
		/* Settings button */
		Button settingsBt = (Button) findViewById(R.id.buttonSettings);
		settingsBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent settingsBtIntent = new Intent(view.getContext(), SettingsActivity.class);
                startActivityForResult(settingsBtIntent, 0);
            }
		});
		
		/* SHAKE button */
		Button shakeBt = (Button) findViewById(R.id.buttonShake);
		shakeBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent shakeBtIntent = new Intent(view.getContext(), ShakeActivity.class);
                startActivityForResult(shakeBtIntent, 0);
            }
		});
		
		/* My Taste button */
		Button myTasteBt = (Button) findViewById(R.id.buttonMyTaste);
		myTasteBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myTasteBtIntent = new Intent(view.getContext(), MyTasteActivity.class);
                startActivityForResult(myTasteBtIntent, 0);
            }
		});
		
		/* Quit button */
		Button quitBt = (Button) findViewById(R.id.buttonQuit);
		quitBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quitBtIntent = new Intent(view.getContext(), QuitActivity.class);
                startActivityForResult(quitBtIntent, 0);
            }
		});
		
		/**
		 * Tab activities
		 */
        Intent tabIntent;               //reusable intent for each tab
        TabHost tabHost = getTabHost(); //the activity TabHost
        TabHost.TabSpec spec;           //reusable TabSpec for each tab
        
        //create an Intent to launch an Activity for the tab (to be reused)
        tabIntent = new Intent().setClass(this, MenuActivity.class);
        //initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("menu").setIndicator("Menu").setContent(tabIntent);
        tabHost.addTab(spec);
        
        //do the same for the other tabs
        tabIntent = new Intent().setClass(this, SearchActivity.class);
        spec = tabHost.newTabSpec("search").setIndicator("Search").setContent(tabIntent);
        tabHost.addTab(spec);

        tabIntent = new Intent().setClass(this, RankingActivity.class);
        spec = tabHost.newTabSpec("ranking").setIndicator("Ranking").setContent(tabIntent);
        tabHost.addTab(spec);
	}
}
