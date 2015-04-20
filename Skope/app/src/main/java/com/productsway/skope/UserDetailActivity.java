package com.productsway.skope;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.productsway.skope.adapters.PostsAdapter;
import com.productsway.skope.adapters.UsersListAdapter;
import com.productsway.skope.custom.CircularPick;
import com.productsway.skope.custom.CircularPickChangeListener;
import com.productsway.skope.custom.CustomActivity;
import com.productsway.skope.custom.ExpandableHeightListView;
import com.productsway.skope.utils.MeasureUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDetailActivity extends CustomActivity {
    private ExpandableHeightListView lstPosts;
    private TextView tvNumberOfPosts;

    private PostsAdapter mPostsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        initData();
        initControls();
        initListeners();
    }

    @Override
    public void initData() {
        //TODO temp data
        ArrayList<String> mPosts = new ArrayList<String>();
        HashMap<String, ArrayList<String>> mComments = new HashMap<String, ArrayList<String>>();
        mPosts.add("Top 250");
        mPosts.add("Now Showing");
        mPosts.add("Coming Soon..");
        ArrayList<String> top250 = new ArrayList<String>();
        top250.add("The Shaw shank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");
        ArrayList<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");
        ArrayList<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");
        mComments.put(mPosts.get(0), top250);
        mComments.put(mPosts.get(1), nowShowing);
        mComments.put(mPosts.get(2), comingSoon);

        mPostsAdapter = new PostsAdapter(this,mPosts,mComments);
    }

    @Override
    public void initControls() {
        lstPosts = (ExpandableHeightListView) this.findViewById(R.id.lst_posts);
        tvNumberOfPosts = (TextView) this.findViewById(R.id.tv_number_of_posts);

        mPostsAdapter.setParent(lstPosts);
        lstPosts.setAdapter(mPostsAdapter);
        lstPosts.setExpanded(true);

        //TODO temp data
        tvNumberOfPosts.setText("User 1");
    }

    @Override
    public void initListeners() {

    }
}