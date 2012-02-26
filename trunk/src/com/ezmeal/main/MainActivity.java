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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ezmeal.activity.MenuFragment;
import com.ezmeal.activity.MyTasteActivity;
import com.ezmeal.activity.QuitActivity;
import com.ezmeal.activity.SettingsActivity;
import com.ezmeal.activity.ShakeActivity;
import com.ezmeal.main.R;

public class MainActivity extends FragmentActivity {

	private static final String [] TITLES = {
		"RANK",
		"MENU",
		"SEARCH"
	};
	

	private ViewPager mViewPager;
	private SwipeyTabs mTabs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_swipeytab);

		mTabs = (SwipeyTabs) findViewById(R.id.swipeytabs);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);

		SwipeyTabsPagerAdapter adapter = new SwipeyTabsPagerAdapter(this,
				getSupportFragmentManager());
		//getSupportFragmentManager:
		//	Return the FragmentManager for interacting with fragments associated with this activity.
		mViewPager.setAdapter(adapter);
		mTabs.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(mTabs);
		//set mTabs as the listener
		mViewPager.setCurrentItem(mTabs.getChildCount() / 2);  //By default, show the middle one of the tabs
		
		/**
		 * Button activities
		 */
		/* Settings button */
		Button settingsBt = (Button) findViewById(R.id.buttonSettings);
		settingsBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent settingsBtIntent = new Intent(view.getContext(), SettingsActivity.class);
                startActivityForResult(settingsBtIntent, 0);
            }
		});
		
		/* SHAKE button */
		Button shakeBt = (Button) findViewById(R.id.buttonShake);
		shakeBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent shakeBtIntent = new Intent(view.getContext(), ShakeActivity.class);
                startActivityForResult(shakeBtIntent, 0);
            }
		});
		
		/* My Taste button */
		Button myTasteBt = (Button) findViewById(R.id.buttonMyTaste);
		myTasteBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myTasteBtIntent = new Intent(view.getContext(), MyTasteActivity.class);
                startActivityForResult(myTasteBtIntent, 0);
            }
		});
		
		/* Quit button */
		Button quitBt = (Button) findViewById(R.id.buttonQuit);
		quitBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quitBtIntent = new Intent(view.getContext(), QuitActivity.class);
                startActivityForResult(quitBtIntent, 0);
            }
		});
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
			}
			return SwipeyTabFragment.newInstance(TITLES[position]);
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		public TextView getTab(final int position, SwipeyTabs root) {
			TextView view = (TextView) LayoutInflater.from(mContext).inflate(
					R.layout.swipey_tab_indicator, root, false);
			view.setText(TITLES[position]);
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mViewPager.setCurrentItem(position);
				}
			});
			
			return view;
		}

	}

}