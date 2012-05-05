package com.ezmeal.activity;

import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ezmeal.lazylist.LazyAdapter;
import com.ezmeal.lazylist.LinearLayoutForListView;
import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;
import com.ezmeal.server.Communication_API;
import com.ezmeal.server.Dish;

public class MyTasteActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	private Communication_API api = new Communication_API();
	private TextView headerTitle, resultText;
	private Button submitBtn, backBtn;
	private RadioButton spicyBtn[], meatBtn[];
	private RadioGroup spicy_group, meat_group;
	private ProgressBar progressBar;
	private Thread postDataThread;
	private Handler refreshHandler = new Handler();

	//variable for blacklist
	private Communication_API api_for_black_list = new Communication_API();
    LazyAdapter adapter;
	private LinearLayoutForListView black_list= null;
	//-------------------------------------
	private Handler refreshHandler_for_black_list;
	private Runnable getBlackListThread;
    private Runnable getDataThread;
    private Runnable updateUI;
    MyHandler myHandler;
    //-------------------------------------
	private static int serverResp;
	private boolean isChanged = false;
	private Activity activity;
	private Vector<Dish> dishes;
	private int dish_counter = 0;
	private TextView noresult_blacklist;
	private TextView timeout_blacklist;
	private ProgressBar progressBar_blacklist;

	private static final int INIT = 	0;
	private static final int FETCH = 	1;
	private static final int DISPLAY = 	2;
	private static final int WAIT = 	3;
	private static final int TIMEOUT2 = 	4;
	
	private int thread_state  = WAIT;
	private boolean isTimeout = false;
	private boolean isNull = false;
	private boolean isLock = false;
	private int retry_counter = 0;
	private static final int RETRY_MAX =5;

	
	private String TIMEOUT    = "Connection error. Please try again later.";
	private String LOADING    = "Loading...";
	private String SUCCESSFUL = "You have successfully changed your taste.";
	private String ERROR_MSG  = "Error occurred to My Taste modification.";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytaste);
        refreshHandler_for_black_list = new Handler();
        
        //set the header title
        headerTitle = (TextView) findViewById(R.id.labelHeader);
        headerTitle.setText("My Taste");
        
        //get check box
/*
        spicyBtn[2] = (RadioButton) findViewById(R.id.spicyDefault);
        spicyBtn[1] = (RadioButton) findViewById(R.id.spicyPrefer);
        spicyBtn[0] = (RadioButton) findViewById(R.id.spicyNotPrefer);
         meatBtn[2] = (RadioButton) findViewById(R.id.meatDefault);
         meatBtn[1] = (RadioButton) findViewById(R.id.meatPrefer);
         meatBtn[0] = (RadioButton) findViewById(R.id.veganPrefer);
         */
        spicy_group = (RadioGroup) findViewById(R.id.spicyGroup);
         meat_group = (RadioGroup) findViewById(R.id.meatGroup);
//        vegeBtn = (CheckBox) findViewById(R.id.checkBoxMyTasteVege);
        
        //set check status of the check boxes
        int taste[] = ((UserApp) this.getApplication()).getTaste();
        switch(taste[0]){
        case 0:
        	spicy_group.check(R.id.spicyNotPrefer);
        	break;
        case 1:
        	spicy_group.check(R.id.spicyPrefer);
        	break;
        default:
        	spicy_group.check(R.id.spicyDefault);
        	break;
        }
        switch(taste[1]){
        case 0:
        	meat_group.check(R.id.veganPrefer);
        	break;
        case 1:
        	meat_group.check(R.id.meatPrefer);
        	break;
        default:
        	meat_group.check(R.id.meatDefault);
        	break;
        }
//        spicyBtn.setChecked();
//        meatBtn.setChecked(taste[1]);
//        vegeBtn.setChecked(taste[2]);
        android.widget.RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener(){

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				isChanged = true;
				
			}
        	
        };
        //set a listener to detect the change of the check boxes
        spicy_group.setOnCheckedChangeListener(listener);
         meat_group.setOnCheckedChangeListener(listener);
//        vegeBtn.setOnCheckedChangeListener(this);
        
        //result
        progressBar = (ProgressBar) findViewById(R.id.progressBarMyTaste);
        resultText = (TextView) findViewById(R.id.labelMyTasteResult);
        
        //buttons
        submitBtn = (Button) findViewById(R.id.buttonMyTasteSubmit);
        submitBtn.setOnClickListener(this);
        backBtn = (Button) findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(this);
        
        black_list = (LinearLayoutForListView) findViewById(R.id.blacklist);
    	noresult_blacklist = (TextView) findViewById(R.id.textNoResultBlacklist);
    	timeout_blacklist = (TextView) findViewById(R.id.textTimeoutBlacklist);
    	progressBar_blacklist = (ProgressBar) findViewById(R.id.progressBarBlacklist);
    	timeout_blacklist.setVisibility(View.INVISIBLE);
    	noresult_blacklist.setVisibility(View.INVISIBLE);
        black_list.setVisibility(View.INVISIBLE);
    	progressBar_blacklist.setVisibility(View.VISIBLE);

		HandlerThread handlerThread = new HandlerThread("handler_thread");
		handlerThread.start();
		myHandler = new MyHandler(handlerThread.getLooper());

        activity = this;
    	final String username = ((UserApp) this.getApplication()).getUserName();
    	
    	updateUI = new Runnable(){
     		public void run() {
				isLock = true;
				if(isTimeout){
	    	        progressBar_blacklist.setVisibility(View.INVISIBLE);
	    	        timeout_blacklist.setVisibility(View.VISIBLE);
	    			thread_state = WAIT;
	    			Log.e("Blacklist","timeout_show");
				}
				else{
					if(isNull){
		    	        progressBar_blacklist.setVisibility(View.INVISIBLE);
		    	        noresult_blacklist.setVisibility(View.VISIBLE);
		    			thread_state = WAIT;
		    			Log.e("Blacklist","Null_show");
					}
					else{
		    	        adapter=new LazyAdapter(activity, dishes);
		    	        black_list.setAdapter(adapter);
		    	        progressBar_blacklist.setVisibility(View.INVISIBLE);
//				    	        progressBar.getLayoutParams().height=0;

		    	        black_list.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
		    	        black_list.setVisibility(View.VISIBLE);
		    			thread_state = WAIT;
		    			Log.e("Blacklist","Success_show");
					}
				}
				isLock = false;	
			}
    	};
     		
     		getDataThread = new Runnable() {
     		public void run() {
 				switch(thread_state){
 				case INIT:
	    			dishes =new Vector<Dish>();
	    			Dish cur_dish;
	    			dish_counter = 0;
	    			retry_counter = 0;
	    			isTimeout = false;
	    			isNull = false;
	    			thread_state = FETCH;
	    			Log.e("Blacklist","start Fetch");
	    			break;
 				case FETCH:
    				cur_dish = api_for_black_list.fetch_blacklist(dish_counter,username);
    				if(cur_dish==null){ //time out. Then delete all loaded dishes
    					if(retry_counter<RETRY_MAX){
    						retry_counter++;
    						break;
    					}
    					else{
	    					dishes.clear();
			    			dish_counter = 0;
    						Log.e("Blacklist", "Timeout!!");
    						isTimeout = true;
	    					thread_state = TIMEOUT2;
	    					retry_counter = 0;
	    					break;
    					}
    				}
    				retry_counter = 0;
    				if(cur_dish.getDish_id()==0) { //all dishes has been fetch	  
    					if(dish_counter==0){
    						isNull = true;
    					}
    					thread_state = DISPLAY;
		    			Log.e("Blacklist","Success_show");
    					break;
    				}
    				dish_counter++;
    				Log.e("Blacklist", "dish"+dish_counter);
    				dishes.add(cur_dish);
 					break;
 				case TIMEOUT2:
 				case DISPLAY:
	    			break;
 				case WAIT:
 					break;
 				}
	      		Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("thread_state",thread_state);
				myHandler.sendMessage(msg);
       		}
     	};
        thread_state = INIT;
        myHandler.post(getDataThread);
        Log.e("Blacklist","INIT");        
    }
    
    /**
     * Post My Taste form to the server.
     */
    private void postMyTasteData() {
    	//get input
    	final String uname = ((UserApp) this.getApplication()).getUserName();
    	final String nname = ((UserApp) this.getApplication()).getNickName();
    	final String passwd = ((UserApp) this.getApplication()).getPassword();
    	int spicy_id = spicy_group.getCheckedRadioButtonId();
    	final int isSpicy = (spicy_id==R.id.spicyDefault?2:(spicy_id==R.id.spicyPrefer?1:0));
    	int meat_id = meat_group.getCheckedRadioButtonId();
    	final int isMeat = (meat_id==R.id.meatDefault?2:(meat_id==R.id.meatPrefer?1:0));
    	final int isVege = (isMeat==2?2:(isMeat==1?0:1));
    	final Activity thisActivity = this;
    	
    	postDataThread = new Thread(new Runnable() {
    		public void run() {
    			MyTasteActivity.serverResp =
    					api.change_user_setting(uname, passwd, nname, isSpicy, isVege, isMeat);
    			
    			//Refresh the data of this app
    			refreshHandler.post(new Runnable() {
    				public void run() {
    				    if(serverResp == 0) {
    				    	resultText.setTextColor(0xffffffff); //white
    		    			resultText.setText(TIMEOUT);
    				    }
    					else if (serverResp == 1) {
    		    			resultText.setTextColor(0xffffffff); //white
    		    			resultText.setText(SUCCESSFUL);
    		    			
    		    			//change local data
    		    			int[] taste = {isSpicy, isMeat, isVege};
    		    			((UserApp) thisActivity.getApplication()).setTaste(taste);
    		    			
    		    		} else {
    		    			resultText.setTextColor(0xffff0000); //red
    		    			resultText.setText(ERROR_MSG);
    		    		}
    		    		progressBar.setVisibility(View.GONE);
    				}
    			});
    		}
    	});
    	postDataThread.start();
	}
    
    /**
     * Click Listener
     */
    public void onClick(View view) {
    	if (view == submitBtn) {
        	resultText.setTextColor(0xffffffff); //white
    		resultText.setText(LOADING);
    		progressBar.setVisibility(View.VISIBLE);
    		postMyTasteData();
    		isChanged = false;
    	}
        else if (view == backBtn) {
    		//Pop up an alert dialog if something has been modified
    		if (isChanged) {
    			final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    			alertDialog.setTitle("Alert!");
    			alertDialog.setMessage("Are you sure to exit this page?");
    			alertDialog.setButton("Yes, please.", new DialogInterface.OnClickListener() {
    			      public void onClick(DialogInterface dialog, int which) {
    			    	  finish();
    			    } });
    			alertDialog.setButton2("Oops, NO!", new DialogInterface.OnClickListener() {
  			      public void onClick(DialogInterface dialog, int which) {
  			    	  alertDialog.dismiss();
  			        } });
    			alertDialog.show();
    		}
    		
    		//Else, directly finish the activity, and go back to the welcome page.
    		else {
    		    finish();
    		}
    	}
    }

	public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
		isChanged = true;
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		reflesh();
	}
	
	private void reflesh(){
    	timeout_blacklist.setVisibility(View.INVISIBLE);
    	noresult_blacklist.setVisibility(View.INVISIBLE);
        black_list.setVisibility(View.INVISIBLE);
    	progressBar_blacklist.setVisibility(View.VISIBLE);
    	thread_state = INIT;
    	myHandler.post(getDataThread);
	}
	
	class MyHandler extends Handler{
        public MyHandler(Looper looper){  
            super(looper);  
        }  
        @Override  
        public void handleMessage(Message msg) {  
            System.out.println("MyHandler Thread :" + Thread.currentThread().getId());  
            // 
            if(thread_state==DISPLAY||thread_state==TIMEOUT2){
            	System.out.println("MyHandler Thread removed");  
                removeCallbacks(getDataThread);  
                refreshHandler.post(updateUI);
            }else if(thread_state==WAIT){
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