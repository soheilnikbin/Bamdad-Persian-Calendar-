package com.omninotes;

import android.support.v4.app.Fragment;

import com.omninotes.helpers.AnalyticsHelper;



public class BaseFragment extends Fragment {


	@Override
	public void onStart() {
		super.onStart();
		AnalyticsHelper.trackScreenView(getClass().getName());
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
