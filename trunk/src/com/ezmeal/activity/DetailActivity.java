package com.ezmeal.activity;

import java.sql.Timestamp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.ezmeal.lazylist.ImageLoader;
import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;
import com.ezmeal.server.Comment;
import com.ezmeal.server.Communication_API;
import com.ezmeal.server.Dish;
import com.ezmeal.shake.ShakeListener;
import com.ezmeal.uploadimage.ImageUpload;

public class DetailActivity extends FragmentActivity implements OnClickListener, OnRatingBarChangeListener, TextWatcher {

	ShakeListener mShaker;
    private ImageLoader imageLoader; 
    private Bundle the_dish_bundle;
    private Dish the_dish;
    private int fatherActivity;
    private LinearLayout commentLayout;
    private Communication_API api = new Communication_API();
    private Handler refreshHandler = new Handler();
    
    private LinearLayout uploadLayout;
    private Button backBtn, sendBtn, uploadBtn;
	private TextView headerTitle, labelNoComment, commentTitle, commentAuthor, commentTime,
	        commentContent, labelTitleCharRem, labelContentCharRem, sendResultText,
	        ratingNum, ratingLevel; //ratingScore;
	private EditText writeTitle, writeContent;
	private ProgressBar sendProgressBar;
	private String dish_name, dish_price, dish_canteen;
	private int dish_id;
	private int position;
	
	private ImageView image;
	private LinearLayout images;

	private ImageView ratingStars;
	private RatingBar ratingBar;

	
	private int MAX_TITLE_LEN = 40;
	private int MAX_CONTENT_LEN = 140;
	private int FULL_RATING_SCORE = 5;
	private int titleCharRem, contentCharRem;

	private String TIMEOUT       = "Connection timeout.";
	private String EMPTY_TITLE   = "Title cannot be empty.";
	private String EMPTY_CONTENT = "Comment cannot be empty.";
	private String SENDING       = "Sending...";
	private String SUCCESSFUL    = "Thanks for your comment!";
	
	private String[] RATING_LEVELS = {"I didn't rate", "I hate it", "I dislike it", "It's fair", "It's good", "It's excellent!"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity_layout);
		the_dish_bundle = getIntent().getExtras();

		
		mShaker = new ShakeListener(this);
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

	}
	
	public void setView() {
		if (the_dish_bundle != null) {
	        fatherActivity = the_dish_bundle.getInt("ActivityFlag");
	        position = the_dish_bundle.getInt("position");
	        the_dish = new Dish(the_dish_bundle);
			dish_name = the_dish.getDish_name();
			dish_canteen = the_dish.getDish_canteen();
			dish_price = Float.toString(the_dish.getDish_price());
			dish_id = the_dish.getDish_id();
			
			TextView name = (TextView) findViewById(R.id.textDishNameInDetail);
			name.setText(dish_name);
			TextView canteen = (TextView) findViewById(R.id.textDishCanteenInDetail);
			canteen.setText(dish_canteen);
			TextView price = (TextView) findViewById(R.id.textDishPriceInDetail);
			price.setText("HKD "+dish_price);
			image = (ImageView) findViewById(R.id.dishImage);
			String pic = "http://143.89.220.19/COMP3111H/image/stub.png";
			if(the_dish.hasImage())
				pic ="http://143.89.220.19/COMP3111H/image/"+the_dish.getDish_id()+".jpg";
	        imageLoader=new ImageLoader(this.getApplicationContext());
	        imageLoader.DisplayImage(pic, image);
	        
	        uploadLayout = (LinearLayout) findViewById(R.id.layoutUploadImage);
	        if(the_dish.hasImage())
	        	uploadLayout.getLayoutParams().height=0;
	        //set page header
			backBtn = (Button) findViewById(R.id.buttonBack);
	        backBtn.setOnClickListener(this);
	        headerTitle = (TextView) findViewById(R.id.labelHeader);
	        headerTitle.setText("Dish");
	        
	        //set rating section
	        //ratingScore = (TextView) findViewById(R.id.labelRatingScore);
	        ratingStars = (ImageView) findViewById(R.id.imageRatingStars);
	        ratingNum = (TextView) findViewById(R.id.labelRatingNum);
	        ratingLevel = (TextView) findViewById(R.id.labelRatingLevel);
	        ratingBar = (RatingBar) findViewById(R.id.ratingBarGiveRating);
	        ratingBar.setOnRatingBarChangeListener(this);
	        ratingBar.setOnClickListener(this);
	        displayRating();
	        
	        //set comment section
	        uploadBtn = (Button)findViewById(R.id.buttonUploadImage);
	        uploadBtn.setOnClickListener(this);
	        
	        images = (LinearLayout) findViewById(R.id.imageboxDetail);
	        if(!the_dish.isDish_spicy()){
	        	ImageView spicyImage = (ImageView)findViewById(com.ezmeal.main.R.id.iconSpicyDetail);
	        	images.removeView(spicyImage);
	        }
	        if(!the_dish.isDish_vege()){
	        	ImageView vegaImage = (ImageView)findViewById(com.ezmeal.main.R.id.iconVegaDetail);
	        	images.removeView(vegaImage);
	        }
	        if(!the_dish.isDish_meat()){
	        	ImageView meatImage = (ImageView)findViewById(com.ezmeal.main.R.id.iconMeatDetail);
	        	images.removeView(meatImage);
	        }

	        
	        sendBtn = (Button) findViewById(R.id.buttonCommentSend);
	        sendBtn.setOnClickListener(this);
	        labelTitleCharRem = (TextView) findViewById(R.id.labelCommentTitleCharRemaining);
	        labelContentCharRem = (TextView) findViewById(R.id.labelCommentContentCharRemaining);
	        writeTitle = (EditText) findViewById(R.id.editTextCommentTitle);
	        writeTitle.addTextChangedListener(this);
	        writeContent = (EditText) findViewById(R.id.editTextCommentContent);
	        writeContent.addTextChangedListener(this);
	        sendProgressBar = (ProgressBar) findViewById(R.id.progressBarWriteComment);
	        sendResultText = (TextView) findViewById(R.id.labelWriteCommentResult);
	        commentLayout = (LinearLayout) findViewById(R.id.layoutComment);
	        labelNoComment = (TextView) findViewById(R.id.labelCommentNoResult);
	        updateCharRem();
	        
	        //load comments on the dish
	        displayComment();
		}
		else{
			TextView view = (TextView) findViewById(R.id.textDishNameInDetail);
			view.setText("Fail");				
		}
	}
	
	/**
	 * Fetch and display overall rating
	 */
	protected void displayRating() {
		final Activity thisActivity = this;
		Thread postDataThread = new Thread(new Runnable() {
			public void run() {
				final float score = api.fetch_rate(dish_id);
				final int numRatings = api.fetch_number_of_rater(dish_id);
				String user_name = ((UserApp)thisActivity.getApplication()).getUserName();
				final int userRating = api.fetch_user_rate(user_name, dish_id);
				Bitmap sourceStar = BitmapFactory.decodeResource(getResources(), R.drawable.stars_full);
				int imgWidth = (int) ((score/FULL_RATING_SCORE) * sourceStar.getWidth());
				int imgHeight = (int) sourceStar.getHeight();
				imgWidth = (imgWidth <= 0) ? 1 : imgWidth;  //guarantee image width is larger than 0
				final Bitmap star = Bitmap.createBitmap(sourceStar, 0, 0, imgWidth, imgHeight);
				
				//Refresh the data of this app
    			refreshHandler.post(new Runnable() {
    				public void run() {
    					//Maintain one fraction for the overall rating score
    					if (score > 0) ratingStars.setImageBitmap(star);
    					if (score == 1) ratingNum.setText(Integer.toString(numRatings) + " rating");
    					else ratingNum.setText(Integer.toString(numRatings) + " ratings");
    					/*
    					BigDecimal bd = new BigDecimal(score);
    					String scoreStr = bd.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    					if (score == -1) ratingScore.setText("N/A");
    					else ratingScore.setText(scoreStr);
    					*/
    					
    					//Set user rating
    					setUserRating(userRating);
    				}
    			});
			}
		});
		postDataThread.start();
	}
	
	/**
	 * Set the rating bar
	 * @param rating = rating given by the user
	 */
	protected void setUserRating(int rating) {
		rating = (rating >= 0) ? rating : 0;
		ratingBar.setRating((float) rating);
		ratingLevel.setText(RATING_LEVELS[rating]);
	}
	
	/**
	 * Send the latest updated user rating to the server
	 * @param rating
	 */
	protected void sendNewRating(int rating) {
		final String user_name = ((UserApp)this.getApplication()).getUserName();
		final int rate = rating;
		Thread postDataThread = new Thread(new Runnable() {
			public void run() {
				api.rate_dish(user_name, dish_id, rate);
				refreshHandler.post(new Runnable() {
    				public void run() {
    					displayRating();
    				}
				});
			}
		});
		postDataThread.start();
	}

	
	/**
	 * Update the number of characters remaining in the title and comment text fields.
	 */
	protected void updateCharRem() {
		titleCharRem = MAX_TITLE_LEN - writeTitle.length();
        contentCharRem = MAX_CONTENT_LEN - writeContent.length();
        labelTitleCharRem.setText(Integer.toString(titleCharRem));
        labelContentCharRem.setText(Integer.toString(contentCharRem));
	}
	
	/**
	 * Check if the comment is valid. An invalid comment has either an empty title or
	 * an empty content (including title or content that has only spaces).
	 * @param title = comment title
	 * @param content = comment content
	 * @return true if the comment is valid
	 */
	protected boolean checkComment(String title, String content) {
		sendResultText.setTextColor(0xffff0000);  //red
		if (title.trim().length() == 0) {
			sendResultText.setText(EMPTY_TITLE);
			return false;
		}
		else if (content.trim().length() == 0) {
			sendResultText.setText(EMPTY_CONTENT);
			return false;
		}
		sendResultText.setTextColor(0xffffffff); //white
		return true;
	}
	
	/**
	 * Send comment to the server
	 */
	protected void sendComment() {
		//get text
		final String title = writeTitle.getText().toString().trim();
		final String content = writeContent.getText().toString().trim();
		final String username = ((UserApp) this.getApplication()).getUserName();
		
		//check if the comment title or content is valid
		if (checkComment(title, content)) {
			Thread postDataThread = new Thread(new Runnable() {
	    		public void run() {
	    			api.add_comment(title, content, username, dish_name);
	    			
	    			//Refresh the data of this app
	    			refreshHandler.post(new Runnable() {
	    				public void run() {
	    		    		if (api.isConnectionTimeout()) {
	    		    			sendResultText.setTextColor(0xffff0000); //red
	    		    			sendResultText.setText(TIMEOUT);
	    		    		}
	    		    		else {
	    		    			//finish the register activity (this)
	    		    			sendResultText.setTextColor(0xffffffff); //white
	    		    			sendResultText.setText(SUCCESSFUL);
	    		    			labelNoComment.setVisibility(View.GONE);
	    		    			
	    		    			//clear the text fields
	    		    			writeTitle.setText("");
	    		    			writeContent.setText("");
	    		    			
	    		    			/*
	    		    			 * Display the submitted comment
	    		    			 */
	    		    			LayoutInflater vi = (LayoutInflater) getApplicationContext().
	        	    	        		getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        					View v = vi.inflate(R.layout.read_comment, null);
	        					
	        					//get time
	        					java.util.Date date = new java.util.Date();
	        					Timestamp fullTime = new Timestamp(date.getTime());
	        					String time = fullTime.toString().substring(0, fullTime.toString().length()-7);
	        					
	        					//set comment info and contents
	        					commentTitle = (TextView) v.findViewById(R.id.labelCommentTitle);
	        					commentTitle.setText(title);
	        					commentAuthor = (TextView) v.findViewById(R.id.labelCommentAuthor);
	        					commentAuthor.setText(username);
	        					commentTime = (TextView) v.findViewById(R.id.labelCommentTime);
	        					commentTime.setText(time);
	        					commentContent = (TextView) v.findViewById(R.id.labelCommentContent);
	        					commentContent.setText(content);
	        					
	            	           	// insert into the beginning of Users' Comments section
	            	            commentLayout.addView(v, 0, new ViewGroup.LayoutParams(
	            	            		ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
	    		    		}
	    		    		sendProgressBar.setVisibility(View.GONE);
	    		    		sendBtn.setEnabled(true);
	    				}
	    			});
	    		}
	    	});
	    	postDataThread.start();
		}
		else {
			sendProgressBar.setVisibility(View.GONE);
			sendBtn.setEnabled(true);
		}
	}
	
	
	/**
	 * Fetch and display users' comments
	 */
	protected void displayComment() {
        Thread postDataThread = new Thread(new Runnable() {
    		public void run() {
    	        int dish_num = 0;
    	        while (dish_num < 20) {  //display the most recent 20 comments
    	        	Comment cm = api.fetch_comment(dish_num, dish_name);
    	        	
    	        	if (cm == null)  //All comments have been loaded
    	        		break;
    	        	
    	        	//get comment detail
    	        	final String title = cm.getTitle();
    	        	final String author = cm.getAuthor();
    	        	final String content = cm.getContent();
    	        	String fullTime = cm.getTime().toString();
    	        	final String time = fullTime.substring(0, fullTime.length()-5);  //accurate to minutes
    	        	final int index = dish_num;   //display order
    	        	
    	        	//Refresh the data of this app
        			refreshHandler.post(new Runnable() {
        				public void run() {
        					LayoutInflater vi = (LayoutInflater) getApplicationContext().
        	    	        		getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        					View v = vi.inflate(R.layout.read_comment, null);
        					
        					//set comment info and contents
        					commentTitle = (TextView) v.findViewById(R.id.labelCommentTitle);
        					commentTitle.setText(title);
        					commentAuthor = (TextView) v.findViewById(R.id.labelCommentAuthor);
        					commentAuthor.setText(author);
        					commentTime = (TextView) v.findViewById(R.id.labelCommentTime);
        					commentTime.setText(time);
        					commentContent = (TextView) v.findViewById(R.id.labelCommentContent);
        					commentContent.setText(content);
        					
            	           	// insert into main view
            	            commentLayout.addView(v, index, new ViewGroup.LayoutParams(
            	            		ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        				}
        			});
    	            dish_num++;
    	        }
    	        if (dish_num == 0) {  //No comments fetched for the dish
    	        	//Refresh the data of this app
        			refreshHandler.post(new Runnable() {
        				public void run() {
    	        	        labelNoComment.setVisibility(View.VISIBLE);
        				}
        			});
    	        }
    		}
    	});
    	postDataThread.start();
	}

	@Override
	protected void onPause(){
		mShaker.pause();
		super.onPause();
	}
	
	@Override
	protected void onResume(){
		mShaker.resume();
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

	/**
	 * OnClick Listener
	 */
	public void onClick(View view) {
		if (view == backBtn) {
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
		
		else if (view == sendBtn) {
			sendBtn.setEnabled(false);  //disable the button for a while, in case of innocent pressing
			sendResultText.setTextColor(0xffffffff); //white
    		sendResultText.setText(SENDING);
    		sendProgressBar.setVisibility(View.VISIBLE);
    		sendComment();
		}
		
		else if(view == uploadBtn) {
			Intent intent = new Intent(view.getContext(), ImageUpload.class);
			Bundle dishInfo = new Bundle();
			dishInfo.putInt("id", the_dish.getDish_id());
			intent.putExtras(dishInfo);
            startActivityForResult(intent, 10);
		}
	}

	/**
	 * TextWatch Listener
	 */
	public void afterTextChanged(Editable ed) {
		updateCharRem();
	}
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		//do nothing
	}
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		//do nothing
	}

	private void checkImage(){
		Log.e("Detail","checkImage");
        if(the_dish.hasImage()){
        	uploadLayout.getLayoutParams().height=0;
        	String pic ="http://143.89.220.19/COMP3111H/image/"+the_dish.getDish_id()+".jpg";
	        imageLoader.DisplayImage(pic, image);
        }
	}


	/**
	 * OnRatingBarChanged Listener
	 */
	public void onRatingChanged(RatingBar rb, float rating, boolean fromUser) {
		if (rb == ratingBar) {
			sendNewRating((int) rb.getRating());
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,  
            Intent data){  
		switch (resultCode){  
		case RESULT_OK:  
			Log.e("Detail","back");
			Bundle b = data.getExtras();  
			Boolean result = b.getBoolean("Result");  
			if(result !=null && result){
				Log.e("Detail","return true");
				the_dish.setDish_image(true);
				Intent i = new Intent();
				Bundle c = new Bundle();
				c.putBoolean("isChanged",true);
				c.putInt("position", position);
				c.putInt("id", the_dish.getDish_id());
				i.putExtras(c);
				setResult(10,i);//10 is just an id
				checkImage();
			}
			break;
		default:
			break;
		}  
	} 
}

