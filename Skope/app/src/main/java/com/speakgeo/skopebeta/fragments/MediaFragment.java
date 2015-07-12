package com.speakgeo.skopebeta.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.speakgeo.skopebeta.R;
import com.speakgeo.skopebeta.utils.imageloader.ImageLoaderSingleton;
import com.speakgeo.skopebeta.utils.imageloader.listeners.OnCompletedDownloadListener;
import com.speakgeo.skopebeta.utils.imageloader.objects.Option;
import com.speakgeo.skopebeta.webservices.objects.Media;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class MediaFragment extends Fragment implements View.OnClickListener {
    private ImageView imgPhoto;
    private ImageView imgPlay;
    private VideoView vdvVideo;

    private Media mMedia;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media, container, false);

        initControls(view);
        initListeners();

        return view;
    }

    public void initData() {
        mMedia = (Media)this.getArguments().getSerializable("MEDIA");
    }

    public void initControls(View container) {
        imgPhoto = (ImageView) container.findViewById(R.id.img_photo);
        imgPlay = (ImageView) container.findViewById(R.id.img_play);
        vdvVideo = (VideoView) container.findViewById(R.id.vdv_video);
        ProgressBar prgLoading = (ProgressBar) container.findViewById(R.id.prg_loading);

        if (mMedia.getType().contains("video")) {
            imgPlay.setVisibility(View.VISIBLE);
            vdvVideo.setVideoPath(mMedia.getSrc());
        } else {
            imgPlay.setVisibility(View.GONE);
        }

        ImageLoaderSingleton.getInstance(getActivity()).load(mMedia.getSrc(), "LARGE_MEDIA" + mMedia.getId(), new OnCompletedDownloadListener() {
            @Override
            public void onComplete(View[] views, Bitmap bitmap) {
                ((ImageView) views[0]).setImageBitmap(bitmap);
                views[1].setVisibility(View.GONE);
            }
        }, null, new Option(500, 500), imgPhoto, prgLoading);
    }

    public void initListeners() {
        imgPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        imgPlay.setVisibility(View.GONE);
        vdvVideo.setVisibility(View.VISIBLE);
        vdvVideo.start();
    }
}