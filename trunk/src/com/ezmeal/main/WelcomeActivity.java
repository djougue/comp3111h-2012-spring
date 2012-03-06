package com.ezmeal.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
 
public class WelcomeActivity extends Activity {
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.welcome);
      
      //Adjust login window location according to screen dimension
      Display display = getWindowManager().getDefaultDisplay();
      int screenHeight = display.getHeight();
      ImageView logoImage = (ImageView) findViewById(R.id.imageViewLogo);
      int logoHeight = logoImage.getHeight();
      logoImage.setPadding(0, screenHeight/4-logoHeight/2, 0, 0);
      
      Button loginBtn = (Button) findViewById(R.id.buttonLogin);
      loginBtn.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
          Intent loginBtnIntent = new Intent(view.getContext(), MainActivity.class);
          startActivityForResult(loginBtnIntent, 0);
          }
      });
      
      /*
      Thread splashThread = new Thread() {
         @Override
         public void run() {
        	//display the welcome image for seconds
            try {
               int waited = 0;
               while (waited < 5000) {
                  sleep(100);
                  waited += 100;
               }
            }
            catch (InterruptedException e) {
               //do nothing
            } 
            
            //then start the main activity of ezMeal (i.e. jump to the home page)
            finally {
               finish();
               Intent intent = new Intent();
               intent.setClassName("com.ezmeal.main",
                              "com.ezmeal.main.MainActivity");
               startActivity(intent);
            }
         }
      };
      splashThread.start();
      */
   }
}
