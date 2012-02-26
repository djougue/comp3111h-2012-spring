package com.ezmeal.activity;

import com.ezmeal.main.MainActivity;
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
        Intent homePageIntent = new Intent().setClass(this, MainActivity.class);
        startActivityForResult(homePageIntent, 0);
    }
}