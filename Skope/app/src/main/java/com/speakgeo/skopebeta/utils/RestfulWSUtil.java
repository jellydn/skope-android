/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

package com.speakgeo.skopebeta.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

/**
 * Provide helper to call Restful Web service.
 * 
 */
public class RestfulWSUtil {
	private static String getResponseText(InputStream inStream) {
		return new Scanner(inStream).useDelimiter("\\A").next();
	}

	public static String doPost(String url, List<NameValuePair> nameValuePairs, List<NameValuePair> query)
			throws ClientProtocolException, IOException {
		String r = null;
		HttpClient httpclient = new DefaultHttpClient();
        String lastUrl = query==null ? url : url+"?"+URLEncodedUtils.format(query, "utf-8");
		HttpPost request = new HttpPost(lastUrl);
        Log.i("SAN", ">>REQUEST: " + lastUrl);
        request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse response;
		try {
			response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inStream = entity.getContent();
				r = getResponseText(inStream);
				Log.i("SAN", "<<RESPONSE: " + r);
                inStream.close();
			}
		} catch (Exception e) {
			Log.e("SAN", "RestfulWSUtil/doPost: "+e.getMessage());
		}
		return r;
	}

    public static String doGet(String url, List<NameValuePair> nameValuePairs)
            throws ClientProtocolException, IOException {
        String r = null;
        HttpClient httpclient = new DefaultHttpClient();
        String lastUrl = url+"?"+URLEncodedUtils.format(nameValuePairs, "utf-8");
        HttpGet request = new HttpGet(lastUrl);
        Log.i("SAN", ">>REQUEST: " + lastUrl);
        HttpResponse response;
        try {
            response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inStream = entity.getContent();
                r = getResponseText(inStream);
                Log.i("SAN", "<<RESPONSE: " + r);
                inStream.close();
            }
        } catch (Exception e) {
            Log.e("SAN", "RestfulWSUtil/doPost: "+e.getMessage());
        }
        return r;
    }
}
