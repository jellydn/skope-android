package com.productsway.skope.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.productsway.skope.R;
import com.productsway.skope.custom.ExpandableHeightListView;

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
    private ExpandableListView mParent;

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
            holder.btnCommentExpand = (TextView) viewToUse.findViewById(R.id.btn_comment_expand);
            holder.tvPostDistance = (TextView) viewToUse.findViewById(R.id.tv_post_distance);
            holder.tvPostContent = (TextView) viewToUse.findViewById(R.id.tv_post_content);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (PostViewHolder) viewToUse.getTag();
        }

        holder.btnCommentExpand.setTag(groupPosition);
        holder.btnCommentExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int groupPosition = (int) v.getTag();
                if (PostsAdapter.this.mParent.isGroupExpanded(groupPosition))
                    PostsAdapter.this.mParent.collapseGroup(groupPosition);
                else PostsAdapter.this.mParent.expandGroup(groupPosition, true);
            }
        });

        //TODO temp data
        holder.tvPostContent.setText(this.mPosts.get(groupPosition));
        holder.tvPostDistance.setText(String.format("Posted %d km away", (int)(100 * Math.random())));
        holder.btnCommentExpand.setText(String.format("Comments (%d)", this.mComments.get(this.mPosts.get(groupPosition))
                .size()));
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
            holder.tvCommentContent = (TextView)viewToUse.findViewById(R.id.tv_comment_content);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (CommentViewHolder) viewToUse.getTag();
        }

        //TODO temp data
        holder.tvCommentContent.setText(this.mComments.get(this.mPosts.get(groupPosition)).get(childPosition));

        return viewToUse;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setParent(ExpandableHeightListView lstPosts) {
        mParent = lstPosts;
    }

    /**
     * Holder for the list items.
     */
    private class PostViewHolder {
        TextView btnCommentExpand;
        TextView tvPostDistance;
        TextView tvPostContent;
        boolean isExpand;
    }

    private class CommentViewHolder {
        TextView tvCommentContent;
    }
}