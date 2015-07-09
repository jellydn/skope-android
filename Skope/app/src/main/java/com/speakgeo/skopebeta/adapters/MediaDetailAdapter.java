package com.speakgeo.skopebeta.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.speakgeo.skopebeta.R;
import com.speakgeo.skopebeta.utils.ImageUtil;
import com.speakgeo.skopebeta.utils.imageloader.ImageLoaderSingleton;
import com.speakgeo.skopebeta.utils.imageloader.listeners.OnCompletedDownloadListener;
import com.speakgeo.skopebeta.utils.imageloader.objects.Option;
import com.speakgeo.skopebeta.webservices.objects.Media;

import java.util.ArrayList;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

public class MediaDetailAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;

    private ArrayList<Media> mResources;

    public MediaDetailAdapter(Context context, ArrayList<Media> resources) {
        mContext = context;
        mResources = resources;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return true;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_media_detail, container, false);

        ImageView imgPhoto = (ImageView) itemView.findViewById(R.id.img_photo);
        VideoView vdvVideo = (VideoView) itemView.findViewById(R.id.vdv_video);
        ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.prg_loading);
        ImageView imgPlay = (ImageView) itemView.findViewById(R.id.img_play);

        if(mResources.get(position).getType().contains("video")) {
            imgPlay.setVisibility(View.VISIBLE);

            vdvVideo.setVideoPath(mResources.get(position).getSrc());

            imgPlay.setTag(vdvVideo);
            imgPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoView vdvVideo = (VideoView) v.getTag();

                    vdvVideo.setVisibility(View.VISIBLE);
                    vdvVideo.start();
                }
            });
        }
        else {
            imgPlay.setVisibility(View.GONE);
            vdvVideo.setVisibility(View.GONE);
        }

        ImageLoaderSingleton.getInstance(mContext).load(mResources.get(position).getSrc(),"LARGE_MEDIA"+mResources.get(position).getId(),new OnCompletedDownloadListener() {
            @Override
            public void onComplete(View[] views, Bitmap bitmap) {
                ((ImageView)views[0]).setImageBitmap(bitmap);
                views[1].setVisibility(View.GONE);
            }
        },null, new Option(500,500),imgPhoto,progressBar);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ViewGroup) object);
    }
}