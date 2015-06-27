package com.speakgeo.skopebeta.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.speakgeo.skopebeta.HomeActivity;
import com.speakgeo.skopebeta.R;
import com.speakgeo.skopebeta.UserDetailActivity;
import com.speakgeo.skopebeta.adapters.UsersListAdapter;
import com.speakgeo.skopebeta.custom.CustomListFragment;
import com.speakgeo.skopebeta.interfaces.IFragmentInitialization;
import com.speakgeo.skopebeta.utils.UserProfileSingleton;
import com.speakgeo.skopebeta.webservices.UserWSObject;
import com.speakgeo.skopebeta.webservices.objects.SearchUserResponse;
import com.speakgeo.skopebeta.webservices.objects.User;

import java.util.ArrayList;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class UsersFragment extends CustomListFragment implements IFragmentInitialization, AbsListView.OnScrollListener {
    private UsersListAdapter mAdapter;

    private LatLng mLastLocation;
    private int mCurrentRadius;
    private int mTotalUser;
    private int mCurrentPage;

    private int currentFirstVisibleItem;
    private int currentVisibleItemCount;
    private int currentScrollState;
    private boolean isLoading;

    private SearchUserTask mSearchUserTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        initControls(view);
        initListeners();

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ((HomeActivity)getActivity()).closeAllDrawer();
        Intent intent = new Intent(getActivity().getApplicationContext(),UserDetailActivity.class);
        intent.putExtra("USER",((UsersListAdapter)l.getAdapter()).getItem(position));
        getActivity().startActivity(intent);
    }

    @Override
    public void initData() {
        mAdapter = new UsersListAdapter(getActivity());

        isLoading = false;
    }

    @Override
    public void initControls(View container) {
        super.initControls(container);

        this.setListAdapter(mAdapter);
    }

    @Override
    public void initListeners() {
        this.mainList.setOnScrollListener(this);
    }

    public void setUserData(ArrayList<User> users, int total, LatLng lastLocation, int currentRadius) {
        mAdapter.setData(users);

        mTotalUser = total;
        mLastLocation = lastLocation;
        mCurrentRadius = currentRadius;
        mCurrentPage = 1;
    }

    /*--------------- Tasks ----------------*/
    private class SearchUserTask extends AsyncTask<Void, Void, SearchUserResponse> {

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        };

        @Override
        protected SearchUserResponse doInBackground(Void... params) {
            return UserWSObject
                    .search(getActivity(), mLastLocation.longitude, mLastLocation.latitude, mCurrentRadius, mCurrentPage++, UserProfileSingleton.NUM_OF_USER_PER_PAGE);
        }

        @Override
        protected void onPostExecute(SearchUserResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                mAdapter.addData(result.getData().getItems());

            } else {
                Toast.makeText(getActivity(), result.getData().getMessage(), Toast.LENGTH_LONG).show();
            }

            isLoading = false;
            hideLoadingBar();
        }
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

            if(!isLoading && mCurrentPage <= Math.ceil(mTotalUser/UserProfileSingleton.NUM_OF_USER_PER_PAGE)) {
                isLoading = true;
                if(mSearchUserTask != null) mSearchUserTask.cancel(true);
                mSearchUserTask = new SearchUserTask();
                mSearchUserTask.execute();
            }
        }
    }
}
