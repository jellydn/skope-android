package com.speakgeo.skopebeta;

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
import com.speakgeo.skopebeta.custom.CustomActivity;
import com.speakgeo.skopebeta.utils.UserProfileSingleton;
import com.speakgeo.skopebeta.webservices.UserWSObject;
import com.speakgeo.skopebeta.webservices.objects.LoginResponse;

import java.security.MessageDigest;
import java.util.Arrays;

public class LoginActivity extends CustomActivity {
    private Button btnLoginFacebook;

    private CallbackManager callbackManager;

    private LoginFacebookTask mLoginFacebookTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.speakgeo.skopebeta",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("SAN", "KeyHash: "+Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            Log.d("SAN", "KeyHash Error: "+e.getMessage());
        }

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,  new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(mLoginFacebookTask != null) mLoginFacebookTask.cancel(true);

                mLoginFacebookTask = new LoginFacebookTask();
                mLoginFacebookTask.execute(loginResult.getAccessToken().getToken());
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
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile","email"));
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /*--------------- Tasks ----------------*/
    private class LoginFacebookTask extends AsyncTask<String, Void, LoginResponse> {

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        };

        @Override
        protected LoginResponse doInBackground(String... params) {
            return UserWSObject
                    .loginFB(getApplicationContext(),params[0]);
        }

        @Override
        protected void onPostExecute(LoginResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                UserProfileSingleton.getConfig(getApplicationContext()).setAccessToken(result.getData().getAccessToken().getToken());
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), result.getData().getMessage(), Toast.LENGTH_LONG).show();
            }

            hideLoadingBar();
        }
    }
}
