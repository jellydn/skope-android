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
import com.speakgeo.skopebeta.webservices.objects.CommentResponse;
import com.speakgeo.skopebeta.webservices.objects.CommonResponse;
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

    public static CommonResponse post(Context context, String content, double longitude, double latitude) {
        try {
            List<NameValuePair> queries = new ArrayList<NameValuePair>(1);
            queries.add(new BasicNameValuePair("access_token", UserProfileSingleton.getConfig(context).getAccessToken()));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
            nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
            nameValuePairs.add(new BasicNameValuePair("content", content));

            String result = RestfulWSUtil.doPost(UserProfileSingleton.END_POINT
                    + "user/post", nameValuePairs, queries);

            Gson gson = new GsonBuilder().create();
            return gson.fromJson(result, CommonResponse.class);
        } catch (Exception e) {
            Log.e("SAN", "PostWSObject/post: "+ e.getMessage());
            return null;
        }
    }

    public static CommentResponse comment(Context context, String content, String postId) {
        try {
            List<NameValuePair> queries = new ArrayList<NameValuePair>(1);
            queries.add(new BasicNameValuePair("access_token", UserProfileSingleton.getConfig(context).getAccessToken()));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("content", content));

            String result = RestfulWSUtil.doPost(UserProfileSingleton.END_POINT
                    + "post/"+postId+"/comment", nameValuePairs, queries);

            Gson gson = new GsonBuilder().create();
            return gson.fromJson(result, CommentResponse.class);
        } catch (Exception e) {
            Log.e("SAN", "PostWSObject/comment: "+ e.getMessage());
            return null;
        }
    }

    public static CommonResponse vote(Context context, boolean isLike, String postId) {
        try {
            List<NameValuePair> queries = new ArrayList<NameValuePair>(1);
            queries.add(new BasicNameValuePair("access_token", UserProfileSingleton.getConfig(context).getAccessToken()));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("type", isLike?"like":"dislike"));

            String result = RestfulWSUtil.doPost(UserProfileSingleton.END_POINT
                    + "post/"+postId+"/vote", nameValuePairs, queries);

            Gson gson = new GsonBuilder().create();
            return gson.fromJson(result, CommonResponse.class);
        } catch (Exception e) {
            Log.e("SAN", "PostWSObject/vote: "+ e.getMessage());
            return null;
        }
    }
}
