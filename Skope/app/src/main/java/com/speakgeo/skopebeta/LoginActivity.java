package com.speakgeo.skopebeta;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,  new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
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
                UserProfileSingleton.getConfig(getApplicationContext()).setFbAccessToken(result.getData().getAccessToken().getToken());
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), result.getMeta().getMessage(), Toast.LENGTH_LONG).show();
            }

            hideLoadingBar();
        }
    }
}
