package com.ezmeal.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezmeal.lazylist.LazyAdapter;
import com.ezmeal.main.MainActivity;
import com.ezmeal.main.R;
import com.ezmeal.server.Communication_API;
import com.ezmeal.server.Dish;

public class SearchFragment extends Fragment {
	private Communication_API api = new Communication_API();
	LayoutInflater Inflater;
	private List<View> mListViews; 
    ListView list;
    private ViewPager awesomePager = null;  
    private AwesomePagerAdapter awesomeAdapter = null; 
    
    private LazyAdapter adapter = null;
    Runnable getDataThread = null;
    Runnable updateUI = null;
    MyHandler myHandler = null;
    private Handler refreshHandler=new Handler();
    private View view = null;
    private View page1 = null;
    private View page2 = null;
    private Activity activity = null;
	private ProgressBar progressBar = null;
	private Button submit_button = null;
	private Button back_button;
	private TextView no_result_text;
	private TextView timeout_text;
	private Spinner time_spinner;
	private ArrayAdapter<String> time_adapter;
	private static final String[] time_name={"Any time","Breakfast","Lunch","Tea","Dinner"};
	private Spinner canteen_spinner;
	private ArrayAdapter<String> canteen_adapter;
	private static final String[] canteen_name={"Any Canteen","LG7 Asia Pacific","Gold Rice Bowl","McDonald","LG1 Canteen","Chinese Restaurant","Coffee Shop","Seafront"};
	private EditText dish_name_text;
	private ImageView frame;
	
	private Vector<Dish> dishes;
	private int dish_counter = 0;

	private static final int INIT = 	0;
	private static final int FETCH = 	1;
	private static final int DISPLAY = 	2;
	private static final int WAIT = 	3;
	private static final int TIMEOUT = 	4;
	
	private int thread_state2  = WAIT;
	private boolean isTimeout = false;
	private boolean isNull = false;
	private boolean isInterrupt = false;
	private boolean isInit = false;
	private int retry_counter = 0;
	private static final int RETRY_MAX =5;
	
	//variable for preference
	private LinearLayout spicyBox;
	private ImageView spicyImage;
	private TextView spicyText;
	private int spicyState = 2;
	private LinearLayout meatBox;	
	private ImageView meatImage;
	private TextView meatText;
	private int meatState = 2;
	private int veganState = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("SearchFragment","OnCreate");
		HandlerThread handlerThread = new HandlerThread("handler_thread");
		handlerThread.start();
		myHandler = new MyHandler(handlerThread.getLooper());
		
		updateUI = new Runnable() {
			public void run() {						
					// TODO Auto-generated method stub	
				Log.e("SearchFragment","Update UI list");
				if(isTimeout){
	    	        progressBar.setVisibility(View.INVISIBLE);
	    	        timeout_text.setVisibility(View.VISIBLE);
	    	        thread_state2 = WAIT;
				}
				else{
					if(isNull){ //the result is empty
						Log.e("SearchFragment", "In fetch, DISPLAY,empty");
		    	        progressBar.setVisibility(View.INVISIBLE);
		    	        no_result_text.setVisibility(View.VISIBLE);
		    	        thread_state2 = WAIT;
					}
					else
					{
						Log.e("SearchFragment", "In fetch, DISPLAY,non empty");
		    	        adapter=new LazyAdapter(activity, dishes);
		    	        list.setAdapter(adapter);

		    	        progressBar.setVisibility(View.INVISIBLE);

		    	        list.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
		    	        list.setVisibility(View.VISIBLE);
		    			frame.setVisibility(View.VISIBLE);
		    			frame.bringToFront();
		    	        if(thread_state2!=INIT||thread_state2!=FETCH)
		    	        	thread_state2 = WAIT;
					}
				}
			}
		};

        getDataThread = new Runnable() {
    		public void run() {
/*    			if(!isInterrupt){
    				if(!itself.isVisible())
    					isInterrupt = true;
    			}*/
				switch(thread_state2){
				case INIT:
					Log.e("SearchFragment", "thread_state = INIT");
	    			dishes =new Vector<Dish>();
	    			Dish cur_dish;
	    			dish_counter = 0;
	    			retry_counter = 0;
	    			isNull = false;
	    			isTimeout = false;
	    			thread_state2 = FETCH;
					
	    			break;
				case FETCH:
					Log.e("SearchFragment", "thread_state = FETCH");
    				cur_dish = api.search_dish(dish_counter,
    						dish_name_text.getText().toString().length()==0?"any":dish_name_text.getText().toString(),
    						(canteen_spinner.getSelectedItemPosition()==1)||(canteen_spinner.getSelectedItemPosition()==0),
    						(canteen_spinner.getSelectedItemPosition()==2)||(canteen_spinner.getSelectedItemPosition()==0),
    						(canteen_spinner.getSelectedItemPosition()==3)||(canteen_spinner.getSelectedItemPosition()==0),
    						(canteen_spinner.getSelectedItemPosition()==4)||(canteen_spinner.getSelectedItemPosition()==0),
    						(canteen_spinner.getSelectedItemPosition()==5)||(canteen_spinner.getSelectedItemPosition()==0),
    						(canteen_spinner.getSelectedItemPosition()==6)||(canteen_spinner.getSelectedItemPosition()==0),
    						(canteen_spinner.getSelectedItemPosition()==7)||(canteen_spinner.getSelectedItemPosition()==0),
    						spicyState,
    						veganState,
    						meatState,
    						(time_spinner.getSelectedItemPosition()==4)||(time_spinner.getSelectedItemPosition()==0),
    						(time_spinner.getSelectedItemPosition()==3)||(time_spinner.getSelectedItemPosition()==0),
    						(time_spinner.getSelectedItemPosition()==2)||(time_spinner.getSelectedItemPosition()==0),
    						(time_spinner.getSelectedItemPosition()==1)||(time_spinner.getSelectedItemPosition()==0));
    				if(cur_dish==null){ //time out. Then delete all loaded dishes
    					if(retry_counter<RETRY_MAX){
    						retry_counter++;
    						break;
    					}
    					else{
	    					dishes.clear();
	    					thread_state2 = TIMEOUT;
	    					isTimeout = true;
	    					Log.e("SearchFragment", "thread_state = TIMEOUT");
	    					break;
    					}
    				}else{
	    				retry_counter = 0;
	    				if(cur_dish.getDish_id()==0) { //all dishes has been fetch
	    					if(dish_counter==0){
	    						isNull = true;
	    					}
	    					thread_state2 = DISPLAY;
	    					Log.e("SearchFragment", "thread_state = DISPLAY");
	    					break;
	    				}
	    				dish_counter++;
	    				dishes.add(cur_dish);
    				}
					break;
					
				case TIMEOUT:
				case DISPLAY:
	    			Log.e("SearchFragment", "thread_state = TIMEOUT/DISPLAY");
	    			break;
				case WAIT:
					Log.e("SearchFragment", "thread_state = WAIT");
					break;
				}
	      		Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("thread_state",thread_state2);
				myHandler.sendMessage(msg);
      		}
    	};

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e("SearchFragment","OnActivityCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("SearchFragment","OnCreateView");
		view = inflater.inflate(com.ezmeal.main.R.layout.search, container, false);
		page1 = inflater.inflate(R.layout.search_1, null);
		page2 = inflater.inflate(R.layout.search_2, null);
		Inflater = inflater;
		activity = this.getActivity();

		awesomeAdapter = new AwesomePagerAdapter();  
        awesomePager = (ViewPager) view.findViewById(R.id.viewpagerSearch);  
        awesomePager.setAdapter(awesomeAdapter);  

        mListViews = new ArrayList<View>();  
        mListViews.add(page1);  
        mListViews.add(page2);            
		list=(ListView)page2.findViewById(com.ezmeal.main.R.id.listSearch);
		no_result_text = (TextView) page2.findViewById(R.id.textNoResultSearch);
		timeout_text = (TextView) page2.findViewById(R.id.textTimeoutSearch);
		progressBar = (ProgressBar) page2.findViewById(R.id.progressBarSearch);
		frame = (ImageView) page2.findViewById(R.id.frameSearch);
		frame.setVisibility(View.INVISIBLE);
        
		//initialize two spinner
		time_spinner = (Spinner) page1.findViewById(R.id.spinnerTimeSearch);
		time_adapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,time_name);
		time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		time_spinner.setAdapter(time_adapter);
		time_spinner.setOnItemSelectedListener(new SpinnerSelectedListener()); 
		time_spinner.setVisibility(View.VISIBLE); 
		time_spinner.setSelection(0);
		canteen_spinner = (Spinner) page1.findViewById(R.id.spinnerCanteenSearch);
		canteen_adapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,canteen_name);
		canteen_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		canteen_spinner.setAdapter(canteen_adapter);
		canteen_spinner.setOnItemSelectedListener(new SpinnerSelectedListener()); 
		canteen_spinner.setVisibility(View.VISIBLE);  
		canteen_spinner.setSelection(0);
		
		spicyBox = (LinearLayout) page1.findViewById(R.id.spicyBox);
		spicyText= (TextView) page1.findViewById(R.id.spicyText);
		spicyImage=(ImageView) page1.findViewById(R.id.spicyImage);
		meatBox = (LinearLayout) page1.findViewById(R.id.meatBox);
		meatText= (TextView) page1.findViewById(R.id.meatText);
		meatImage=(ImageView) page1.findViewById(R.id.meatImage);
		spicyBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	switch(spicyState){
            	case 2:
            		spicyState=1;
            		spicyText.setText("Spicy");
            		spicyImage.setImageResource(R.drawable.icon_spicy);
            		break;
            	case 1:
            		spicyState=0;
            		spicyText.setText("No spicy");
            		spicyImage.setImageResource(R.drawable.spicy_ban);            		
            		break;
            	case 0:
            		spicyState = 2;
            		spicyText.setText("Either");
            		spicyImage.setImageResource(R.drawable.spicy_no_preference);
            		break;		
            	}
            }
		});
		
		meatBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	switch(meatState){
            	case 2:
            		meatState=1;
            		veganState = 0;
            		meatText.setText("Meat");
            		meatImage.setImageResource(R.drawable.icon_meat);
            		break;
            	case 1:
            		meatState=0;
            		veganState=1;
            		meatText.setText("Vegan");
            		meatImage.setImageResource(R.drawable.icon_vega);            		
            		break;
            	case 0:
            		meatState = 2;
            		veganState = 2;
            		meatText.setText("Either");
            		meatImage.setImageResource(R.drawable.venga_meat);
            		break;		
            	}
            }
		});
		
		dish_name_text = (EditText) page1.findViewById(R.id.editTextSearchName);
		
		//initialize the reconnect button
        submit_button = (Button) page1.findViewById(R.id.buttonSubmitSearch);
		submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	refleshDish();
            	awesomePager.setCurrentItem(1);
            	if(!isInit)
            		isInit = true;
            }
		});
        
		back_button = (Button) page2.findViewById(R.id.buttonBackSearch);
		back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	//refleshDish();
            	thread_state2 = WAIT;
            	awesomePager.setCurrentItem(0);
            }
		});	
        if(!isInterrupt){
			list.setVisibility(View.INVISIBLE);
			no_result_text.setVisibility(View.VISIBLE);
			timeout_text.setVisibility(View.INVISIBLE);		
			progressBar.setVisibility(View.INVISIBLE);
	        thread_state2 = WAIT;
	        Log.e("SearchFragment", "thread_state = WAIT(2)");
	        myHandler.post(getDataThread);
        }else{
        	isInterrupt = false;
        	list.getLayoutParams().height = 0;
        	list.setVisibility(View.INVISIBLE);
        	
        	no_result_text.setVisibility(View.INVISIBLE);
        	timeout_text.setVisibility(View.INVISIBLE);
        	
        	progressBar.setVisibility(View.VISIBLE);
        	
        	if(thread_state2 == WAIT)
        		thread_state2 = DISPLAY;
        	Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putInt("thread_state",thread_state2);
			myHandler.sendMessage(msg);
        }

      
		return view;
	}

	class SpinnerSelectedListener implements OnItemSelectedListener{  
		  
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
                long arg3) {  
        	//refleshDish();
        }  
  
        public void onNothingSelected(AdapterView<?> arg0) {  
        }  
    }  

	void refleshDish(){
    	list.getLayoutParams().height = 0;
    	list.setVisibility(View.INVISIBLE);
		frame.setVisibility(View.INVISIBLE);
    	
    	no_result_text.setVisibility(View.INVISIBLE);
    	timeout_text.setVisibility(View.INVISIBLE);
    	
    	progressBar.setVisibility(View.VISIBLE);
    	thread_state2 = INIT;
    	myHandler.post(getDataThread);
    	Log.e("SearchFragment", "thread_state = INIT");
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Log.e("SearchFragment","onResume");
		if(this.getActivity() instanceof MainActivity){
			if(((MainActivity)this.getActivity()).isDirty>0){
				((MainActivity)this.getActivity()).isDirty--;
				if(adapter!=null)
					adapter.updateDish(((MainActivity)this.getActivity()).position,((MainActivity)this.getActivity()).id);
			}
		}
	}
	
	@Override
	public void onPause(){
		super.onPause();
		Log.e("SearchFragment","onPause");
	}
	@Override
	public void onStop(){
		super.onStop();
		Log.e("SearchFragment","onStop");
	}
	@Override
	public void onDestroyView(){
		super.onDestroyView();
		if(isInit)
			isInterrupt = true;
		Log.e("SearchFragment","onDestroyView");
	}

	private class AwesomePagerAdapter extends PagerAdapter{  
		  
        
        @Override  
        public int getCount() {  
            return mListViews.size();  
        }  
  
        @Override  
        public Object instantiateItem(View collection, int position) {  
  
              
            ((ViewPager) collection).addView(mListViews.get(position),0);  
              
            return mListViews.get(position);  
        }  
  
        @Override  
        public void destroyItem(View collection, int position, Object view) {  
            ((ViewPager) collection).removeView(mListViews.get(position));  
        }  
  
          
          
        @Override  
        public boolean isViewFromObject(View view, Object object) {  
            return view==(object);  
        }  
  
        @Override  
        public void finishUpdate(View arg0) {}  
          
  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {}  
  
        @Override  
        public Parcelable saveState() {
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {}  
          
    }
	class MyHandler extends Handler{
        public MyHandler(Looper looper){  
            super(looper);  
        }  
        @Override  
        public void handleMessage(Message msg) {  
            //System.out.println("MyHandler Thread :" + Thread.currentThread().getId());  
            // 
            if(thread_state2==DISPLAY||thread_state2==TIMEOUT){
            	//System.out.println("MyHandler Thread removed");  
                removeCallbacks(getDataThread);  
                if(isInterrupt);
                else
                	refreshHandler.post(updateUI);
            }else if(thread_state2==WAIT){
            	//System.out.println("MyHandler Thread removed");  
                removeCallbacks(getDataThread); 
                refreshHandler.removeCallbacks(updateUI);
            }else{
            	//System.out.println("MyHandler Thread post");
            	post(getDataThread); 
            }
        }
	}
}