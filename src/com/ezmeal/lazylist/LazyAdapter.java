package com.ezmeal.lazylist;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezmeal.server.Dish;

public class LazyAdapter extends BaseAdapter{    
	private Activity activity;
    private String[] data;
    private String[] dishes_name;
    private String[] dishes_canteen;
    private Float[] dishes_price;
    private boolean isEmpty;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    private Vector<Dish> input;
    /*
    public LazyAdapter(Activity a, String[] d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
	*/
    public LazyAdapter(Activity a, Vector<Dish>dishes) {
    	activity = a;
    	if(dishes.isEmpty()){
    		isEmpty = true;
        }
        else{
        	isEmpty = false;
        	input = dishes;
	        data = new String[dishes.size()];
	        dishes_name = new String[dishes.size()];
	        dishes_canteen = new String[dishes.size()];
	        dishes_price = new Float[dishes.size()];
	        for(int i=0;i<dishes.size();i++){
	        	Dish one_dish = dishes.elementAt(i);
	        	if(one_dish.hasImage())
	        		data[i] = "http://143.89.220.19/COMP3111H/image/"+one_dish.getDish_id()+".jpg";
	        	else	        		
	        		data[i]="http://143.89.220.19/COMP3111H/image/stub.png";
	        	dishes_name[i]=one_dish.getDish_name();
	        	dishes_canteen[i]=one_dish.getDish_canteen();
	        	dishes_price[i]=one_dish.getDish_price();
	        }
        
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        }
    }

    public int getCount() {
    	if(isEmpty)
    		return 0;
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(com.ezmeal.main.R.layout.menu_item, null);
        if(isEmpty)
        	return null;
        TextView text1=(TextView)vi.findViewById(com.ezmeal.main.R.id.text1);
        TextView text2=(TextView)vi.findViewById(com.ezmeal.main.R.id.text2);
        TextView text3=(TextView)vi.findViewById(com.ezmeal.main.R.id.text3);
        ImageView image=(ImageView)vi.findViewById(com.ezmeal.main.R.id.image);
        
        text1.setText("$"+Float.toString(dishes_price[position]));
        text2.setText(dishes_name[position]);
        //text2.setText(Float.toString(dishes_price[position]));
        text3.setText(dishes_canteen[position]);
        
        LinearLayout images=(LinearLayout) vi.findViewById(com.ezmeal.main.R.id.imagebox);
        
        if(!input.elementAt(position).isDish_spicy()){
        	ImageView spicyImage = (ImageView)vi.findViewById(com.ezmeal.main.R.id.iconSpicy);
        	images.removeView(spicyImage);
        }
        if(!input.elementAt(position).isDish_vege()){
        	ImageView vegaImage = (ImageView)vi.findViewById(com.ezmeal.main.R.id.iconVega);
        	images.removeView(vegaImage);
        }
        if(!input.elementAt(position).isDish_meat()){
        	ImageView meatImage = (ImageView)vi.findViewById(com.ezmeal.main.R.id.iconMeat);
        	images.removeView(meatImage);
        }
        
        imageLoader.DisplayImage(data[position], image);

        OnClickListener itemListener=new OnClickListener(){
            public void onClick(View arg0) {
    			Intent intent = new Intent(activity.getApplicationContext(),
    					com.ezmeal.activity.DetailActivity.class);
    			Bundle dishInfo = input.elementAt(position).dishToBundle();
/*    			dishInfo.putString("name", dishes_name[position]);
    			dishInfo.putString("pic", data[position]);
    			dishInfo.putString("price", Float.toString(dishes_price[position]));
    			dishInfo.putString("canteen", dishes_canteen[position]);

    			dishInfo.putString("canteen", dishes_canteen[position]);
*/
    			dishInfo.putInt("ActivityFlag", 0);
    			intent.putExtras(dishInfo);
    			activity.startActivity(intent);
            }        	
        };
        vi.setOnClickListener(itemListener);
        return vi;
    }
}