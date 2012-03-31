package com.ezmeal.activity;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

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
	private Vector<Bundle> dishes;

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
		
        getDataThread = new Thread(new Runnable() {
    		public void run() {
    			dishes =new Vector<Bundle>();
    			Dish cur_dish;
    			int _index = 0;
    			while(true){
    				cur_dish = Communication_API.fetch_dish(_index);
    				
    				if(cur_dish==null){ //time out. Then delete all loaded dishes
    					dishes.clear();
    					break;
    				}
    				if(cur_dish.getDish_id()==0) break;
    				_index++;
    				Bundle bundle = new Bundle();
    				bundle.putString("name", cur_dish.getDish_name());
    				bundle.putString("canteen", cur_dish.getDish_canteen());
    				bundle.putFloat("price", cur_dish.getDish_price());
    				dishes.add(bundle);
    			}

    			refreshHandler.post(new Runnable() {
    				public void run() {						
							// TODO Auto-generated method stub													
		    	        adapter=new LazyAdapter(activity, dishes);
		    	        list.setAdapter(adapter);
		    	        progressBar.setVisibility(View.INVISIBLE);
		    	        progressBar.getLayoutParams().height=0;
		    	        list.setVisibility(View.VISIBLE);    	          
    				}
    			});
      		}
    	});
        getDataThread.start();
        
		return view;
	}
	/*
    public OnClickListener listener=new OnClickListener(){
        public void onClick(View arg0) {
            adapter.imageLoader.clearCache();
            adapter.notifyDataSetChanged();
        }
    };*/
 }
