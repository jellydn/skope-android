package com.speakgeo.skopebeta.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.speakgeo.skopebeta.HomeActivity;
import com.speakgeo.skopebeta.R;
import com.speakgeo.skopebeta.UserDetailActivity;
import com.speakgeo.skopebeta.adapters.UsersListAdapter;
import com.speakgeo.skopebeta.interfaces.IFragmentInitialization;

import java.util.ArrayList;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class UsersFragment extends ListFragment implements IFragmentInitialization {
    private UsersListAdapter mAdapter;

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
        getActivity().startActivity(new Intent(getActivity().getApplicationContext(),UserDetailActivity.class));
    }

    @Override
    public void initData() {
        //TODO temp data
        ArrayList mUsers = new ArrayList();
        mUsers.add("Example 1");
        mUsers.add("Example 2");
        mUsers.add("Example 3");
        mUsers.add("Example 3");
        mUsers.add("Example 3");
        mUsers.add("Example 3");
        mUsers.add("Example 3");
        mUsers.add("Example 3");
        mUsers.add("Example 3");
        mUsers.add("Example 3");
        mUsers.add("Example 3");

        mAdapter = new UsersListAdapter(getActivity(),mUsers);
    }

    @Override
    public void initControls(View container) {
        this.setListAdapter(mAdapter);
    }

    @Override
    public void initListeners() {

    }
}
