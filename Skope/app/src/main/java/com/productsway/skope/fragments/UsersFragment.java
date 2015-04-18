package com.productsway.skope.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.productsway.skope.R;
import com.productsway.skope.adapters.UsersListAdapter;

import java.util.ArrayList;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class UsersFragment extends ListFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        UsersListAdapter mAdapter = new UsersListAdapter(getActivity(),mUsers);
        this.setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_users, null, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Toast.makeText(getActivity(),"sddf",Toast.LENGTH_SHORT).show();
    }
}
