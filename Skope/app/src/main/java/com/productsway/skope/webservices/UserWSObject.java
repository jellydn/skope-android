/**
* Skope
*
* Created by Vo Hoang San - hoangsan.762@gmai.com
* Copyright (c) 2015 San Vo. All right reserved.
*/

package com.productsway.skope.webservices;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.productsway.skope.utils.RestfulWSUtil;
import com.productsway.skope.utils.UserProfileSingleton;
import com.productsway.skope.webservices.objects.CommonRequest;
import com.productsway.skope.webservices.objects.LoginResponse;

/**
* Implement all services API which relate with Point.
*
*/
public class UserWSObject {
	public static LoginResponse loginFB(Context context, String fbAccessToken) {
        try {
            JSONObject request = null;
            JSONObject result = RestfulWSUtil.doPost(UserProfileSingleton.END_POINT
                    + "users/login", request);
        } catch (Exception e) {
            Log.e("SAN", "UserWSObject/loginFB: "+ e.getMessage());
        }

		return new LoginResponse();
	}
}
