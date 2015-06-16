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
                    + "user/auth", nameValuePairs);

            Gson gson = new GsonBuilder().create();
            return gson.fromJson(result, LoginResponse.class);
        } catch (Exception e) {
            Log.e("SAN", "UserWSObject/loginFB: "+ e.getMessage());
            return null;
        }
	}
}
