package com.speakgeo.skopebeta.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.speakgeo.skopebeta.MediaDetailActivity;
import com.speakgeo.skopebeta.R;
import com.speakgeo.skopebeta.utils.ImageUtil;
import com.speakgeo.skopebeta.utils.imageloader.ImageLoaderSingleton;
import com.speakgeo.skopebeta.utils.imageloader.listeners.OnCompletedDownloadListener;
import com.speakgeo.skopebeta.utils.imageloader.objects.Option;
import com.speakgeo.skopebeta.webservices.objects.Media;

import java.util.ArrayList;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class MediaAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Media> mData;

    public MediaAdapter(Context context, ArrayList<Media> data) {
        super();
        this.context = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Media getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View viewToUse = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            viewToUse = inflater.inflate(R.layout.item_media, null);

            holder = new ViewHolder();
            holder.imgPhoto = (ImageView)viewToUse.findViewById(R.id.img_photo);
            holder.imgPlay = (ImageView)viewToUse.findViewById(R.id.img_play);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        if(mData.get(position).getType().contains("video"))
            holder.imgPlay.setVisibility(View.VISIBLE);
        else
            holder.imgPlay.setVisibility(View.GONE);

        ImageLoaderSingleton.getInstance(context).load(mData.get(position).getThumb(), "Media_"+mData.get(position).getId(), new OnCompletedDownloadListener() {
            @Override
            public void onComplete(View[] views, Bitmap bitmap) {
                ((ImageView) views[0]).setImageBitmap(bitmap);
            }
        }, null, new Option(200, 200), holder.imgPhoto);

        holder.imgPhoto.setTag(position);
        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();

                Intent i = new Intent(context, MediaDetailActivity.class);
                i.putExtra("POS",position);
                i.putExtra("MEDIA",mData);
                context.startActivity(i);
            }
        });

        return viewToUse;
    }

    /**
     * Holder for the list items.
     */
    private class ViewHolder{
        ImageView imgPhoto;
        ImageView imgPlay;
    }
}
