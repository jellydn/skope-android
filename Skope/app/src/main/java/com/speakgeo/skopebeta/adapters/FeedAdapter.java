package com.speakgeo.skopebeta.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.speakgeo.skopebeta.R;
import com.speakgeo.skopebeta.fragments.FeedFragment;
import com.speakgeo.skopebeta.webservices.objects.CommentItem;
import com.speakgeo.skopebeta.webservices.objects.Post;

import java.util.ArrayList;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class FeedAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<Post> mPosts;
    private ExpandableListView mParent;
    private FeedFragment mFragment;

    public FeedAdapter(Context context, FeedFragment fragment) {
        this.mContext = context;
        this.mPosts = new ArrayList<>();
        this.mFragment = fragment;
    }

    @Override
    public int getGroupCount() {
        return this.mPosts.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mPosts.get(groupPosition).getComment().getItems().size();
    }

    @Override
    public Post getGroup(int groupPosition) {
        return this.mPosts.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mPosts.get(groupPosition).getComment().getItems().get(childPosition);
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
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
                mFragment.showAddCommentBox(groupPosition);
            }
        });

        holder.tvUserName.setText(this.mPosts.get(groupPosition).getUser().getName());
        holder.tvPostContent.setText(this.mPosts.get(groupPosition).getContent());
        holder.tvPostDistance.setText(String.format("Posted %d km away", (int) (Double.parseDouble(this.mPosts.get(groupPosition).getLocation().getDistance()) * Math.random())));
        holder.btnCommentExpand.setText(String.format("Comments (%d)", this.mPosts.get(groupPosition).getComment().getItems().size()));
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

        holder.tvCommentContent.setText(this.mPosts.get(groupPosition).getComment().getItems().get(childPosition).getContent());

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

    public void setData(ArrayList<Post> posts) {
        this.mPosts = posts;

        this.notifyDataSetChanged();
    }

    public void addData(ArrayList<Post> posts) {
        for(Post p : posts) {
            mPosts.add(p);
        }

        this.notifyDataSetChanged();
    }

    public void addComment(int mGroupPos, CommentItem comment) {
        mPosts.get(mGroupPos).getComment().getItems().add(comment);

        this.notifyDataSetChanged();
    }
}