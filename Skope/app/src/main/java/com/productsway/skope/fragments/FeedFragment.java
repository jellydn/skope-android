package com.productsway.skope.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.productsway.skope.R;
import com.productsway.skope.adapters.FeedAdapter;
import com.productsway.skope.custom.CustomFragment;
import com.productsway.skope.interfaces.ICommentable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class FeedFragment extends CustomFragment implements View.OnClickListener, ICommentable{
    private ExpandableListView lstMain;
    private View vgrCompose;
    private EditText edtComposeContent;
    private Button btnPost;

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

        mFeedAdapter = new FeedAdapter(getActivity(),mPosts,mComments, this);
    }

    @Override
    public void initControls(View container) {
        lstMain = (ExpandableListView) container.findViewById(R.id.list_main);
        vgrCompose = container.findViewById(R.id.vgr_compose);
        btnPost = (Button) container.findViewById(R.id.btn_post);
        edtComposeContent = (EditText) container.findViewById(R.id.edt_compose_content);

        mFeedAdapter.setParent(lstMain);
        lstMain.setAdapter(mFeedAdapter);
    }

    @Override
    public void initListeners() {
        btnPost.setOnClickListener(this);
        vgrCompose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_post) {
            vgrCompose.setVisibility(View.GONE);
            edtComposeContent.setText("");

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

        } else if (v.getId() == R.id.vgr_compose) {
            if (vgrCompose.getVisibility() == View.VISIBLE) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

                vgrCompose.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showAddCommentBox() {
        vgrCompose.setVisibility(View.VISIBLE);
        edtComposeContent.requestFocus();
    }
}