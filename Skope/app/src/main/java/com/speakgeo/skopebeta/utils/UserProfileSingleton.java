/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

package com.speakgeo.skopebeta.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserProfileSingleton {
	//
	public static final String END_POINT = "http://108.61.202.156/";

	//
	public static String PREFERENCES = "SkopeConfig";

	private final SharedPreferences mSettings;
	private final SharedPreferences.Editor mEditor;

	private static UserProfileSingleton mInstance = null;

	private String mAccessToken;

	//
	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";

	public static synchronized UserProfileSingleton getConfig(Context context) {
		if (mInstance == null) {
			mInstance = new UserProfileSingleton(context);
		}
		return mInstance;
	}

	private UserProfileSingleton(Context context) {
		mSettings = context.getSharedPreferences(PREFERENCES, 0);
		mEditor = mSettings.edit();

		mAccessToken = mSettings.getString(ACCESS_TOKEN, "");
	}

	public String getAccessToken() {
		return mAccessToken;
	}

	public void setAccessToken(String v) {
        mAccessToken = v;
		mEditor.putString(ACCESS_TOKEN, v);
		mEditor.commit();
	}
}
