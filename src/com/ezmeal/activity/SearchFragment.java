package com.ezmeal.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezmeal.lazylist.LazyAdapter;
import com.ezmeal.main.R;
import com.ezmeal.server.Communication_API;
import com.ezmeal.server.Dish;

public class SearchFragment extends Fragment {
	private Communication_API api = new Communication_API();
	LayoutInflater Inflater;
	private List<View> mListViews; 
    ListView list;
    private ViewPager awesomePager;  
    private AwesomePagerAdapter awesomeAdapter; 
    
    private LazyAdapter adapter;
    private Thread getDataThread;
    private Handler refreshHandler=new Handler();
    private View view;
    private View page1;
    private View page2;
    private Activity activity;
	private ProgressBar progressBar;
	private Button submit_button;
	private Button back_button;
	private TextView no_result_text;
	private TextView timeout_text;
	private Spinner time_spinner;
	private ArrayAdapter<String> time_adapter;
	private static final String[] time_name={"Any time","Breakfast","Lunch","Tea","Dinner"};
	private Spinner canteen_spinner;
	private ArrayAdapter<String> canteen_adapter;
	private static final String[] canteen_name={"Any Canteen","Coffee Shop","Chinese Restaurant","McDonald","Seafront","LG7 Asia Pacific","Gold Rice Bowl"};
	private static final String[] canteen_search_name={"any","Coffee Shop","Chinese Restaurant","McDonald","Seafront Cafeteria","LG7 Asia Pacific Catering","LG7 Gold Rice Bowl"};
	private CheckBox[] taste = new CheckBox[3];
	private EditText dish_name_text;
	
	private Vector<Bundle> dishes;
	private int dish_counter = 0;

	private static final int INIT = 	0;
	private static final int FETCH = 	1;
	private static final int DISPLAY = 	2;
	private static final int WAIT = 	3;
	private static final int TIMEOUT = 	4;
	
	private int thread_state2  = WAIT;
	
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
		list.setVisibility(View.INVISIBLE);
		
		progressBar = (ProgressBar) page2.findViewById(R.id.progressBarSearch);
		progressBar.setVisibility(View.INVISIBLE);
		
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
		
		taste[0] =(CheckBox) page1.findViewById(R.id.checkBoxSpicy);
		taste[1] =(CheckBox) page1.findViewById(R.id.checkBoxVegan);
		taste[2] =(CheckBox) page1.findViewById(R.id.checkBoxMeat);
		
		dish_name_text = (EditText) page1.findViewById(R.id.editTextSearchName);
		
		//initialize the reconnect button
        submit_button = (Button) page1.findViewById(R.id.buttonSubmitSearch);
		submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	refleshDish();
            	awesomePager.setCurrentItem(1);
            }
		});
        
		back_button = (Button) page2.findViewById(R.id.buttonBackSearch);
		back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	refleshDish();
            	awesomePager.setCurrentItem(0);
            }
		});		
		no_result_text = (TextView) page2.findViewById(R.id.textNoResultSearch);
		no_result_text.setVisibility(View.VISIBLE);
		timeout_text = (TextView) page2.findViewById(R.id.textTimeoutSearch);
		timeout_text.setVisibility(View.INVISIBLE);
		
        getDataThread = new Thread(new Runnable() {
    		public void run() {
    			while(true){
    				switch(thread_state2){
    				case INIT:
		    			dishes =new Vector<Bundle>();
		    			Dish cur_dish;
		    			dish_counter = 0;
		    			thread_state2 = FETCH;
    					Log.e("SearchFragment", "thread_state = FETCH");
		    			break;
    				case FETCH:
	    				cur_dish = api.search_dish(dish_counter,
	    						dish_name_text.getText().toString().length()==0?"any":dish_name_text.getText().toString(),
	    	    				canteen_search_name[canteen_spinner.getSelectedItemPosition()],
	    						taste[0].isChecked()?1:2,
	    						taste[1].isChecked()?1:2,
	    						taste[2].isChecked()?1:2,
	    						(time_spinner.getSelectedItemPosition()==4)||(time_spinner.getSelectedItemPosition()==0),
	    						(time_spinner.getSelectedItemPosition()==3)||(time_spinner.getSelectedItemPosition()==0),
	    						(time_spinner.getSelectedItemPosition()==2)||(time_spinner.getSelectedItemPosition()==0),
	    						(time_spinner.getSelectedItemPosition()==1)||(time_spinner.getSelectedItemPosition()==0));
	    				if(cur_dish==null){ //time out. Then delete all loaded dishes    					
	    					dishes.clear();
	    					thread_state2 = TIMEOUT;
	    					Log.e("SearchFragment", "thread_state = TIMEOUT");
	    					break;
	    				}
	    				if(cur_dish.getDish_id()==0) { //all dishes has been fetch
	    					thread_state2 = DISPLAY;
	    					Log.e("SearchFragment", "thread_state = DISPLAY");
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
		    					if(thread_state2 == TIMEOUT){
					    	        progressBar.setVisibility(View.INVISIBLE);
					    	        timeout_text.setVisibility(View.VISIBLE);
		    					}
		    					else{
		    						if(dish_counter == 0){ //the result is empty
			    						Log.e("SearchFragment", "In fetch, DISPLAY,empty");
						    	        progressBar.setVisibility(View.INVISIBLE);
						    	        no_result_text.setVisibility(View.VISIBLE);
		    						}
		    						else
		    						{
			    						Log.e("SearchFragment", "In fetch, DISPLAY,non empty");
						    	        adapter=new LazyAdapter(activity, dishes);
						    	        list.setAdapter(adapter);
		
						    	        progressBar.setVisibility(View.INVISIBLE);
		//					    	        progressBar.getLayoutParams().height=0;
		
						    	        list.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
						    	        list.setVisibility(View.VISIBLE);
		    						}
		    					}
		    				}
		    			});
		    			thread_state2 = WAIT;
		    			Log.e("SearchFragment", "thread_state = WAIT");
		    			break;
    				case WAIT:
    					break;
    				}
    			}
      		}
    	});
        thread_state2 = WAIT;
        Log.e("SearchFragment", "thread_state = WAIT");
        getDataThread.start();
      
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
    	
    	no_result_text.setVisibility(View.INVISIBLE);
    	timeout_text.setVisibility(View.INVISIBLE);
    	
    	progressBar.setVisibility(View.VISIBLE);
    	thread_state2 = INIT;
    	Log.e("SearchFragment", "thread_state = INIT");
	}
	
	private class AwesomePagerAdapter extends PagerAdapter{  
		  
        
        @Override  
        public int getCount() {  
            return mListViews.size();  
        }  
  
        /** 
         * 从指定的position创建page 
         * 
         * @param container ViewPager容器 
         * @param position The page position to be instantiated. 
         * @return 返回指定position的page，这里不需要是一个view，也可以是其他的视图容器. 
         */  
        @Override  
        public Object instantiateItem(View collection, int position) {  
  
              
            ((ViewPager) collection).addView(mListViews.get(position),0);  
              
            return mListViews.get(position);  
        }  
  
        /** 
         * <span style="font-family:'Droid Sans';">从指定的position销毁page</span> 
         *  
         *  
         *<span style="font-family:'Droid Sans';">参数同上</span> 
         */  
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
}