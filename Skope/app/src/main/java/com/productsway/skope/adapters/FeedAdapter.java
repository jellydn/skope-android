package com.productsway.skope.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.productsway.skope.R;
import com.productsway.skope.custom.ExpandableHeightListView;
import com.productsway.skope.fragments.FeedFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class FeedAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<String> mPosts;
    private HashMap<String, ArrayList<String>> mComments;
    private ExpandableListView mParent;
    private FeedFragment mFragment;

    public FeedAdapter(Context context, ArrayList<String> posts,
                       HashMap<String, ArrayList<String>> comments, FeedFragment fragment) {
        this.mContext = context;
        this.mPosts = posts;
        this.mComments = comments;
        this.mFragment = fragment;
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
            viewToUse = inflater.inflate(R.layout.item_feed, null);

            holder = new PostViewHolder();
            holder.tvUserName = (TextView) viewToUse.findViewById(R.id.tv_user_name);
            holder.btnCommentExpand = (TextView) viewToUse.findViewById(R.id.btn_comment_expand);
            holder.tvPostDistance = (TextView) viewToUse.findViewById(R.id.tv_post_distance);
            holder.tvPostContent = (TextView) viewToUse.findViewById(R.id.tv_post_content);
            holder.btnComment = (Button) viewToUse.findViewById(R.id.btn_comment);
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
                if (FeedAdapter.this.mParent.isGroupExpanded(groupPosition))
                    FeedAdapter.this.mParent.collapseGroup(groupPosition);
                else FeedAdapter.this.mParent.expandGroup(groupPosition, true);
            }
        });
        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.showAddCommentBox();
            }
        });

        //TODO temp data
        holder.tvUserName.setText("User 1");
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
            viewToUse = inflater.inflate(R.layout.item_feed_comment, null);

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

    public void setParent(ExpandableListView lstPosts) {
        mParent = lstPosts;
    }

    /**
     * Holder for the list items.
     */
    private class PostViewHolder {
        TextView tvUserName;
        TextView btnCommentExpand;
        TextView tvPostDistance;
        TextView tvPostContent;
        Button btnComment;
    }

    private class CommentViewHolder {
        TextView tvCommentContent;
    }
}