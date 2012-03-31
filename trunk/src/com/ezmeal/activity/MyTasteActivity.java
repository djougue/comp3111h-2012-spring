package com.ezmeal.activity;

import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;
import com.ezmeal.server.Communication_API;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyTasteActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	private TextView headerTitle, resultText;
	private Button submitBtn, backBtn;
	private CheckBox spicyBtn, meatBtn, vegeBtn;
	private ProgressBar progressBar;
	private Thread postDataThread;
	private Handler refreshHandler;
	private static int serverResp;
	private boolean isChanged = false;
	
	private String TIMEOUT    = "Connection error. Please try again later.";
	private String LOADING    = "Loading...";
	private String SUCCESSFUL = "You have successfully changed your taste.";
	private String ERROR_MSG  = "Error occurred to My Taste modification.";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytaste);
        refreshHandler = new Handler();
        
        //set the header title
        headerTitle = (TextView) findViewById(R.id.labelHeader);
        headerTitle.setText("My Taste");
        
        //get check box
        spicyBtn = (CheckBox) findViewById(R.id.checkBoxMyTasteSpicy);
        meatBtn = (CheckBox) findViewById(R.id.checkBoxMyTasteMeat);
        vegeBtn = (CheckBox) findViewById(R.id.checkBoxMyTasteVege);
        
        //set check status of the check boxes
        boolean taste[] = ((UserApp) this.getApplication()).getTaste();
        spicyBtn.setChecked(taste[0]);
        meatBtn.setChecked(taste[1]);
        vegeBtn.setChecked(taste[2]);
        
        //set a listener to detect the change of the check boxes
        spicyBtn.setOnCheckedChangeListener(this);
        meatBtn.setOnCheckedChangeListener(this);
        vegeBtn.setOnCheckedChangeListener(this);
        
        //result
        progressBar = (ProgressBar) findViewById(R.id.progressBarMyTaste);
        resultText = (TextView) findViewById(R.id.labelMyTasteResult);
        
        //buttons
        submitBtn = (Button) findViewById(R.id.buttonMyTasteSubmit);
        submitBtn.setOnClickListener(this);
        backBtn = (Button) findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(this);
    }
    
    /**
     * Post My Taste form to the server.
     */
    private void postMyTasteData() {
    	//get input
    	final String uname = ((UserApp) this.getApplication()).getUserName();
    	final String nname = ((UserApp) this.getApplication()).getNickName();
    	final String passwd = ((UserApp) this.getApplication()).getPassword();
    	final boolean isSpicy = spicyBtn.isChecked();
    	final boolean isMeat = meatBtn.isChecked();
    	final boolean isVege = vegeBtn.isChecked();
    	final Activity thisActivity = this;
    	
    	postDataThread = new Thread(new Runnable() {
    		public void run() {
    			MyTasteActivity.serverResp =
    					Communication_API.change_user_setting(uname, passwd, nname, isSpicy?1:0, isVege?1:0, isMeat?1:0);
    			
    			//Refresh the data of this app
    			refreshHandler.post(new Runnable() {
    				public void run() {
    				    if(serverResp == 0) {
    				    	resultText.setTextColor(0xffffffff); //white
    		    			resultText.setText(TIMEOUT);
    				    }
    					else if (serverResp == 1) {
    		    			resultText.setTextColor(0xffffffff); //white
    		    			resultText.setText(SUCCESSFUL);
    		    			
    		    			//change local data
    		    			boolean[] taste = {isSpicy, isMeat, isVege};
    		    			((UserApp) thisActivity.getApplication()).setTaste(taste);
    		    			
    		    		} else {
    		    			resultText.setTextColor(0xffff0000); //red
    		    			resultText.setText(ERROR_MSG);
    		    		}
    		    		progressBar.setVisibility(View.GONE);
    				}
    			});
    		}
    	});
    	postDataThread.start();
	}
    
    /**
     * Click Listener
     */
    public void onClick(View view) {
    	if (view == submitBtn) {
        	resultText.setTextColor(0xffffffff); //white
    		resultText.setText(LOADING);
    		progressBar.setVisibility(View.VISIBLE);
    		postMyTasteData();
    	}
        else if (view == backBtn) {
    		//Pop up an alert dialog if something has been modified
    		if (isChanged) {
    			final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    			alertDialog.setTitle("Alert!");
    			alertDialog.setMessage("Are you sure to exit this page?");
    			alertDialog.setButton("Yes, please.", new DialogInterface.OnClickListener() {
    			      public void onClick(DialogInterface dialog, int which) {
    			    	  finish();
    			    } });
    			alertDialog.setButton2("Oops, NO!", new DialogInterface.OnClickListener() {
  			      public void onClick(DialogInterface dialog, int which) {
  			    	  alertDialog.dismiss();
  			        } });
    			alertDialog.show();
    		}
    		
    		//Else, directly finish the activity, and go back to the welcome page.
    		else {
    		    finish();
    		}
    	}
    }

	public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
		isChanged = true;
	}
}