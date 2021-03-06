package com.speakgeo.skopebeta.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.speakgeo.skopebeta.R;
import com.speakgeo.skopebeta.custom.ExpandableHeightListView;
import com.speakgeo.skopebeta.custom.HorizontalListView;
import com.speakgeo.skopebeta.interfaces.ICommentable;
import com.speakgeo.skopebeta.webservices.objects.CommentItem;
import com.speakgeo.skopebeta.webservices.objects.Post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class PostsAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<Post> mPosts;
    private ExpandableListView mParent;
    private ICommentable mCommentableActivity;

    private boolean mIsShowPostDate;

    public PostsAdapter(Context context, ICommentable commentableActivity) {
        this.mContext = context;
        this.mPosts = new ArrayList<>();
        this.mCommentableActivity = commentableActivity;

        mIsShowPostDate = false;
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
            viewToUse = inflater.inflate(R.layout.item_user_detail_post, null);

            holder = new PostViewHolder();
            holder.btnCommentExpand = (TextView) viewToUse.findViewById(R.id.btn_comment_expand);
            holder.tvPostDistance = (TextView) viewToUse.findViewById(R.id.tv_post_distance);
            holder.tvPostDate = (TextView) viewToUse.findViewById(R.id.tv_post_date);
            holder.tvPostContent = (TextView) viewToUse.findViewById(R.id.tv_post_content);
            holder.btnComment = (Button) viewToUse.findViewById(R.id.btn_comment);
            holder.tvLike = (TextView) viewToUse.findViewById(R.id.tv_like);
            holder.tvDislike = (TextView) viewToUse.findViewById(R.id.tv_dislike);
            holder.tvLikeClick = (TextView) viewToUse.findViewById(R.id.tv_like_click);
            holder.tvDislikeClick = (TextView) viewToUse.findViewById(R.id.tv_dislike_click);
            holder.pgbRate = (ProgressBar) viewToUse.findViewById(R.id.pgb_rate);
            holder.lstMeida = (HorizontalListView) viewToUse.findViewById(R.id.lst_media);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (PostViewHolder) viewToUse.getTag();
        }
        
        if(mIsShowPostDate) {
            holder.tvPostDate.setVisibility(View.VISIBLE);
        }
        else {
            holder.tvPostDate.setVisibility(View.GONE);
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
        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentableActivity.showAddCommentBox(groupPosition);
            }
        });
        holder.tvLikeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentableActivity.like(groupPosition);
            }
        });
        holder.tvDislikeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentableActivity.dislike(groupPosition);
            }
        });

        holder.tvPostContent.setText(this.mPosts.get(groupPosition).getContent());
        if(this.mPosts.get(groupPosition).getLocation().getDistance() != null)
            holder.tvPostDistance.setText(String.format("Posted %d km away", (int)(Double.parseDouble(this.mPosts.get(groupPosition).getLocation().getDistance()) * Math.random())));
        holder.btnCommentExpand.setText(String.format("Comments (%d)", this.mPosts.get(groupPosition).getComment().getItems().size()));
        holder.tvLike.setText(String.valueOf(this.mPosts.get(groupPosition).getLike().getTotal()));
        holder.tvDislike.setText(String.valueOf(this.mPosts.get(groupPosition).getDislike().getTotal()));

        if(this.mPosts.get(groupPosition).getLike().getTotal() != this.mPosts.get(groupPosition).getDislike().getTotal()) {
            holder.pgbRate.setMax(this.mPosts.get(groupPosition).getLike().getTotal() + this.mPosts.get(groupPosition).getDislike().getTotal());
            holder.pgbRate.setProgress(this.mPosts.get(groupPosition).getLike().getTotal());
        }
        else {
            holder.pgbRate.setMax(2);
            holder.pgbRate.setProgress(1);
        }

        DateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy");
        Date netDate = (new Date(this.mPosts.get(groupPosition).getCreated_at()));
        holder.tvPostDate.setText(sdf.format(netDate));

        if(mPosts.get(groupPosition).getMedia().size() != 0) {
            MediaAdapter adapter = new MediaAdapter(mContext, mPosts.get(groupPosition).getMedia());
            holder.lstMeida.setAdapter(adapter);
            holder.lstMeida.setVisibility(View.VISIBLE);
        }

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
            holder.tvCommentContent = (TextView) viewToUse.findViewById(R.id.tv_comment_content);
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

    public void setParent(ExpandableHeightListView lstPosts) {
        mParent = lstPosts;
    }

    public void isShowPostDate(boolean isShown) {
        mIsShowPostDate = isShown;
    }

    /**
     * Holder for the list items.
     */
    private class PostViewHolder {
        TextView btnCommentExpand;
        TextView tvPostDistance;
        TextView tvPostDate;
        TextView tvPostContent;
        Button btnComment;
        TextView tvLike;
        TextView tvDislike;
        TextView tvLikeClick;
        TextView tvDislikeClick;
        ProgressBar pgbRate;
        HorizontalListView lstMeida;
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

    public void updateVote(int mGroupPos, Post post) {
        mPosts.get(mGroupPos).setLike(post.getLike());
        mPosts.get(mGroupPos).setDislike(post.getDislike());

        this.notifyDataSetChanged();
    }
}