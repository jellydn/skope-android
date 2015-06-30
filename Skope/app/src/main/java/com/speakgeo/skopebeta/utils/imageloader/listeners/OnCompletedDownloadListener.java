/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

package com.speakgeo.skopebeta.utils.imageloader.listeners;

import android.graphics.Bitmap;
import android.view.View;

public interface OnCompletedDownloadListener {
	public void onComplete(View[] views, Bitmap bitmap);
}
