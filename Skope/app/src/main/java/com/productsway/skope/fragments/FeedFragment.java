package com.productsway.skope.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.productsway.skope.R;
import com.productsway.skope.adapters.FeedAdapter;
import com.productsway.skope.custom.CustomFragment;
import com.productsway.skope.custom.ExpandableHeightListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class FeedFragment extends CustomFragment {
    private ExpandableListView lstMain;

    private FeedAdapter mFeedAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        initControls(view);
        initListeners();

        return view;
    }

    @Override
    public void initData() {
        //TODO temp data
        ArrayList<String> mPosts = new ArrayList<String>();
        HashMap<String, ArrayList<String>> mComments = new HashMap<String, ArrayList<String>>();
        mPosts.add("Top 250");
        mPosts.add("Now Showing");
        mPosts.add("Coming Soon.. Coming Soon.. Coming Soon.. Coming Soon.. Coming Soon.. Coming Soon.. Coming Soon.. Coming Soon..");
        ArrayList<String> top250 = new ArrayList<String>();
        top250.add("The Shaw shank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly. The Good, the Bad and the Ugly. The Good, the Bad and the Ugly");
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

        mFeedAdapter = new FeedAdapter(getActivity(),mPosts,mComments);
    }

    @Override
    public void initControls(View container) {
        lstMain = (ExpandableListView) container.findViewById(R.id.list_main);

        mFeedAdapter.setParent(lstMain);
        lstMain.setAdapter(mFeedAdapter);
    }

    @Override
    public void initListeners() {

    }
}
