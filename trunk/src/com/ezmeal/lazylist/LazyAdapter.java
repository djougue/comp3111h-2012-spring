package com.ezmeal.lazylist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, String[] d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
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

        TextView text1=(TextView)vi.findViewById(com.ezmeal.main.R.id.dishnameText);
        TextView text2=(TextView)vi.findViewById(com.ezmeal.main.R.id.priceText);
        ImageView image=(ImageView)vi.findViewById(com.ezmeal.main.R.id.image);
        /** TO BE MODIFIED **/
        text1.setText("dish "+position);
        text2.setText("10.0");
        /** END OF DEMO **/
        imageLoader.DisplayImage(data[position], image);
        OnClickListener itemListener=new OnClickListener(){
            public void onClick(View arg0) {
    			Intent intent = new Intent(activity.getApplicationContext(),
    					com.ezmeal.activity.DetailActivity.class);
    			intent.putExtra("value", position);
    			activity.startActivity(intent);
            }        	
        };
        vi.setOnClickListener(itemListener);
        return vi;
    }
}