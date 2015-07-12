package com.speakgeo.skopebeta;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.speakgeo.skopebeta.adapters.MediaSliderAdapter;
import com.speakgeo.skopebeta.custom.CustomActivity;
import com.speakgeo.skopebeta.webservices.objects.Media;

import java.util.ArrayList;

public class MediaDetailActivity extends CustomActivity {

    private MediaSliderAdapter mMediaSliderAdapter;

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
        ArrayList<Media> medias = (ArrayList<Media>) this.getIntent().getSerializableExtra("MEDIA");
        mStartPos = this.getIntent().getIntExtra("POS", 0);

        mMediaSliderAdapter = new MediaSliderAdapter(getFragmentManager(), medias);
    }

    @Override
    public void initControls() {
        super.initControls();

        ViewPager vpgMain = (ViewPager) findViewById(R.id.vpg_main);
        vpgMain.setAdapter(mMediaSliderAdapter);

        vpgMain.setCurrentItem(mStartPos);
    }

    @Override
    public void initListeners() {

    }
}
