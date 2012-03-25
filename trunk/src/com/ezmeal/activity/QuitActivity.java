package com.ezmeal.activity;

import android.app.Activity;
import android.os.Bundle;

public class QuitActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //quit the program but not terminate
        finish();
    }
}
