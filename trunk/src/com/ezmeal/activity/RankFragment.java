package com.ezmeal.activity;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezmeal.lazylist.LazyAdapter;
import com.ezmeal.lazylist.LinearLayoutForListView;
import com.ezmeal.main.R;
import com.ezmeal.server.Communication_API;
import com.ezmeal.server.Dish;

public class RankFragment extends Fragment {
	private Communication_API api = new Communication_API();
	LayoutInflater Inflater;
	private LinearLayoutForListView list;
    LazyAdapter adapter;
    Runnable getDataThread;
    Runnable updateUI;
    MyHandler myHandler;
    Handler refreshHandler=new Handler();
    private View view;
    private Activity activity;
	private ProgressBar progressBar;
	private TextView no_result_text;
	private ImageView frame;

/*	private Spinner time_spinner;
	private Spinner canteen_spinner;
	private ArrayAdapter<String> canteen_adapter;
	private static final String[] canteen_name={"LG7 Asia Pacific","Gold Rice Bowl","McDonald","LG1 Canteen","Chinese Restaurant","Coffee Shop","Seafront"};
	private ArrayAdapter<String> time_adapter;
	private static final String[] time_name={"Breakfast","Lunch","Tea","Dinner"};
*/
	private Button reconnectBt;
//	private Vector<Bundle> dishes;
	private Vector<Dish> dishes;
	private int dish_counter = 0;

	private static final int INIT = 	0;
	private static final int FETCH = 	1;
	private static final int DISPLAY = 	2;
	private static final int WAIT = 	3;
	private static final int TIMEOUT = 	4;
	
	private int thread_state  = WAIT;
	private boolean isTimeout = false;
	private boolean isNull = false;
	private boolean isLock = false;
	private int retry_counter = 0;
	private static final int RETRY_MAX =5;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(com.ezmeal.main.R.layout.ranking, container, false);
		Inflater = inflater;
		activity = this.getActivity();
		
		list=(LinearLayoutForListView )view.findViewById(com.ezmeal.main.R.id.listRank);
		
		progressBar = (ProgressBar) view.findViewById(R.id.progressBarRank);
		progressBar.setVisibility(View.VISIBLE);
		
		no_result_text = (TextView) view.findViewById(R.id.textNoResultRank);
		no_result_text.setVisibility(View.INVISIBLE);

		//initialize the reconnect button
		reconnectBt = (Button) view.findViewById(R.id.reconnectBtRank);
		reconnectBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	refleshDish();
            }
		});
		
		frame = (ImageView) view.findViewById(R.id.frameRank);
		frame.setVisibility(View.INVISIBLE);

		HandlerThread handlerThread = new HandlerThread("handler_thread");
		handlerThread.start();
		myHandler = new MyHandler(handlerThread.getLooper());
		updateUI = new Runnable() {
			public void run() {
				isLock = true;
				if(isTimeout){
	    	        progressBar.setVisibility(View.INVISIBLE);
	    	        reconnectBt.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
					reconnectBt.setVisibility(View.VISIBLE);
	    			thread_state = WAIT;
				}
				else{
					if(isNull){
		    	        progressBar.setVisibility(View.INVISIBLE);
		    	        no_result_text.setVisibility(View.VISIBLE);
		    			thread_state = WAIT;
					}
					else{
		    	        adapter=new LazyAdapter(activity, dishes);
		    	        list.setAdapter(adapter);
		    	        progressBar.setVisibility(View.INVISIBLE);
//		    	        progressBar.getLayoutParams().height=0;

		    	        list.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
		    	        list.setVisibility(View.VISIBLE);
		    			frame.setVisibility(View.VISIBLE);
		    			frame.bringToFront();
		    	        if(thread_state!=INIT||thread_state!=FETCH)
		    	        	thread_state = WAIT;
					}
				}
				isLock = false;
			}
		};
        getDataThread = new Runnable() {
    		public void run() {
    			//while(thread_state!=(WAIT+1)){
    				switch(thread_state){
    				case INIT:
    	            	Log.e("getDataThread","init");
		    			dishes =new Vector<Dish>();
		    			Dish cur_dish;
		    			dish_counter = 1;
		    			retry_counter = 0;
		    			isTimeout = false;
		    			isNull = false;
		    			thread_state = FETCH;
		    			break;
    				case FETCH:
    	            	Log.e("getDataThread","fetch");
	    				cur_dish = api.fetch_dish_by_rank(dish_counter);
	    				if(cur_dish==null){ //time out. Then delete all loaded dishes
	    					if(retry_counter<RETRY_MAX){
	    						retry_counter++;
	    						break;
	    					}
	    					else{
		    					dishes.clear();
				    			dish_counter = 0;
	//    						Log.e("MenuFragment", "Timeout!!");
	    						isTimeout = true;
		    					thread_state = TIMEOUT;
		    					retry_counter = 0;
		    					break;
	    					}
	    				}else{
		    				retry_counter = 0;
		    				if(cur_dish.getDish_id()==0 ||dish_counter == 11) { //all dishes has been fetch	  
		    					if(dish_counter==0){
		    						isNull = true;
		    					}
		    					thread_state = DISPLAY;
		    					break;
		    				}
		    				dish_counter++;
		    				dishes.add(cur_dish);
	    				}
    					break;
    				case TIMEOUT:
    				case DISPLAY:
    	            	Log.e("getDataThread","timeout/display");
		    			
		    			//thread_state = WAIT;
		    			break;
    				case WAIT:
    	            	Log.e("getDataThread","wait");
    					//thread_state++;
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
        
		return view;
	}

	class SpinnerSelectedListener implements OnItemSelectedListener{  
		  
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
                long arg3) {  
        	refleshDish();
        }  
  
        public void onNothingSelected(AdapterView<?> arg0) {  
        }  
    }  

	void refleshDish(){
    	reconnectBt.setVisibility(View.INVISIBLE);
    	list.setVisibility(View.INVISIBLE);    	
		frame.setVisibility(View.INVISIBLE);
    	no_result_text.setVisibility(View.INVISIBLE);

    	progressBar.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
    	progressBar.setVisibility(View.VISIBLE);
		thread_state = INIT;
    	myHandler.post(getDataThread);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		refleshDish();

	}
	
	class MyHandler extends Handler{
        public MyHandler(Looper looper){  
            super(looper);  
        }  
        @Override  
        public void handleMessage(Message msg) {  
            System.out.println("MyHandler Thread :" + Thread.currentThread().getId());  
            // 
            if(thread_state==DISPLAY||thread_state==TIMEOUT){
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