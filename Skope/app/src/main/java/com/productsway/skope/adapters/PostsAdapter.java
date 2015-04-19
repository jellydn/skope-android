package com.productsway.skope.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.productsway.skope.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class PostsAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<String> mPosts;
    private HashMap<String, ArrayList<String>> mComments;

    public PostsAdapter(Context context, ArrayList<String> posts,
                        HashMap<String, ArrayList<String>> comments) {
        this.mContext = context;
        this.mPosts = posts;
        this.mComments = comments;
    }

    @Override
    public int getGroupCount() {
        return this.mPosts.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mComments.get(this.mPosts.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mPosts.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mComments.get(this.mPosts.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        PostViewHolder holder = null;
        View viewToUse = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            viewToUse = inflater.inflate(R.layout.item_user_detail_post, null);

            holder = new PostViewHolder();
            //holder.titleText = (TextView)viewToUse.findViewById(R.id.titleTextView);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (PostViewHolder) viewToUse.getTag();
        }

        //holder.titleText.setText(item.getItemTitle());
        return viewToUse;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CommentViewHolder holder = null;
        View viewToUse = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            viewToUse = inflater.inflate(R.layout.item_user_detail_post_comment, null);

            holder = new CommentViewHolder();
            //holder.titleText = (TextView)viewToUse.findViewById(R.id.titleTextView);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (CommentViewHolder) viewToUse.getTag();
        }

        //holder.titleText.setText(item.getItemTitle());
        return viewToUse;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * Holder for the list items.
     */
    private class PostViewHolder {
        //TextView titleText;
    }

    private class CommentViewHolder {
        //TextView titleText;
    }
}
