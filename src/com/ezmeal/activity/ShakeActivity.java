package com.ezmeal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezmeal.activity.MenuFragment.MyHandler;
import com.ezmeal.lazylist.ImageLoader;
import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;
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
	private String pic=" ";
	private int shake_state = 0;
	Dish the_dish;
    private Thread shakeThread;
    Handler shakeHandler=new Handler();
    Runnable getDataThread;
    Runnable updateUI;
    MyHandler myHandler;
    Handler refreshHandler=new Handler();

	private ProgressBar progressBar;
	private LinearLayout view;
	ShakeListener mShaker;
	ImageView dishImage;
	ImageView dishImageButton;
    private ImageLoader imageLoader; 

	
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
		    	if(((UserApp)getApplication()).isShake()){
		    		reshake();
		    	}
		    }  
		});

		imageLoader=new ImageLoader(this.getApplicationContext());
		dishImage = (ImageView) findViewById(com.ezmeal.main.R.id.dishImageShake);
		dishImageButton = (ImageView) findViewById(com.ezmeal.main.R.id.dishImageButton);
		dishImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if(the_dish != null){
	    			Intent intent = new Intent(getApplicationContext(),
	    					com.ezmeal.activity.DetailActivity.class);
	    			
	    			Bundle dishInfo = the_dish.dishToBundle();
	    			dishInfo.putInt("ActivityFlag", 1);
	    			intent.putExtras(dishInfo);
	    			startActivity(intent);
            	}
    		}
		});
        
		HandlerThread handlerThread = new HandlerThread("handler_thread");
		handlerThread.start();
		myHandler = new MyHandler(handlerThread.getLooper());
		updateUI = new Runnable() {
			public void run() {
				if(shake_state == 2){
					TextView name=(TextView)findViewById(com.ezmeal.main.R.id.textDishNameInShake);	
					TextView canteen=(TextView)findViewById(com.ezmeal.main.R.id.textDishCanteenInShake);
					TextView price=(TextView)findViewById(com.ezmeal.main.R.id.textDishPriceInShake);
					name.setText(dish_name);
					canteen.setText(dish_canteen);
					price.setText(dish_price);
			        imageLoader.DisplayImage(pic, dishImage);									
					
					view=(LinearLayout)findViewById(com.ezmeal.main.R.id.information);	
					progressBar.setVisibility(View.INVISIBLE);
					view.setVisibility(View.VISIBLE);
					shake_state = 1;
				}
			}
		};
        getDataThread = new Runnable() {
    		public void run() {
				if(shake_state==0){
					the_dish = api.random_dish(((UserApp)getApplication()).getUserName(),((UserApp)getApplication()).getTaste()[0],((UserApp)getApplication()).getTaste()[1]);
					
					if(the_dish!=null){
						if(the_dish.getDish_price()==0.00){ //no result
							dish_name = "no result. Please modify your preference.";
							dish_canteen = "";
							dish_price = "";
							pic = "";
							
						}
						else{
							dish_name = the_dish.getDish_name();
							dish_canteen = the_dish.getDish_canteen();
							dish_price = "$"+Float.toString(the_dish.getDish_price());
							pic = "http://143.89.220.19/COMP3111H/image/stub.png";
							if(the_dish.hasImage())
								pic ="http://143.89.220.19/COMP3111H/image/"+the_dish.getDish_id()+".jpg";
						}

						shake_state = 2;
					}
					else{
						dish_name="Connection failed";
						dish_canteen=" ";
						dish_price=" ";
						shake_state = 2;
					}	
				}
	      		Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("thread_state",shake_state);
				myHandler.sendMessage(msg);
    		}
        };
/*		shakeThread = new Thread(new Runnable() {
			public void run() {
				while(true){
					if(shake_state==0){
						the_dish = api.random_dish(((UserApp)getApplication()).getUserName(),((UserApp)getApplication()).getTaste()[0],((UserApp)getApplication()).getTaste()[1]);
						
						if(the_dish!=null){
							if(the_dish.getDish_price()==0.00){ //no result
								dish_name = "no result. Please modify your preference.";
								dish_canteen = "";
								dish_price = "";
								pic = "";
								
							}
							else{
								dish_name = the_dish.getDish_name();
								dish_canteen = the_dish.getDish_canteen();
								dish_price = "$"+Float.toString(the_dish.getDish_price());
								pic = "http://143.89.220.19/COMP3111H/image/stub.png";
								if(the_dish.hasImage())
									pic ="http://143.89.220.19/COMP3111H/image/"+the_dish.getDish_id()+".jpg";
							}

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
							        imageLoader.DisplayImage(pic, dishImage);									
									
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
*/
        shake_state = 0;
        myHandler.post(getDataThread);
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
    		myHandler.post(getDataThread);
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
	class MyHandler extends Handler{
        public MyHandler(Looper looper){  
            super(looper);  
        }  
        @Override  
        public void handleMessage(Message msg) {  
            System.out.println("MyHandler Thread :" + Thread.currentThread().getId());  
            // 
            if(shake_state==2){
            	System.out.println("MyHandler Thread removed");  
                removeCallbacks(getDataThread);  
                refreshHandler.post(updateUI);
            }else if(shake_state==1){
            	System.out.println("MyHandler Thread removed");  
                removeCallbacks(getDataThread); 
                refreshHandler.removeCallbacks(updateUI);
            }else{
            	System.out.println("MyHandler Thread post");
            	post(getDataThread); 
            }
        }
	}        
}