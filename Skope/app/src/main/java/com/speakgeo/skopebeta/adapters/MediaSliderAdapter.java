package com.speakgeo.skopebeta.adapters;

import android.app.Fragment;
import android.app.FragmentManager;

import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

import com.speakgeo.skopebeta.fragments.MediaFragment;
import com.speakgeo.skopebeta.webservices.objects.Media;

import java.util.ArrayList;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class MediaSliderAdapter  extends FragmentPagerAdapter {
    private ArrayList<Media> mResources;

    public MediaSliderAdapter(FragmentManager fm , ArrayList<Media> resources) {
        super(fm);

        this.mResources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MEDIA", mResources.get(position));
        MediaFragment mediaFragment = new MediaFragment();
        mediaFragment.setArguments(bundle);
        return mediaFragment;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }
}