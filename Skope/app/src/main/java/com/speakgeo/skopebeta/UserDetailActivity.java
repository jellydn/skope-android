package com.speakgeo.skopebeta;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.speakgeo.skopebeta.adapters.PostsAdapter;
import com.speakgeo.skopebeta.custom.CustomActivity;
import com.speakgeo.skopebeta.custom.CustomScrollView;
import com.speakgeo.skopebeta.custom.ExpandableHeightListView;
import com.speakgeo.skopebeta.interfaces.ICommentable;
import com.speakgeo.skopebeta.utils.ImageUtil;
import com.speakgeo.skopebeta.utils.UserProfileSingleton;
import com.speakgeo.skopebeta.utils.imageloader.ImageLoaderSingleton;
import com.speakgeo.skopebeta.utils.imageloader.listeners.OnCompletedDownloadListener;
import com.speakgeo.skopebeta.utils.imageloader.objects.Option;
import com.speakgeo.skopebeta.webservices.PostWSObject;
import com.speakgeo.skopebeta.webservices.UserWSObject;
import com.speakgeo.skopebeta.webservices.objects.CommentResponse;
import com.speakgeo.skopebeta.webservices.objects.CommonResponse;
import com.speakgeo.skopebeta.webservices.objects.SearchPostByUserResponse;
import com.speakgeo.skopebeta.webservices.objects.User;
import com.speakgeo.skopebeta.webservices.objects.VoteResponse;

public class UserDetailActivity extends CustomActivity implements View.OnClickListener, CustomScrollView.OnScrollListener, ICommentable {
    private ExpandableHeightListView lstPosts;
    private TextView tvUsername;
    private View vgrCompose;
    private EditText edtComposeContent;
    private Button btnPost, btnSendMessage;
    private ImageView imgAvatar;
    private ProgressBar prgLoadingImage;
    private CustomScrollView scrMain;

    private PostsAdapter mPostsAdapter;

    private User mUser;
    private int mTotalPost;
    private int mCurrentPage;

    private int mCurrentSelectedPostPos;

    private boolean isLoading;

    private GetPostTask mGetPostTask;
    private CommentTask mCommentTask;
    private VoteTask mVoteTask;

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
        super.initControls();

        lstPosts = (ExpandableHeightListView) this.findViewById(R.id.lst_posts);
        tvUsername = (TextView) this.findViewById(R.id.tv_user_name);
        vgrCompose = this.findViewById(R.id.vgr_compose);
        btnPost = (Button) this.findViewById(R.id.btn_post);
        btnSendMessage = (Button) this.findViewById(R.id.btn_send_message);
        edtComposeContent = (EditText) this.findViewById(R.id.edt_compose_content);
        imgAvatar = (ImageView) this.findViewById(R.id.img_avatar);
        prgLoadingImage = (ProgressBar) this.findViewById(R.id.prg_loading_img);
        scrMain = (CustomScrollView) this.findViewById(R.id.scr_main);

        mPostsAdapter.setParent(lstPosts);
        lstPosts.setAdapter(mPostsAdapter);
        lstPosts.setExpanded(true);

        tvUsername.setText(mUser.getName());

        ImageLoaderSingleton.getInstance(this).load(mUser.getAvatar(), mUser.getId(), new OnCompletedDownloadListener() {
            @Override
            public void onComplete(View[] views, Bitmap bitmap) {
                ((ImageView) views[0]).setImageBitmap(ImageUtil.getRoundedCornerBitmap(bitmap));
                views[1].setVisibility(View.GONE);
            }
        }, null, new Option(200, 200), imgAvatar, prgLoadingImage);
    }

    @Override
    public void initListeners() {
        btnPost.setOnClickListener(this);
        vgrCompose.setOnClickListener(this);
        btnSendMessage.setOnClickListener(this);

        scrMain.setOnScrollListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_post) {
            if(edtComposeContent.getText().toString().isEmpty()) return;

            if(mCommentTask != null) mCommentTask.cancel(true);
            mCommentTask = new CommentTask(mCurrentSelectedPostPos);
            mCommentTask.execute(edtComposeContent.getText().toString(),mPostsAdapter.getGroup(mCurrentSelectedPostPos).getId());
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
        mCurrentSelectedPostPos = position;

        vgrCompose.setVisibility(View.VISIBLE);
        edtComposeContent.requestFocus();
    }

    @Override
    public void like(int position) {
        mCurrentSelectedPostPos = position;

        if(mVoteTask != null) mVoteTask.cancel(true);
        mVoteTask = new VoteTask(mCurrentSelectedPostPos);
        mVoteTask.execute("true",mPostsAdapter.getGroup(mCurrentSelectedPostPos).getId());
    }

    @Override
    public void dislike(int position) {
        mCurrentSelectedPostPos = position;

        if(mVoteTask != null) mVoteTask.cancel(true);
        mVoteTask = new VoteTask(mCurrentSelectedPostPos);
        mVoteTask.execute("false",mPostsAdapter.getGroup(mCurrentSelectedPostPos).getId());
    }

    public void loadPostData() {
        mTotalPost = 0;
        mCurrentPage = 0;

        if(mGetPostTask != null) mGetPostTask.cancel(true);
        mGetPostTask = new GetPostTask();
        mGetPostTask.execute();
    }

    public void onScrollToEnd() {
        if (!isLoading && mCurrentPage <= Math.ceil(mTotalPost / UserProfileSingleton.NUM_OF_POST_PER_PAGE)) {
            isLoading = true;
            if (mGetPostTask != null) mGetPostTask.cancel(true);
            mGetPostTask = new GetPostTask();
            mGetPostTask.execute();
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
            return UserWSObject.searchPostByUser(getApplicationContext(), mUser.getId(), ++mCurrentPage, UserProfileSingleton.NUM_OF_POST_PER_PAGE);
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

    private class CommentTask extends AsyncTask<String, Void, CommentResponse> {
        private int mGroupPos;

        public CommentTask(int groupPos) {
            mGroupPos = groupPos;
        }

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        };

        @Override
        protected CommentResponse doInBackground(String... params) {
            return PostWSObject
                    .comment(getApplicationContext(), params[0], params[1]);//content, post id
        }

        @Override
        protected void onPostExecute(CommentResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                vgrCompose.setVisibility(View.GONE);
                edtComposeContent.setText("");

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

                mPostsAdapter.addComment(mGroupPos, result.getData().getComment());
            } else {
                Toast.makeText(getApplicationContext(), result.getMeta().getMessage(), Toast.LENGTH_LONG).show();
            }

            isLoading = false;
            hideLoadingBar();
        }
    }

    private class VoteTask extends AsyncTask<String, Void, VoteResponse> {
        private int mGroupPos;

        public VoteTask(int groupPos) {
            mGroupPos = groupPos;
        }

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        };

        @Override
        protected VoteResponse doInBackground(String... params) {
            return PostWSObject
                    .vote(getApplicationContext(), Boolean.parseBoolean(params[0]), params[1]);//type, post id
        }

        @Override
        protected void onPostExecute(VoteResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                mPostsAdapter.updateVote(mGroupPos, result.getData().getPost());

                Toast.makeText(getApplicationContext(), result.getData().getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), result.getMeta().getMessage(), Toast.LENGTH_LONG).show();
            }

            isLoading = false;
            hideLoadingBar();
        }
    }
}