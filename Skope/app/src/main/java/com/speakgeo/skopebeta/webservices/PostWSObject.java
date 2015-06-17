/**
* Skope
*
* Created by Vo Hoang San - hoangsan.762@gmai.com
* Copyright (c) 2015 San Vo. All right reserved.
*/

package com.speakgeo.skopebeta.webservices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.speakgeo.skopebeta.utils.RestfulWSUtil;
import com.speakgeo.skopebeta.utils.UserProfileSingleton;
import com.speakgeo.skopebeta.webservices.objects.SearchPostResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
* Implement all services API which relate with Point.
*
*/
public class PostWSObject {
    public static SearchPostResponse search(Context context, double longitude, double latitude, int distance, int page, int limit) {
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("access_token", UserProfileSingleton.getConfig(context).getAccessToken()));
            nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
            nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
            nameValuePairs.add(new BasicNameValuePair("distance", String.valueOf(distance)));
            nameValuePairs.add(new BasicNameValuePair("page", String.valueOf(page)));
            nameValuePairs.add(new BasicNameValuePair("limit", String.valueOf(limit)));

            String result = RestfulWSUtil.doGet(UserProfileSingleton.END_POINT
                    + "post/search", nameValuePairs);

            Gson gson = new GsonBuilder().create();
            return gson.fromJson(result, SearchPostResponse.class);
        } catch (Exception e) {
            Log.e("SAN", "PostWSObject/search: "+ e.getMessage());
            return null;
        }
    }
}
