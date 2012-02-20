package com.ezmeal.main;

import com.ezmeal.activity.*;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class Main extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.ezmeal.main.R.layout.homepage);
        
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
