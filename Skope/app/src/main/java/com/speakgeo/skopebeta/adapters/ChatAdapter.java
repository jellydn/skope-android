package com.speakgeo.skopebeta.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.speakgeo.skopebeta.R;

import java.util.ArrayList;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class ChatAdapter extends BaseAdapter {
    private Context context;
    private ArrayList mData;

    public ChatAdapter(Context context) {
        super();
        this.context = context;
        mData = new ArrayList();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
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

            //TODO temp data
            if (position % 3 == 0)
                viewToUse = inflater.inflate(R.layout.item_chat_left, null);
            else
                viewToUse = inflater.inflate(R.layout.item_chat_right, null);

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
    private class ViewHolder {
        //TextView titleText;
    }

    public void addItem(String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int pos) {
        mData.remove(pos);
        notifyDataSetChanged();
    }
}
