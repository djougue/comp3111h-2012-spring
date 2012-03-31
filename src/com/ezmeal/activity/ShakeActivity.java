package com.ezmeal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezmeal.main.R;
import com.ezmeal.server.Communication_API;
import com.ezmeal.server.Dish;
import com.ezmeal.shake.ShakeListener;

public class ShakeActivity extends Activity implements OnClickListener{
	private TextView headerTitle;
	private Button backBtn;

	private String dish_name="fail";
	private String dish_canteen="fail";
	private String dish_price="fail";
	private int shake_state = 0;
	Dish the_dish;
    private Thread shakeThread;
    Handler shakeHandler=new Handler();
	private ProgressBar progressBar;
	private LinearLayout view;
	ShakeListener mShaker;
	
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
		
		mShaker = new ShakeListener(this);		
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {  
		    public void onShake() {  
		    	reshake();
		    }  
		});

		shakeThread = new Thread(new Runnable() {
			public void run() {
				while(true){
					if(shake_state==0){
						the_dish = Communication_API.random_dish();
						
						if(the_dish!=null){
							dish_name = the_dish.getDish_name();
							dish_canteen = the_dish.getDish_canteen();
							dish_price = "$"+Float.toString(the_dish.getDish_price());
						}
						
						shakeHandler.post(new Runnable() {
							public void run() {						
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
						});
					}
				}
	  		}});
		shakeThread.start();
    }

    protected void onPause() {
    	mShaker.pause();
    	super.onPause();
    	shake_state = 1;
    	finish();
    }
    
    @Override
	protected void onResume(){
		mShaker.resume();
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