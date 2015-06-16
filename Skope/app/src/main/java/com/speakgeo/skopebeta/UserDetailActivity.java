package com.speakgeo.skopebeta;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.speakgeo.skopebeta.adapters.PostsAdapter;
import com.speakgeo.skopebeta.custom.CustomActivity;
import com.speakgeo.skopebeta.custom.ExpandableHeightListView;
import com.speakgeo.skopebeta.interfaces.ICommentable;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDetailActivity extends CustomActivity implements View.OnClickListener, ICommentable {
    private ExpandableHeightListView lstPosts;
    private TextView tvNumberOfPosts;
    private View vgrCompose;
    private EditText edtComposeContent;
    private Button btnPost, btnSendMessage;

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

        mPostsAdapter = new PostsAdapter(this, mPosts, mComments, this);
    }

    @Override
    public void initControls() {
        lstPosts = (ExpandableHeightListView) this.findViewById(R.id.lst_posts);
        tvNumberOfPosts = (TextView) this.findViewById(R.id.tv_number_of_posts);
        vgrCompose = this.findViewById(R.id.vgr_compose);
        btnPost = (Button) this.findViewById(R.id.btn_post);
        btnSendMessage = (Button) this.findViewById(R.id.btn_send_message);
        edtComposeContent = (EditText) this.findViewById(R.id.edt_compose_content);

        mPostsAdapter.setParent(lstPosts);
        lstPosts.setAdapter(mPostsAdapter);
        lstPosts.setExpanded(true);

        //TODO temp data
        tvNumberOfPosts.setText("User 1");
    }

    @Override
    public void initListeners() {
        btnPost.setOnClickListener(this);
        vgrCompose.setOnClickListener(this);
        btnSendMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_post) {
            vgrCompose.setVisibility(View.GONE);
            edtComposeContent.setText("");

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

        } else if (v.getId() == R.id.vgr_compose) {
            if (vgrCompose.getVisibility() == View.VISIBLE) {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

                vgrCompose.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.btn_send_message) {
            startActivity(new Intent(this, MessageDetailActivity.class));
        }
    }

    @Override
    public void showAddCommentBox() {
        vgrCompose.setVisibility(View.VISIBLE);
        edtComposeContent.requestFocus();
    }
}