/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ezmeal.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ezmeal.activity.MenuFragment;
import com.ezmeal.activity.MyTasteActivity;
import com.ezmeal.activity.QuitActivity;
import com.ezmeal.activity.SearchFragment;
import com.ezmeal.activity.SettingsActivity;
import com.ezmeal.activity.ShakeActivity;
import com.ezmeal.shake.ShakeListener;
import com.ezmeal.swipeytabs.SwipeyTabFragment;
import com.ezmeal.swipeytabs.SwipeyTabs;
import com.ezmeal.swipeytabs.SwipeyTabsAdapter;

public class MainActivity extends FragmentActivity {

	private static final String [] TITLES = {
		"RANK",
		"MENU",
		"SEARCH"
	};

	private ViewPager mViewPager;
	private SwipeyTabs mTabs;
	ShakeListener mShaker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.e("MainActivity", "creating MainActivity");
		super.onCreate(savedInstanceState);

		setContentView(com.ezmeal.main.R.layout.activity_swipeytab);
		Log.e("MainActivity", "setting view");

		mTabs = (SwipeyTabs) findViewById(com.ezmeal.main.R.id.swipeytabs);

		mViewPager = (ViewPager) findViewById(com.ezmeal.main.R.id.viewpager);

		SwipeyTabsPagerAdapter adapter = new SwipeyTabsPagerAdapter(this,
				getSupportFragmentManager());
		
		//getSupportFragmentManager:
		//	Return the FragmentManager for interacting with fragments associated with this activity.
		mViewPager.setAdapter(adapter);
		mTabs.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(mTabs);
		//set mTabs as the listener
		mViewPager.setCurrentItem(mTabs.getChildCount() / 2);  //By default, show the middle one of the tabs
		Log.e("MainActivity", "setting viewPager");

/*
		mShaker = new ShakeListener(this);		
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {  
		    public void onShake() {  
	   			Intent intent = new Intent(getApplicationContext(),
    					com.ezmeal.activity.ShakeActivity.class);
	   			startActivity(intent);
		    }  
		});
*/
		Log.e("MainActivity", "setting shaking");

		/**
		 * Button activities
		 */
		Button settingsBt = (Button) findViewById(com.ezmeal.main.R.id.buttonSettings);
		settingsBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent settingsBtIntent = new Intent(view.getContext(), SettingsActivity.class);
                startActivityForResult(settingsBtIntent, 0);
            }
		});
		
		
		Button shakeBt = (Button) findViewById(com.ezmeal.main.R.id.buttonShake);
		shakeBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent shakeBtIntent = new Intent(view.getContext(), ShakeActivity.class);
                startActivityForResult(shakeBtIntent, 0);
            }
		});
		
		
		Button myTasteBtn = (Button) findViewById(com.ezmeal.main.R.id.buttonMyTaste);
		myTasteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myTasteBtnIntent = new Intent(view.getContext(), MyTasteActivity.class);
                startActivityForResult(myTasteBtnIntent, 0);
            }
		});
		
		
		Button quitBt = (Button) findViewById(com.ezmeal.main.R.id.buttonQuit);
		quitBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent quit = new Intent(view.getContext(), QuitActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(quit);
            	finish();
            }
		});

		/*		
		Button settingsBt = (Button) findViewById(com.ezmeal.main.R.id.buttonSettings);
		settingsBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent settingsBtIntent = new Intent(view.getContext(), SettingsActivity.class);
                startActivityForResult(settingsBtIntent, 0);
            }
		});
		
		
		Button shakeBt = (Button) findViewById(com.ezmeal.main.R.id.buttonShake);
		shakeBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent shakeBtIntent = new Intent(view.getContext(), ShakeActivity.class);
                startActivityForResult(shakeBtIntent, 0);
            }
		});
		
		
		Button myTasteBtn = (Button) findViewById(com.ezmeal.main.R.id.buttonMyTaste);
		myTasteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myTasteBtnIntent = new Intent(view.getContext(), MyTasteActivity.class);
                startActivityForResult(myTasteBtnIntent, 0);
            }
		});
		
		
		Button quitBt = (Button) findViewById(com.ezmeal.main.R.id.buttonQuit);
		quitBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent quit = new Intent(view.getContext(), QuitActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(quit);
            	finish();
            }
		});
*/
	}
	
	
	/**
	 * SwipeyTabsPagerAdapter
	 */
	private class SwipeyTabsPagerAdapter extends FragmentPagerAdapter implements
			SwipeyTabsAdapter {
		
		private final Context mContext;

		public SwipeyTabsPagerAdapter(Context context, FragmentManager fm) {
			super(fm);

			this.mContext = context;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 1:
				MenuFragment mFragment = new MenuFragment();
				return mFragment;
			case 2:
				SearchFragment sFragment = new SearchFragment();
				return sFragment;
			}
			return SwipeyTabFragment.newInstance(TITLES[position]);
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		public TextView getTab(final int position, SwipeyTabs root) {
			TextView view = (TextView) LayoutInflater.from(mContext).inflate(
					com.ezmeal.main.R.layout.swipey_tab_indicator, root, false);
			view.setText(TITLES[position]);
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mViewPager.setCurrentItem(position);
				}
			});
			
			return view;
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
		Intent intent = new Intent(getApplicationContext(),
				com.ezmeal.main.WelcomeActivity.class);
		startActivity(intent);
		finish();
		return;
	}	
}