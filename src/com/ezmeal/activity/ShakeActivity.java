package com.ezmeal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezmeal.main.R;
import com.ezmeal.server.Communication_API;
import com.ezmeal.server.Dish;
import com.ezmeal.shake.ShakeListener;

public class ShakeActivity extends Activity implements OnClickListener{
	private Communication_API api = new Communication_API();
	private TextView headerTitle;
	private Button backBtn;

	private String dish_name="Connection failed";
	private String dish_canteen=" ";
	private String dish_price=" ";
	private int shake_state = 0;
	Dish the_dish;
    private Thread shakeThread;
    Handler shakeHandler=new Handler();
	private ProgressBar progressBar;
	private LinearLayout view;
	ShakeListener mShaker;
	ImageView dishImage;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.shake_activity_layout);

		progressBar = (ProgressBar) findViewById(R.id.progressBarMenu);

        headerTitle = (TextView) findViewById(R.id.labelHeader);
        headerTitle.setText("SHAKE");
        backBtn = (Button) findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(this);

		
		Button reshakeBt = (Button) findViewById(com.ezmeal.main.R.id.reshake);
		reshakeBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	reshake();
            }
		});

/*		
		mShaker = new ShakeListener(this);		
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {  
		    public void onShake() {  
		    	reshake();
		    }  
		});
*/
		dishImage = (ImageView) findViewById(com.ezmeal.main.R.id.dishImageButton);

		dishImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if(the_dish != null){
	    			Intent intent = new Intent(getApplicationContext(),
	    					com.ezmeal.activity.DetailActivity.class);
	    			
	    			Bundle dishInfo = new Bundle();
	    			dishInfo.putString("name", dish_name);
	    			dishInfo.putString("pic", " ");
	    			dishInfo.putString("price", dish_price);
	    			dishInfo.putString("canteen", dish_canteen);
	    			dishInfo.putInt("ActivityFlag", 1);
	    			intent.putExtras(dishInfo);
	    			startActivity(intent);
            	}
    		}
		});
        
		shakeThread = new Thread(new Runnable() {
			public void run() {
				while(true){
					if(shake_state==0){
						the_dish = api.random_dish();
						
						if(the_dish!=null){
							dish_name = the_dish.getDish_name();
							dish_canteen = the_dish.getDish_canteen();
							dish_price = "$"+Float.toString(the_dish.getDish_price());
							shake_state = 2;
						}
						else{
							dish_name="Connection failed";
							dish_canteen=" ";
							dish_price=" ";
							shake_state = 2;
						}
						
						shakeHandler.post(new Runnable() {
							public void run() {
								if(shake_state == 2){
									TextView name=(TextView)findViewById(com.ezmeal.main.R.id.textDishNameInShake);	
									TextView canteen=(TextView)findViewById(com.ezmeal.main.R.id.textDishCanteenInShake);
									TextView price=(TextView)findViewById(com.ezmeal.main.R.id.textDishPriceInShake);
									name.setText(dish_name);
									canteen.setText(dish_canteen);
									price.setText(dish_price);
									
									
									view=(LinearLayout)findViewById(com.ezmeal.main.R.id.information);	
									progressBar.setVisibility(View.INVISIBLE);
									view.setVisibility(View.VISIBLE);
									shake_state = 1;
								}
							}
						});
					}
				}
	  		}});
		shakeThread.start();
		
    }

    protected void onPause() {
//    	mShaker.pause();
    	super.onPause();
    	shake_state = 1;
    	finish();
    }
    
    @Override
	protected void onResume(){
//		mShaker.resume();
		super.onResume();
	}

   
    
    void reshake(){
    	if(shake_state == 1){
    		view.setVisibility(View.INVISIBLE);
    		progressBar.setVisibility(View.VISIBLE);
    		shake_state = 0;
    	}
    	else 
    		return;
    }

    /**
     * Click Listener
     */
    public void onClick(View view) {
    	if (view == backBtn) {
    		finish();
    	}
    }
        
}