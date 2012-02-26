package com.ezmeal.main;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
 
public class WelcomeActivity extends Activity {
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.welcome);
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
   }
}