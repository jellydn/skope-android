package com.speakgeo.skopebeta.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.speakgeo.skopebeta.R;
import com.speakgeo.skopebeta.adapters.FeedAdapter;
import com.speakgeo.skopebeta.custom.CustomListFragment;
import com.speakgeo.skopebeta.interfaces.ICommentable;
import com.speakgeo.skopebeta.utils.UserProfileSingleton;
import com.speakgeo.skopebeta.webservices.PostWSObject;
import com.speakgeo.skopebeta.webservices.objects.CommentResponse;
import com.speakgeo.skopebeta.webservices.objects.CommonResponse;
import com.speakgeo.skopebeta.webservices.objects.Post;
import com.speakgeo.skopebeta.webservices.objects.SearchPostResponse;
import com.speakgeo.skopebeta.webservices.objects.VoteResponse;

import java.util.ArrayList;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class FeedFragment extends CustomListFragment implements View.OnClickListener, ICommentable, AbsListView.OnScrollListener{
    private ExpandableListView lstMain;
    private View vgrCompose;
    private EditText edtComposeContent;
    private Button btnPost;

    private FeedAdapter mFeedAdapter;

    private LatLng mLastLocation;
    private int mCurrentRadius;

    private int mCurrentSelectedPostPos;

    private int mTotalPost;
    private int mCurrentPage;

    private int currentFirstVisibleItem;
    private int currentVisibleItemCount;
    private int currentScrollState;
    private boolean isLoading;

    private SearchPostTask mSearchPostTask;
    private CommentTask mCommentTask;
    private VoteTask mVoteTask;

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
        mFeedAdapter = new FeedAdapter(getActivity(), this);
    }

    @Override
    public void initControls(View container) {
        super.initControls(container);

        lstMain = (ExpandableListView) container.findViewById(android.R.id.list);
        this.mainList = lstMain;

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

        this.lstMain.setOnScrollListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_post) {
            if(edtComposeContent.getText().toString().isEmpty()) return;

            if(mCommentTask != null) mCommentTask.cancel(true);
            mCommentTask = new CommentTask(mCurrentSelectedPostPos);
            mCommentTask.execute(edtComposeContent.getText().toString(),mFeedAdapter.getGroup(mCurrentSelectedPostPos).getId());
        } else if (v.getId() == R.id.vgr_compose) {
            if (vgrCompose.getVisibility() == View.VISIBLE) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

                vgrCompose.setVisibility(View.GONE);
            }
        }
    }

    public void setFeedData(ArrayList<Post> posts, int total, LatLng lastLocation, int currentRadius) {
        mFeedAdapter.setData(posts);

        mTotalPost = total;
        mLastLocation = lastLocation;
        mCurrentRadius = currentRadius;
        mCurrentPage = 1;
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
        mVoteTask.execute("true",mFeedAdapter.getGroup(mCurrentSelectedPostPos).getId());
    }

    @Override
    public void dislike(int position) {
        mCurrentSelectedPostPos = position;

        if(mVoteTask != null) mVoteTask.cancel(true);
        mVoteTask = new VoteTask(mCurrentSelectedPostPos);
        mVoteTask.execute("false",mFeedAdapter.getGroup(mCurrentSelectedPostPos).getId());
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
                if(mSearchPostTask != null) mSearchPostTask.cancel(true);
                mSearchPostTask = new SearchPostTask();
                mSearchPostTask.execute();
            }
        }
    }

    /*--------------- Tasks ----------------*/
    private class SearchPostTask extends AsyncTask<Void, Void, SearchPostResponse> {

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        };

        @Override
        protected SearchPostResponse doInBackground(Void... params) {
            return PostWSObject
                    .search(getActivity(), mLastLocation.longitude, mLastLocation.latitude, mCurrentRadius, mCurrentPage++, UserProfileSingleton.NUM_OF_POST_PER_PAGE);
        }

        @Override
        protected void onPostExecute(SearchPostResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                mFeedAdapter.addData(result.getData().getItems());
                mTotalPost = result.getData().getTotal();
            } else {
                Toast.makeText(getActivity(), result.getData().getMessage(), Toast.LENGTH_LONG).show();
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
                    .comment(getActivity(),params[0],params[1]);//content, post id
        }

        @Override
        protected void onPostExecute(CommentResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                vgrCompose.setVisibility(View.GONE);
                edtComposeContent.setText("");

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

                mFeedAdapter.addComment(mGroupPos, result.getData().getComment());
            } else {
                Toast.makeText(getActivity(), result.getMeta().getMessage(), Toast.LENGTH_LONG).show();
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
                    .vote(getActivity(), Boolean.parseBoolean(params[0]), params[1]);//type, post id
        }

        @Override
        protected void onPostExecute(VoteResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                mFeedAdapter.updateVote(mGroupPos, result.getData().getPost());

                Toast.makeText(getActivity(), result.getData().getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), result.getMeta().getMessage(), Toast.LENGTH_LONG).show();
            }

            isLoading = false;
            hideLoadingBar();
        }
    }
}
