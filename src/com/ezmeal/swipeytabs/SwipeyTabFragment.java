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

package com.ezmeal.swipeytabs;

import com.ezmeal.main.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SwipeyTabFragment extends Fragment {
	
	public static SwipeyTabFragment newInstance(String title) {
		SwipeyTabFragment f = new SwipeyTabFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		f.setArguments(args);
		//setArguments:
		//Supply the construction arguments for this fragment. 
		//This can only be called before the fragment has been attached to its activity
		//in onCreateView, there is an Bundle argument
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_swipeytab, null);
		final String title = getArguments().getString("title");
		((TextView) root.findViewById(R.id.text)).setText(title);
		return root;
	}

}
