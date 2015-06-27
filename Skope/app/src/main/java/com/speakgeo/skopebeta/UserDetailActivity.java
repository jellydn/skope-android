package com.speakgeo.skopebeta;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.speakgeo.skopebeta.adapters.PostsAdapter;
import com.speakgeo.skopebeta.custom.CustomActivity;
import com.speakgeo.skopebeta.custom.ExpandableHeightListView;
import com.speakgeo.skopebeta.interfaces.ICommentable;
import com.speakgeo.skopebeta.utils.UserProfileSingleton;
import com.speakgeo.skopebeta.webservices.UserWSObject;
import com.speakgeo.skopebeta.webservices.objects.SearchPostByUserResponse;
import com.speakgeo.skopebeta.webservices.objects.User;

public class UserDetailActivity extends CustomActivity implements View.OnClickListener, AbsListView.OnScrollListener, ICommentable {
    private ExpandableHeightListView lstPosts;
    private TextView tvUsername;
    private View vgrCompose;
    private EditText edtComposeContent;
    private Button btnPost, btnSendMessage;

    private PostsAdapter mPostsAdapter;

    private User mUser;
    private int mTotalPost;
    private int mCurrentPage;

    private int currentFirstVisibleItem;
    private int currentVisibleItemCount;
    private int currentScrollState;
    private boolean isLoading;

    private GetPostTask mGetPostTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        initData();
        initControls();
        initListeners();

        loadPostData();
    }

    @Override
    public void initData() {
        mUser = (User)this.getIntent().getSerializableExtra("USER");

        mPostsAdapter = new PostsAdapter(this,this);
    }

    @Override
    public void initControls() {
        lstPosts = (ExpandableHeightListView) this.findViewById(R.id.lst_posts);
        tvUsername = (TextView) this.findViewById(R.id.tv_user_name);
        vgrCompose = this.findViewById(R.id.vgr_compose);
        btnPost = (Button) this.findViewById(R.id.btn_post);
        btnSendMessage = (Button) this.findViewById(R.id.btn_send_message);
        edtComposeContent = (EditText) this.findViewById(R.id.edt_compose_content);

        mPostsAdapter.setParent(lstPosts);
        lstPosts.setAdapter(mPostsAdapter);
        lstPosts.setExpanded(true);

        tvUsername.setText(mUser.getName());
    }

    @Override
    public void initListeners() {
        btnPost.setOnClickListener(this);
        vgrCompose.setOnClickListener(this);
        btnSendMessage.setOnClickListener(this);

        this.lstPosts.setOnScrollListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_post) {
            //TODO not done yet
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
    public void showAddCommentBox(int position) {
        vgrCompose.setVisibility(View.VISIBLE);
        edtComposeContent.requestFocus();
    }

    public void loadPostData() {
        mTotalPost = 0;
        mCurrentPage = 0;

        if(mGetPostTask != null) mGetPostTask.cancel(true);
        mGetPostTask = new GetPostTask();
        mGetPostTask.execute();
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.currentScrollState = scrollState;
        this.isScrollCompleted();
    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {

            if(!isLoading && mCurrentPage <= Math.ceil(mTotalPost/ UserProfileSingleton.NUM_OF_POST_PER_PAGE)) {
                isLoading = true;
                if(mGetPostTask != null) mGetPostTask.cancel(true);
                mGetPostTask = new GetPostTask();
                mGetPostTask.execute();
            }
        }
    }

    /*--------------- Tasks ----------------*/
    private class GetPostTask extends AsyncTask<Void, Void, SearchPostByUserResponse> {

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        };

        @Override
        protected SearchPostByUserResponse doInBackground(Void... params) {
            return UserWSObject.searchPostByUser(getApplicationContext(), mUser.getId(), mCurrentPage++, UserProfileSingleton.NUM_OF_POST_PER_PAGE);
        }

        @Override
        protected void onPostExecute(SearchPostByUserResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                mPostsAdapter.addData(result.getData().getItems());

                mTotalPost = result.getData().getTotal();
            } else {
                Toast.makeText(getApplicationContext(), result.getData().getMessage(), Toast.LENGTH_LONG).show();
            }

            isLoading = false;
            hideLoadingBar();
        }
    }
}