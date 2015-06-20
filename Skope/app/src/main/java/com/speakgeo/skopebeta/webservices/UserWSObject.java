/**
* Skope
*
* Created by Vo Hoang San - hoangsan.762@gmai.com
* Copyright (c) 2015 San Vo. All right reserved.
*/

package com.speakgeo.skopebeta.webservices;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.speakgeo.skopebeta.utils.RestfulWSUtil;
import com.speakgeo.skopebeta.utils.UserProfileSingleton;
import com.speakgeo.skopebeta.webservices.objects.LoginResponse;
import com.speakgeo.skopebeta.webservices.objects.SearchUserResponse;

import java.util.ArrayList;
import java.util.List;

/**
* Implement all services API which relate with Point.
*
*/
public class UserWSObject {
	public static LoginResponse loginFB(Context context, String fbAccessToken) {
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("fb_token", fbAccessToken));

            String result = RestfulWSUtil.doPost(UserProfileSingleton.END_POINT
                    + "user/auth", nameValuePairs, null);

            Gson gson = new GsonBuilder().create();
            return gson.fromJson(result, LoginResponse.class);
        } catch (Exception e) {
            Log.e("SAN", "UserWSObject/loginFB: "+ e.getMessage());
            return null;
        }
	}

    public static SearchUserResponse search(Context context, double longitude, double latitude, int distance, int page, int limit) {
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("access_token", UserProfileSingleton.getConfig(context).getAccessToken()));
            nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
            nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
            nameValuePairs.add(new BasicNameValuePair("distance", String.valueOf(distance)));
            nameValuePairs.add(new BasicNameValuePair("page", String.valueOf(page)));
            nameValuePairs.add(new BasicNameValuePair("limit", String.valueOf(limit)));

            String result = RestfulWSUtil.doGet(UserProfileSingleton.END_POINT
                    + "user/search", nameValuePairs);

            Gson gson = new GsonBuilder().create();
            return gson.fromJson(result, SearchUserResponse.class);
        } catch (Exception e) {
            Log.e("SAN", "UserWSObject/search: "+ e.getMessage());
            return null;
        }
    }
}
