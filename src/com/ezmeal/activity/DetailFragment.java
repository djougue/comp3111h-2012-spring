package com.ezmeal.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezmeal.main.R;

public class DetailFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("Test", "hello");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((DetailActivity)this.getActivity()).setView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.details2, container, false);
		return view;
	}
/*
	public void setText(String item) {
		TextView view = (TextView) getView().findViewById(R.id.textDishNameInDetail);
		view.setText(item);
	}
	*/
}
