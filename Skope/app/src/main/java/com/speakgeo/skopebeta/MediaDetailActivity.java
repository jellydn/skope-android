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
import android.support.v4.view.ViewPager;
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
import com.speakgeo.skopebeta.adapters.MediaDetailAdapter;
import com.speakgeo.skopebeta.custom.CustomActivity;
import com.speakgeo.skopebeta.utils.UserProfileSingleton;
import com.speakgeo.skopebeta.utils.imageloader.ImageLoaderSingleton;
import com.speakgeo.skopebeta.webservices.UserWSObject;
import com.speakgeo.skopebeta.webservices.objects.LoginResponse;
import com.speakgeo.skopebeta.webservices.objects.Media;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;

public class MediaDetailActivity extends CustomActivity {
    private ViewPager vpgMain;

    private MediaDetailAdapter mMediaDetailAdapter;

    private int mStartPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);

        initData();
        initControls();
        initListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //ImageLoaderSingleton.getInstance(getApplicationContext()).clearCache();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //ImageLoaderSingleton.getInstance(getApplicationContext()).clearCache();
    }

    @Override
    public void initData() {
        ArrayList<Media> medias = (ArrayList<Media>)this.getIntent().getSerializableExtra("MEDIA");
        mStartPos = this.getIntent().getIntExtra("POS",0);

        mMediaDetailAdapter = new MediaDetailAdapter(getApplicationContext(), medias);
    }

    @Override
    public void initControls() {
        super.initControls();

        vpgMain = (ViewPager) findViewById(R.id.vpg_main);
        vpgMain.setAdapter(mMediaDetailAdapter);

        vpgMain.setCurrentItem(mStartPos);
    }

    @Override
    public void initListeners() {

    }
}
