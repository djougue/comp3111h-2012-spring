package com.ezmeal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezmeal.lazylist.ImageLoader;
import com.ezmeal.main.R;
import com.ezmeal.shake.ShakeListener;

public class DetailActivity extends FragmentActivity implements OnClickListener {

	ShakeListener mShaker;
    public ImageLoader imageLoader; 
    private Bundle the_dish;
    private int fatherActivity;
    
    private Button backBtn;
	private TextView headerTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity_layout);
		the_dish = getIntent().getExtras();

/*		
		mShaker = new ShakeListener(this);		
>>>>>>> .r344
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {  
		    public void onShake() {
		    	if(fatherActivity==0)
		    	{
		   			Intent intent = new Intent(getApplicationContext(),
	    					com.ezmeal.activity.ShakeActivity.class);
		   			startActivity(intent);
		    	}
		    	else if(fatherActivity==1){
		   			Intent intent = new Intent(getApplicationContext(),
	    					com.ezmeal.activity.ShakeActivity.class);
		   			startActivity(intent);
		    		finish();
		    	}
 		    }  
		});
*/
	}
	
	void setView() {
		if (the_dish != null) {
	        fatherActivity = the_dish.getInt("ActivityFlag");
			String dish_name = the_dish.getString("name");
			String dish_canteen = the_dish.getString("canteen");
			String dish_price = the_dish.getString("price");
			TextView name = (TextView) findViewById(R.id.textDishNameInDetail);
			name.setText(dish_name);
			TextView canteen = (TextView) findViewById(R.id.textDishCanteenInDetail);
			canteen.setText(dish_canteen);
			TextView price = (TextView) findViewById(R.id.textDishPriceInDetail);
			price.setText(dish_price);
			ImageView image = (ImageView) findViewById(R.id.dishImage);
			String pic = the_dish.getString("pic");
	        imageLoader=new ImageLoader(this.getApplicationContext());
	        imageLoader.DisplayImage(pic, image);
	        //set the header
			backBtn = (Button) findViewById(R.id.buttonBack);
	        backBtn.setOnClickListener(this);
	        headerTitle = (TextView) findViewById(R.id.labelHeader);
	        headerTitle.setText("Dish");
		}
		else{
			TextView view = (TextView) findViewById(R.id.textDishNameInDetail);
			view.setText("Fail");				
		}
	}

	@Override
	protected void onPause(){
		//mShaker.pause();
		super.onPause();
	}
	
	@Override
	protected void onResume(){
		//mShaker.resume();
		super.onResume();
	}
	
	@Override
	public void onBackPressed() {
    	if(fatherActivity==0)
    	{
    		finish();
    	}
    	else if(fatherActivity==1){
   			Intent intent = new Intent(getApplicationContext(),
					com.ezmeal.activity.ShakeActivity.class);
   			startActivity(intent);
    		finish();
    	}		
		return;
	}

	public void onClick(View view) {
		if (view == backBtn) {
			finish();
		}
	}
}