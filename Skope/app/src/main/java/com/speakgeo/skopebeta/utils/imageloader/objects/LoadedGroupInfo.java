/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

package com.speakgeo.skopebeta.utils.imageloader.objects;

import android.view.View;

import com.speakgeo.skopebeta.utils.imageloader.listeners.OnCompletedDownloadListener;
import com.speakgeo.skopebeta.utils.imageloader.listeners.OnProgressChangeListener;

public class LoadedGroupInfo {
	private final OnCompletedDownloadListener mCompletedDownloadListener;
	private final OnProgressChangeListener mProgressChangeListener;

	private final View[] mViews;
	private final String mUrl;
	private final String mCacheId;
	private final Option mOption;

	public LoadedGroupInfo(View[] views, String url, String cacheId, OnCompletedDownloadListener completedDownloadListener, OnProgressChangeListener progressChangeListener, Option option) {
		mCompletedDownloadListener = completedDownloadListener;
		mProgressChangeListener = progressChangeListener;
		mViews = views;
		mUrl = url;
		mCacheId = cacheId;
		mOption = option;
	}

	public OnCompletedDownloadListener getCompletedDownloadListener() {
		return mCompletedDownloadListener;
	}

	public OnProgressChangeListener getProgressChangeListener() {
		return mProgressChangeListener;
	}

	public View[] getViews() {
		return mViews;
	}

	public String getUrl() {
		return mUrl;
	}

	public String getCacheId() {
		return mCacheId;
	}

	public Option getOption() {
		return mOption;
	}

	public boolean isCache() {
		return mCacheId == null ? false : true;
	}
}
