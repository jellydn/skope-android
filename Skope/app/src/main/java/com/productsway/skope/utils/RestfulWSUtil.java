/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

package com.productsway.skope.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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

	public static String getSpecialChars(String input) {
		String output = "";
		output = input.replace("&amp;", "&");
		output = input.replace("&lt;", "<");
		output = input.replace("&gt;", ">");
		output = input.replace("&quot;", "\"");
		output = input.replace("&#39;", "'");
		output = input.replace("&apos;", "'");
		return output;

	}

	public static String processSpecialChars(String input) {
		String output = "";
		output = input.replace("&", "&amp;");
		output = input.replace("<", "&lt;");
		output = input.replace(">", "&gt;");
		output = input.replace("\"", "&quot;");
		output = input.replace("'", "&apos;");
		return output;

	}

	public static JSONObject doPost(String url, JSONObject c)
			throws ClientProtocolException, IOException {
		JSONObject r = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		StringEntity s = new StringEntity(c.toString(), "UTF-8");

		Log.i("SAN", ">>REQUEST: (" + url + ")" + c.toString());

		request.setEntity(s);
		request.addHeader("Content-Type", "application/json; charset=UTF-8");

		HttpResponse response;
		try {
			response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				r = new JSONObject(getResponseText(instream));
				Log.i("SAN", ">>RESPONSE: " + r.toString());
				instream.close();
			}
		} catch (Exception e) {
			Log.e("SAN", "RestfulWSUtil/doPost: "+e.getMessage());
		}
		return r;
	}
}
