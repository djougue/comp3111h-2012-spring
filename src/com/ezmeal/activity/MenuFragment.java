package com.ezmeal.activity;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezmeal.lazylist.LazyAdapter;
import com.ezmeal.main.R;
import com.ezmeal.server.Communication_API;
import com.ezmeal.server.Dish;

public class MenuFragment extends Fragment {
	private Communication_API api = new Communication_API();
	LayoutInflater Inflater;
    ListView list;
    LazyAdapter adapter;
    Thread getDataThread;
    Handler refreshHandler=new Handler();
    private View view;
    private Activity activity;
	private ProgressBar progressBar;
	private TextView no_result_text;

	private Spinner time_spinner;
	private Spinner canteen_spinner;
	private ArrayAdapter<String> canteen_adapter;
	private static final String[] canteen_name={"Coffee Shop","Chinese Restaurant","McDonald","Seafront","LG7 Asia Pacific","Gold Rice Bowl"};
	private static final String[] canteen_search_name={"Coffee","Chinese","McDonald","Seafront","Pacific","Bowl"};
	private ArrayAdapter<String> time_adapter;
	private static final String[] time_name={"Breakfast","Lunch","Tea","Dinner"};

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
		
		no_result_text = (TextView) view.findViewById(R.id.textNoResultMenu);
		no_result_text.setVisibility(View.INVISIBLE);
		
		//initialize two spinner
		time_spinner = (Spinner) view.findViewById(R.id.timeSpinner);
		time_adapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,time_name);
		time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		time_spinner.setAdapter(time_adapter);
		time_spinner.setOnItemSelectedListener(new SpinnerSelectedListener()); 
		time_spinner.setVisibility(View.VISIBLE); 
		Calendar rightNow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		int hour =rightNow.get(Calendar.HOUR_OF_DAY);
		int minutes = rightNow.get(Calendar.MINUTE);
		int total_minutes = 60*hour+minutes;
		if(total_minutes>=7*60&&total_minutes<11*60){
			time_spinner.setSelection(0);
		}else if(total_minutes<(14*60+30)){
			time_spinner.setSelection(1);
		}else if(total_minutes<(17*60+30)){
			time_spinner.setSelection(2);
		}else{
			time_spinner.setSelection(3);
		}
		
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
	    				cur_dish = api.search_dish(dish_counter,
	    						"any",
	    						canteen_search_name[canteen_spinner.getSelectedItemPosition()],
	    						2,2,2,
	    						(time_spinner.getSelectedItemPosition()==3),
	    						(time_spinner.getSelectedItemPosition()==2),
	    						(time_spinner.getSelectedItemPosition()==1),
	    						(time_spinner.getSelectedItemPosition()==0));
	    				if(cur_dish==null){ //time out. Then delete all loaded dishes
	    					dishes.clear();
	    					thread_state = TIMEOUT;
	    					break;
	    				}
	    				if(cur_dish.getDish_id()==0) { //all dishes has been fetch
	    			    	
	    					thread_state = DISPLAY;
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
		    					if(thread_state == TIMEOUT){
		    						Log.e("MenuFragment", "Timeout!");
					    	        progressBar.setVisibility(View.INVISIBLE);
					    	        reconnectBt.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
		    						reconnectBt.setVisibility(View.VISIBLE);
		    					}
		    					else{
		    						if(dish_counter == 0){
						    	        progressBar.setVisibility(View.INVISIBLE);
						    	        no_result_text.setVisibility(View.VISIBLE);
						    	        Log.e("MenuFragment", "no result");
		    						}
		    						else{
						    	        adapter=new LazyAdapter(activity, dishes);
						    	        list.setAdapter(adapter);
						    	        progressBar.setVisibility(View.INVISIBLE);
//						    	        progressBar.getLayoutParams().height=0;

						    	        list.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
						    	        list.setVisibility(View.VISIBLE);
		    						}
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
    	reconnectBt.setVisibility(View.INVISIBLE);
    	list.setVisibility(View.INVISIBLE);    	
    	no_result_text.setVisibility(View.INVISIBLE);

    	progressBar.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
    	progressBar.setVisibility(View.VISIBLE);
    	thread_state = INIT;
	}
 }
