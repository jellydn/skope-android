package com.speakgeo.skopebeta.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.speakgeo.skopebeta.R;
import com.speakgeo.skopebeta.utils.ImageUtil;
import com.speakgeo.skopebeta.utils.imageloader.ImageLoaderSingleton;
import com.speakgeo.skopebeta.utils.imageloader.listeners.OnCompletedDownloadListener;
import com.speakgeo.skopebeta.utils.imageloader.objects.Option;
import com.speakgeo.skopebeta.webservices.objects.User;

import java.util.ArrayList;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class UsersListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<User> mUsers;

    public UsersListAdapter(Context context) {
        super();
        this.context = context;
        mUsers = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public User getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View viewToUse = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            viewToUse = inflater.inflate(R.layout.item_users, null);

            holder = new ViewHolder();
            holder.tvUserName = (TextView)viewToUse.findViewById(R.id.tv_username);
            holder.tvDistance = (TextView)viewToUse.findViewById(R.id.tv_distance);
            holder.imgAvatar = (ImageView)viewToUse.findViewById(R.id.img_avatar);
            holder.prgLoading = (ProgressBar)viewToUse.findViewById(R.id.prg_loading);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        holder.tvUserName.setText(mUsers.get(position).getName());
        holder.tvDistance.setText(mUsers.get(position).getLocation().getDistance() + " km away");

        ImageLoaderSingleton.getInstance(context).load(mUsers.get(position).getAvatar(),mUsers.get(position).getId(),new OnCompletedDownloadListener() {
            @Override
            public void onComplete(View[] views, Bitmap bitmap) {
                ((ImageView)views[0]).setImageBitmap(ImageUtil.getRoundedCornerBitmap(bitmap));
                views[1].setVisibility(View.GONE);
            }
        },null, new Option(150,150),holder.imgAvatar,holder.prgLoading);

        return viewToUse;
    }

    /**
     * Holder for the list items.
     */
    private class ViewHolder{
        TextView tvUserName;
        TextView tvDistance;
        ImageView imgAvatar;
        ProgressBar prgLoading;
    }

    public void setData(ArrayList<User> users) {
        this.mUsers = users;

        this.notifyDataSetChanged();
    }

    public void addData(ArrayList<User> users) {
        for(User user : users) {
            mUsers.add(user);
        }

        this.notifyDataSetChanged();
    }
}
