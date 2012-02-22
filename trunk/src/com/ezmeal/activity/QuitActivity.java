package com.ezmeal.activity;

/**
 * QuitActivity
 * 
 * This activity leads the program to quit.
 * 
 * Author: ZHENG, Zichen
 */

import com.ezmeal.main.Main;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class QuitActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //quit the program but not terminate
        Intent quit = new Intent(Intent.ACTION_MAIN);
        quit.addCategory(Intent.CATEGORY_HOME);
        quit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(quit);
        
        //back to the home page
        Intent homePageIntent = new Intent().setClass(this, Main.class);
        startActivityForResult(homePageIntent, 0);
    }
}