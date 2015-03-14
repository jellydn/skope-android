/*
 * Copyright (C) 2014 The Asian Tech. All rights reserved
 * WeightManagement Project
 */
package dev.skope.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
/**
 * @category BaseFragment
 * @author TienLQ
 * @version 1.0.0
 */
public class BaseFragment extends Fragment {

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() 
					+ " must implement OnBaseFragmentListener");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
}
