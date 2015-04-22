package com.productsway.skope.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.productsway.skope.R;

import java.util.ArrayList;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class ComposePreviewAdapter extends BaseAdapter {
    private Context context;
    private String[] mUsers;

    public ComposePreviewAdapter(Context context, String[] users) {
        super();
        this.context = context;
        mUsers = users;
    }

    @Override
    public int getCount() {
        return mUsers.length;
    }

    @Override
    public Object getItem(int position) {
        return mUsers[position];
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
            viewToUse = inflater.inflate(R.layout.item_compose_preview, null);

            holder = new ViewHolder();
            //holder.titleText = (TextView)viewToUse.findViewById(R.id.titleTextView);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        //holder.titleText.setText(item.getItemTitle());
        return viewToUse;
    }

    /**
     * Holder for the list items.
     */
    private class ViewHolder{
        //TextView titleText;
    }
}
