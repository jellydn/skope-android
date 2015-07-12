/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

package com.speakgeo.skopebeta.utils.imageloader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.view.View;

import com.speakgeo.skopebeta.utils.UserProfileSingleton;
import com.speakgeo.skopebeta.utils.imageloader.listeners.OnCompletedDownloadListener;
import com.speakgeo.skopebeta.utils.imageloader.listeners.OnProgressChangeListener;
import com.speakgeo.skopebeta.utils.imageloader.objects.LoadedGroupInfo;
import com.speakgeo.skopebeta.utils.imageloader.objects.Option;

public class ImageLoaderSingleton {
	private static ImageLoaderSingleton mInstance;
	private final Context mContext;
	private ExecutorService mThreadPool;
	private final LruCache<String, Bitmap> mMemoryCache;

	private ImageLoaderSingleton(Context context) {
		mThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		mMemoryCache = new LruCache<String, Bitmap>((int)Runtime.getRuntime().maxMemory() / 2);
		mContext = context;
	}

	public static ImageLoaderSingleton getInstance(Context context) {
		if (mInstance == null) mInstance = new ImageLoaderSingleton(context);

		return mInstance;
	}

	public void load(String url, String cacheId, OnCompletedDownloadListener completedDownloadListener, OnProgressChangeListener progressChangeListener, Option option, View... views) {
		Bitmap bm = mMemoryCache.get(cacheId);

		if (bm != null) {
			if (completedDownloadListener != null) completedDownloadListener.onComplete(views, bm);
		}
		else {
			LoadedGroupInfo weakLoadedGroupInfo = new LoadedGroupInfo(views, url, cacheId, completedDownloadListener, progressChangeListener, option);

			this.executeTask(weakLoadedGroupInfo);
		}
	}

	public void stopAll() {
		mThreadPool.shutdown();
		mThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	public void clearMemoryCache() {
		mMemoryCache.evictAll();
	}

	public void clearFileCache() {
		for (String file : mContext.fileList()) {
			mContext.deleteFile(file);
		}
	}

	public void clearCache() {
		this.clearMemoryCache();
		this.clearFileCache();
	}

    public void clearCacheById(String cacheId) {
        this.mMemoryCache.remove(cacheId);
        for (String file : mContext.fileList()) {
            if(file.equals(cacheId)) {
                mContext.deleteFile(file);
                break;
            }
        }
    }

	public void resetAll() {
		this.stopAll();
		this.clearCache();
	}

	private void executeTask(LoadedGroupInfo loadedGroupInfo) {
		mThreadPool.execute(new ProcessTask(loadedGroupInfo));
	}

	private void onFinishLoadFileInThread(LoadedGroupInfo loadGroupInfo, Bitmap bm) {
		if (loadGroupInfo != null && loadGroupInfo.getCompletedDownloadListener() != null) {
			loadGroupInfo.getCompletedDownloadListener().onComplete(loadGroupInfo.getViews(), bm);
		}
	}

	private void onProgressChangeInThread(LoadedGroupInfo loadGroupInfo, int progress) {
		if (loadGroupInfo == null || loadGroupInfo.getProgressChangeListener() == null) return;

		loadGroupInfo.getProgressChangeListener().onProgressChange(loadGroupInfo.getViews(), progress);
	}

	private class ProcessTask implements Runnable, OnProgressChangeListener {
		private final LoadedGroupInfo mLoadedGroupInfo;
		private Bitmap mResult;
		private int mProgress;
		private final Handler threadHandler = new Handler();

		private final Runnable threadCallback = new Runnable() {
			@Override
			public void run() {
				onFinishLoadFileInThread(mLoadedGroupInfo, mResult);
			}
		};

		private final Runnable threadProgressCallback = new Runnable() {
			@Override
			public void run() {
				onProgressChangeInThread(mLoadedGroupInfo, mProgress);
			}
		};

		public ProcessTask(LoadedGroupInfo loadedGroupInfo) {
			mLoadedGroupInfo = loadedGroupInfo;
		}

		@Override
		public void run() {

			if (mLoadedGroupInfo != null) {
				if (mContext.getFileStreamPath(mLoadedGroupInfo.getCacheId()).exists()) {
					mResult = resizeImage(mLoadedGroupInfo.getCacheId(), mLoadedGroupInfo.getOption());
				}
				else {
					mResult = downloadImage(mLoadedGroupInfo.getCacheId(), mLoadedGroupInfo.getUrl(), mLoadedGroupInfo.getOption(), this);
				}

				if (mLoadedGroupInfo.getCompletedDownloadListener() != null) threadHandler.post(threadCallback);

				if (mResult == null) return;

				if (mLoadedGroupInfo.isCache()) {
					mMemoryCache.put(mLoadedGroupInfo.getCacheId(), mResult);
				}
				else {
					mContext.deleteFile(mLoadedGroupInfo.getCacheId());
				}
			}
		}

		@Override
		public void onProgressChange(View[] views, int progress) {
			mProgress = progress;
			threadHandler.post(threadProgressCallback);
		}

	}

	private Bitmap resizeImage(String cacheId, Option option) {
		try {
			int scale = option.getScale();
			if (!option.isByScale()) {
				FileInputStream is = mContext.openFileInput(cacheId);
				scale = calculateBitmapSize(is, option.getWidth(), option.getHeight());
				is.close();
			}

			FileInputStream is = mContext.openFileInput(cacheId);

			return scaleBitmap(is, scale);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private synchronized Bitmap scaleBitmap(InputStream is, int size) {
		try {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = false;
			opt.inSampleSize = size;
			return BitmapFactory.decodeStream(is, null, opt);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private int calculateBitmapSize(InputStream is, int rw, int rh) {
		int scale = 1;
		try {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, opt);
			int w = opt.outWidth;
			int h = opt.outHeight;

			if (w < h) {
				scale = Math.round((float)h / rh + 0.5f);
			}
			else {
				scale = Math.round((float)w / rw + 0.5f);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return scale;
	}

	private Bitmap downloadImage(String cacheId, String url, Option option, OnProgressChangeListener listener) {
		InputStream inStream = null;
		HttpURLConnection conn = null;

		try {
			conn = (HttpURLConnection)new URL(url).openConnection();
			conn.setReadTimeout(UserProfileSingleton.TIME_OUT);
			conn.setDoInput(true);

			inStream = conn.getInputStream();

			int total = conn.getContentLength();
			int count = 0;

			FileOutputStream fos = mContext.openFileOutput(cacheId, Context.MODE_PRIVATE);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
				fos.flush();

				count += len;

				if (listener != null) {
					listener.onProgressChange(null, (int)((count / (float)total) * 100));
				}
			}
			fos.close();

			inStream.close();
			conn.disconnect();
			fos = null;
			inStream = null;
			conn = null;

			return resizeImage(cacheId, option);
		}
		catch (Exception e) {
			e.printStackTrace();

			inStream = null;
			conn = null;

			return null;
		}
	}
}
