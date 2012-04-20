package com.ezmeal.activity;

import java.sql.Timestamp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezmeal.lazylist.ImageLoader;
import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;
import com.ezmeal.server.Comment;
import com.ezmeal.server.Communication_API;
import com.ezmeal.shake.ShakeListener;

public class DetailActivity extends FragmentActivity implements OnClickListener, TextWatcher {

	ShakeListener mShaker;
    public ImageLoader imageLoader; 
    private Bundle the_dish;
    private int fatherActivity;
    private LinearLayout commentLayout;
    Communication_API api = new Communication_API();
    Handler refreshHandler = new Handler();
    
    private Button backBtn, sendBtn;
	private TextView headerTitle, labelNoComment, commentTitle, commentAuthor, commentTime,
	        commentContent, labelTitleCharRem, labelContentCharRem, sendResultText;
	private EditText writeTitle, writeContent;
	private ProgressBar sendProgressBar;
	private String dish_name, dish_price, dish_canteen;
	
	private int MAX_TITLE_LEN = 40;
	private int MAX_CONTENT_LEN = 140;
	private int titleCharRem, contentCharRem;

	private String TIMEOUT             = "Connection timeout.";
	private String EMPTY_TITLE         = "Title cannot be empty.";
	private String EMPTY_CONTENT       = "Comment cannot be empty.";
	private String SENDING             = "Sending...";
	private String SUCCESSFUL          = "Thanks for your comment!";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity_layout);
		the_dish = getIntent().getExtras();

/*		
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
*/
	}
	
	public void setView() {
		if (the_dish != null) {
	        fatherActivity = the_dish.getInt("ActivityFlag");
			dish_name = the_dish.getString("name");
			dish_canteen = the_dish.getString("canteen");
			dish_price = the_dish.getString("price");
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
	        
	        //set page header
			backBtn = (Button) findViewById(R.id.buttonBack);
	        backBtn.setOnClickListener(this);
	        headerTitle = (TextView) findViewById(R.id.labelHeader);
	        headerTitle.setText("Dish");
	        
	        //set comment section
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
	 * Update the number of characters remaining in the title and comment text fields.
	 */
	protected void updateCharRem() {
		titleCharRem = MAX_TITLE_LEN - writeTitle.length();
        contentCharRem = MAX_CONTENT_LEN - writeContent.length();
        labelTitleCharRem.setText(Integer.toString(titleCharRem));
        labelContentCharRem.setText(Integer.toString(contentCharRem));
	}
	
	/**
	 * Send comment to the server
	 */
	protected void sendComment() {
		//get text
		final String title = writeTitle.getText().toString().trim();
		final String content = writeContent.getText().toString().trim();
		final String username = ((UserApp) this.getApplication()).getUserName();
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
	    				}
	    			});
	    		}
	    	});
	    	postDataThread.start();
		}
		else {
			sendProgressBar.setVisibility(View.GONE);
		}
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
	 * Fetch and display users' comments
	 * @param dName = dish name
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

	/**
	 * OnClick Listener
	 */
	public void onClick(View view) {
		if (view == backBtn) {
			finish();
		}
		
		else if (view == sendBtn) {
			sendResultText.setTextColor(0xffffffff); //white
    		sendResultText.setText(SENDING);
    		sendProgressBar.setVisibility(View.VISIBLE);
    		sendComment();
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
}