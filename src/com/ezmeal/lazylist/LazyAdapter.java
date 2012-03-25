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
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter{    
	private Activity activity;
    private String[] data;
    private String[] dishes_name;
    private String[] dishes_canteen;
    private Float[] dishes_price;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, String[] d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public LazyAdapter(Activity a, Vector<Bundle>dishes) {
        activity = a;
        data = new String[dishes.size()];
        dishes_name = new String[dishes.size()];
        dishes_canteen = new String[dishes.size()];
        dishes_price = new Float[dishes.size()];
        for(int i=0;i<dishes.size();i++){
        	Bundle one_dish = dishes.elementAt(i);
        	data[i]=mStrings[i];
        	dishes_name[i]=one_dish.getString("name");
        	dishes_canteen[i]=one_dish.getString("canteen");
        	dishes_price[i]=one_dish.getFloat("price");
        }
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

        TextView text1=(TextView)vi.findViewById(com.ezmeal.main.R.id.text1);
        TextView text2=(TextView)vi.findViewById(com.ezmeal.main.R.id.text2);
        TextView text3=(TextView)vi.findViewById(com.ezmeal.main.R.id.text3);
        ImageView image=(ImageView)vi.findViewById(com.ezmeal.main.R.id.image);
        /** TO BE MODIFIED **/
        text1.setText("$"+Float.toString(dishes_price[position]));
        text2.setText(dishes_name[position]);
        //text2.setText(Float.toString(dishes_price[position]));
        text3.setText(dishes_canteen[position]);
        /** END OF DEMO **/
        imageLoader.DisplayImage(data[position], image);
        OnClickListener itemListener=new OnClickListener(){
            public void onClick(View arg0) {
    			Intent intent = new Intent(activity.getApplicationContext(),
    					com.ezmeal.activity.DetailActivity.class);
    			Bundle dishInfo = new Bundle();
    			dishInfo.putInt("name", position);
    			dishInfo.putString("pic", data[position]);
    			intent.putExtras(dishInfo);
    			activity.startActivity(intent);
            }        	
        };
        vi.setOnClickListener(itemListener);
        return vi;
    }

    private String[] mStrings={
            "http://www.ezmealsonline.com/wp-content/uploads/2010/02/pork_milanese_dinner-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2010/02/hearty-baked-potato-soup-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2010/01/Ground-Beef-and-Potato-Tostada-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/11/pumpkin-cheescake-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/10/Beef-Tips-over-Egg-Noodles-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/10/carnitas-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/10/chicken-salad-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/10/chicken-pasta-bake-and-broiled-veggies-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/09/Oreo-Cake-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/09/mini-sirloin-burgers-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/09/enfrijoladas-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/09/baked-potatoes-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/09/pot-roast-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/04/weeknight-spaghetti-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/04/chili-mac-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/03/chicken-chow-mein-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/03/sweet-pork-loin-150x150.jpg",
            "http://www.ezmealsonline.com/wp-content/uploads/2009/03/caponata-panini-150x150.jpg",
            "http://a1.twimg.com/profile_images/63737974/2008-11-06_1637_normal.png",
            "http://a3.twimg.com/profile_images/548410609/icon_8_73.png",
            "http://a1.twimg.com/profile_images/612232882/nexusoneavatar_normal.jpg",
            "http://a1.twimg.com/profile_images/213722080/Bugdroid-phone_normal.png",
            "http://a1.twimg.com/profile_images/645523828/OT_icon_090918_android_normal.png",
            "http://a3.twimg.com/profile_images/64827025/android-wallpaper6_2560x160_normal.png",
            "http://a3.twimg.com/profile_images/77641093/AndroidPlanet.png",
            "http://a1.twimg.com/profile_images/850960042/elandroidelibre-logo_300x300_normal.jpg",
            "http://a1.twimg.com/profile_images/655119538/andbook_normal.png",
            "http://a3.twimg.com/profile_images/511790713/AG_normal.png",
            "http://a3.twimg.com/profile_images/956404323/androinica-avatar.png",
            "http://a1.twimg.com/profile_images/909231146/Android_Biz_Man_normal.png",
            "http://a3.twimg.com/profile_images/72774055/AndroidHomme-LOGO_normal.jpg",
            "http://a1.twimg.com/profile_images/349012784/android_logo_small_normal.jpg",
            "http://a1.twimg.com/profile_images/841338368/ea-twitter-icon_normal.png",
            "http://a3.twimg.com/profile_images/64827025/android-wallpaper6_2560x160_normal.png",
            "http://a3.twimg.com/profile_images/77641093/AndroidPlanet.png",
            "http://a3.twimg.com/profile_images/64827025/android-wallpaper6_2560x160_normal.png",
            "http://a3.twimg.com/profile_images/77641093/AndroidPlanet_normal.png",
            "http://a1.twimg.com/profile_images/850960042/elandroidelibre-logo_300x300.jpg",
            "http://a1.twimg.com/profile_images/655119538/andbook_normal.png",
            "http://a3.twimg.com/profile_images/511790713/AG_normal.png",
            "http://a3.twimg.com/profile_images/956404323/androinica-avatar_normal.png",
            "http://a1.twimg.com/profile_images/909231146/Android_Biz_Man_normal.png",
            "http://a3.twimg.com/profile_images/121630227/Droid.jpg",
            "http://a1.twimg.com/profile_images/957149154/twitterhalf_normal.jpg",
            "http://a1.twimg.com/profile_images/97470808/icon_normal.png",
            "http://a3.twimg.com/profile_images/511790713/AG_normal.png",
            "http://a3.twimg.com/profile_images/956404323/androinica-avatar_normal.png",
            "http://a1.twimg.com/profile_images/909231146/Android_Biz_Man.png",
            "http://a3.twimg.com/profile_images/72774055/AndroidHomme-LOGO_normal.jpg",
            "http://a1.twimg.com/profile_images/349012784/android_logo_small_normal.jpg",
            "http://a1.twimg.com/profile_images/841338368/ea-twitter-icon_normal.png",
            "http://a3.twimg.com/profile_images/64827025/android-wallpaper6_2560x160_normal.png",
            "http://a3.twimg.com/profile_images/77641093/AndroidPlanet.png",
            "http://a3.twimg.com/profile_images/670625317/aam-logo-v3-twitter_normal.png",
            "http://a3.twimg.com/profile_images/740897825/AndroidCast-350_normal.png",
            "http://a3.twimg.com/profile_images/121630227/Droid_normal.jpg",
            "http://a1.twimg.com/profile_images/957149154/twitterhalf_normal.jpg",
            "http://a1.twimg.com/profile_images/97470808/icon.png",
            "http://a3.twimg.com/profile_images/511790713/AG_normal.png",
            "http://a3.twimg.com/profile_images/956404323/androinica-avatar_normal.png",
            "http://a1.twimg.com/profile_images/909231146/Android_Biz_Man_normal.png",
            "http://a3.twimg.com/profile_images/72774055/AndroidHomme-LOGO_normal.jpg",
            "http://a1.twimg.com/profile_images/349012784/android_logo_small_normal.jpg",
            "http://a1.twimg.com/profile_images/841338368/ea-twitter-icon.png",
            "http://a3.twimg.com/profile_images/64827025/android-wallpaper6_2560x160_normal.png",
            "http://a3.twimg.com/profile_images/77641093/AndroidPlanet_normal.png",
            "http://a1.twimg.com/profile_images/850960042/elandroidelibre-logo_300x300_normal.jpg",
            "http://a1.twimg.com/profile_images/655119538/andbook_normal.png",
            "http://a3.twimg.com/profile_images/768060227/ap4u_normal.jpg",
            "http://a1.twimg.com/profile_images/74724754/android_logo.png",
            "http://a3.twimg.com/profile_images/681537837/SmallAvatarx150_normal.png",
            "http://a1.twimg.com/profile_images/63737974/2008-11-06_1637_normal.png",
            "http://a3.twimg.com/profile_images/548410609/icon_8_73_normal.png",
            "http://a1.twimg.com/profile_images/612232882/nexusoneavatar_normal.jpg",
            "http://a1.twimg.com/profile_images/213722080/Bugdroid-phone_normal.png",
            "http://a1.twimg.com/profile_images/645523828/OT_icon_090918_android.png",
            "http://a3.twimg.com/profile_images/64827025/android-wallpaper6_2560x160_normal.png",
            "http://a3.twimg.com/profile_images/77641093/AndroidPlanet_normal.png",
            "http://a1.twimg.com/profile_images/850960042/elandroidelibre-logo_300x300_normal.jpg",
            "http://a1.twimg.com/profile_images/655119538/andbook.png",
            "http://a3.twimg.com/profile_images/511790713/AG_normal.png",
            "http://a3.twimg.com/profile_images/956404323/androinica-avatar_normal.png",
            "http://a1.twimg.com/profile_images/909231146/Android_Biz_Man_normal.png",
            "http://a3.twimg.com/profile_images/72774055/AndroidHomme-LOGO_normal.jpg",
            "http://a1.twimg.com/profile_images/349012784/android_logo_small_normal.jpg",
            "http://a1.twimg.com/profile_images/841338368/ea-twitter-icon.png",
            "http://a3.twimg.com/profile_images/64827025/android-wallpaper6_2560x160_normal.png",
            "http://a3.twimg.com/profile_images/77641093/AndroidPlanet_normal.png",
            "http://a3.twimg.com/profile_images/64827025/android-wallpaper6_2560x160_normal.png",
            "http://a3.twimg.com/profile_images/77641093/AndroidPlanet_normal.png",
            "http://a1.twimg.com/profile_images/850960042/elandroidelibre-logo_300x300_normal.jpg",
            "http://a1.twimg.com/profile_images/655119538/andbook_normal.png",
            "http://a3.twimg.com/profile_images/511790713/AG_normal.png",
            "http://a3.twimg.com/profile_images/956404323/androinica-avatar_normal.png",
            "http://a1.twimg.com/profile_images/909231146/Android_Biz_Man_normal.png",
            "http://a3.twimg.com/profile_images/121630227/Droid_normal.jpg",
            "http://a1.twimg.com/profile_images/957149154/twitterhalf.jpg",
            "http://a1.twimg.com/profile_images/97470808/icon_normal.png",
            "http://a3.twimg.com/profile_images/511790713/AG_normal.png",
            "http://a3.twimg.com/profile_images/956404323/androinica-avatar_normal.png",
            "http://a1.twimg.com/profile_images/909231146/Android_Biz_Man_normal.png",
            "http://a3.twimg.com/profile_images/72774055/AndroidHomme-LOGO_normal.jpg",
            "http://a1.twimg.com/profile_images/349012784/android_logo_small.jpg",
            "http://a1.twimg.com/profile_images/841338368/ea-twitter-icon_normal.png",
            "http://a3.twimg.com/profile_images/64827025/android-wallpaper6_2560x160_normal.png",
            "http://a3.twimg.com/profile_images/77641093/AndroidPlanet_normal.png"
    };
}