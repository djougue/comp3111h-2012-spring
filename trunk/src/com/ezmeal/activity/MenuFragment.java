package com.ezmeal.activity;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.ezmeal.lazylist.LazyAdapter;
import com.ezmeal.main.R;
import com.ezmeal.server.Communication_API;
import com.ezmeal.server.Dish;

public class MenuFragment extends Fragment {
	LayoutInflater Inflater;
    ListView list;
    LazyAdapter adapter;
    Thread getDataThread;
    Handler refreshHandler=new Handler();
    private View view;
    private Activity activity;
	private ProgressBar progressBar;

	private Spinner time_spinner;
	private Spinner canteen_spinner;
	private ArrayAdapter<String> canteen_adapter;
	private static final String[] canteen_name={"Coffee Shop","Chinese Restaurant","McDonald","Seafront","LG7 Asia Pacific","Gold Rice Bowl"};

	private Button reconnectBt;
	private Vector<Bundle> dishes;
	private int dish_counter = 0;

	private static final int INIT = 	0;
	private static final int FETCH = 	1;
	private static final int DISPLAY = 	2;
	private static final int WAIT = 	3;
	private static final int TIMEOUT = 	4;
	
	private int thread_state  = WAIT;
	
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
		view = inflater.inflate(com.ezmeal.main.R.layout.menu, container, false);
		Inflater = inflater;
		activity = this.getActivity();
		
		list=(ListView)view.findViewById(com.ezmeal.main.R.id.list);
		
		progressBar = (ProgressBar) view.findViewById(R.id.progressBarMenu);
		progressBar.setVisibility(View.VISIBLE);
		
		//initialize two spinner
		time_spinner = (Spinner) view.findViewById(R.id.timeSpinner);
		canteen_spinner = (Spinner) view.findViewById(R.id.canteenSpinner);
		canteen_adapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,canteen_name);
		canteen_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		canteen_spinner.setAdapter(canteen_adapter);
		canteen_spinner.setOnItemSelectedListener(new SpinnerSelectedListener()); 
		canteen_spinner.setVisibility(View.VISIBLE);  
		
		//initialize the reconnect button
		reconnectBt = (Button) view.findViewById(R.id.reconnectBt);
		reconnectBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	refleshDish();
            }
		});

		
        getDataThread = new Thread(new Runnable() {
    		public void run() {
    			while(true){
    				switch(thread_state){
    				case INIT:
		    			dishes =new Vector<Bundle>();
		    			Dish cur_dish;
		    			dish_counter = 0;
		    			thread_state = FETCH;
		    			break;
    				case FETCH:
	    				cur_dish = Communication_API.fetch_dish(dish_counter);
	    				if(cur_dish==null){ //time out. Then delete all loaded dishes    					
	    					dishes.clear();
	    					thread_state = TIMEOUT;
	    					Log.e("MenuFragment", "In fetch, timeout");
	    					break;
	    				}
	    				if(cur_dish.getDish_id()==0) { //all dishes has been fetch
	    					thread_state = DISPLAY;
	    					Log.e("MenuFragment", "In fetch, get all dishes");
	    					break;
	    				}
	    				dish_counter++;
	    				Bundle bundle = new Bundle();
	    				bundle.putString("name", cur_dish.getDish_name());
	    				bundle.putString("canteen", cur_dish.getDish_canteen());
	    				bundle.putFloat("price", cur_dish.getDish_price());
	    				dishes.add(bundle);
    					break;
    				case TIMEOUT:
    				case DISPLAY:
		    			refreshHandler.post(new Runnable() {
		    				public void run() {						
									// TODO Auto-generated method stub	
		    					if(thread_state == TIMEOUT){
					    	        progressBar.setVisibility(View.INVISIBLE);
					    	        reconnectBt.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
		    						reconnectBt.setVisibility(View.VISIBLE);
		    					}
		    					else{
		    						Log.e("MenuFragment", "In fetch, DISPLAY");
					    	        adapter=new LazyAdapter(activity, dishes);
					    	        list.setAdapter(adapter);

					    	        progressBar.setVisibility(View.INVISIBLE);
					    	        progressBar.getLayoutParams().height=0;

					    	        list.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
					    	        list.setVisibility(View.VISIBLE);
		    					}
		    				}
		    			});
		    			thread_state = WAIT;
		    			break;
    				case WAIT:
    					break;
    				}
    			}
      		}
    	});
        thread_state = INIT;
        getDataThread.start();
      
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
		reconnectBt.getLayoutParams().height = 0;
    	reconnectBt.setVisibility(View.INVISIBLE);

    	list.getLayoutParams().height = 0;
    	list.setVisibility(View.INVISIBLE);

    	progressBar.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
    	progressBar.setVisibility(View.VISIBLE);
    	thread_state = INIT;
	}
 }
