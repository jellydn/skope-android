/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

package com.productsway.skope.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserProfileSingleton {
	//
	public static final String END_POINT = "http://appyeverafter.co.za/ws/";// "http://bptmedia.com/appyeverafter/";

	//
	public static String PREFERENCES = "SkopeConfig";

	private final SharedPreferences mSettings;
	private final SharedPreferences.Editor mEditor;

	private static UserProfileSingleton mInstance = null;

	private String mFBAcessToken;

	//
	private static final String FB_ACCESS_TOKEN = "FB_ACCESS_TOKEN";

	public static synchronized UserProfileSingleton getConfig(Context context) {
		if (mInstance == null) {
			mInstance = new UserProfileSingleton(context);
		}
		return mInstance;
	}

	private UserProfileSingleton(Context context) {
		mSettings = context.getSharedPreferences(PREFERENCES, 0);
		mEditor = mSettings.edit();

		mFBAcessToken = mSettings.getString(FB_ACCESS_TOKEN, "");
	}

	public String getFBAcessToken() {
		return mFBAcessToken;
	}

	public void setFbAccessToken(String v) {
        mFBAcessToken = v;
		mEditor.putString(FB_ACCESS_TOKEN, v);
		mEditor.commit();
	}
}
