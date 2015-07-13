package com.speakgeo.skopebeta.adapters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.speakgeo.skopebeta.R;
import com.speakgeo.skopebeta.utils.ImageUtil;

import java.util.ArrayList;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class ComposePreviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Uri> mData;

    public ComposePreviewAdapter(Context context) {
        super();
        this.context = context;
        mData = new ArrayList();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Uri getItem(int position) {
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
            viewToUse = inflater.inflate(R.layout.item_compose_preview, null);

            holder = new ViewHolder();
            holder.imgPhoto= (ImageView)viewToUse.findViewById(R.id.img_photo);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        Bitmap bitmap = null;

        if(mData.get(position).getPath().contains("video")) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(context, mData.get(position));
            bitmap = mediaMetadataRetriever.getFrameAtTime(1000);
        }
        else {
            bitmap = ImageUtil.resizeBitmapFromUri(context, mData.get(position));
        }

        holder.imgPhoto.setImageBitmap(bitmap);

        return viewToUse;
    }

    public Uri pop() {
        Uri r = mData.get(0);
        mData.remove(0);
        notifyDataSetChanged();
        return r;
    }

    /**
     * Holder for the list items.
     */
    private class ViewHolder{
        ImageView imgPhoto;
    }

    public void addItem(Uri item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int pos) {
        mData.remove(pos);
        notifyDataSetChanged();
    }

    public void reset() {
        mData.clear();
        notifyDataSetChanged();
    }
}
