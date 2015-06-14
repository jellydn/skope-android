package com.productsway.skope;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.productsway.skope.custom.CustomActivity;
import com.productsway.skope.webservices.UserWSObject;
import com.productsway.skope.webservices.objects.CommonResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends CustomActivity {
    Button btnLoginFacebook;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,  new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                //finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),getString(R.string.login_fb_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),getString(R.string.login_fb_fail), Toast.LENGTH_LONG).show();
            }
        });

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.productsway.skope",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.productsway.skope",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        initControls();
        initListeners();
    }


    @Override
    public void initData() {

    }

    @Override
    public void initControls() {
        super.initControls();

        btnLoginFacebook = (Button) this.findViewById(R.id.btn_login_facebook);
    }

    @Override
    public void initListeners() {
        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private class LoginFacebookTask extends AsyncTask<String, Void, CommonResponse> {

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        };

        @Override
        protected CommonResponse doInBackground(String... params) {
            return UserWSObject
                    .loginFB(getApplicationContext(),params[0]);
        }

        @Override
        protected void onPostExecute(CommonResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {

            } else {
                Toast.makeText(getApplicationContext(),
                        "pritn messsss", Toast.LENGTH_SHORT).show();
            }

            hideLoadingBar();
        }
    }
}
