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
import org.apache.http.client.methods.HttpPost;
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

	public static String doPost(String url, List<NameValuePair> nameValuePairs)
			throws ClientProtocolException, IOException {
		String r = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);

        request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse response;
		try {
			response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inStream = entity.getContent();
				r = getResponseText(inStream);
				Log.i("SAN", ">>RESPONSE: " + r);
                inStream.close();
			}
		} catch (Exception e) {
			Log.e("SAN", "RestfulWSUtil/doPost: "+e.getMessage());
		}
		return r;
	}
}
