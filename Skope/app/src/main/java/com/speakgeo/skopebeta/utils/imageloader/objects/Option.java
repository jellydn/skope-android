/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

package com.speakgeo.skopebeta.utils.imageloader.objects;

public class Option {
	private final int mWidth;
	private final int mHeight;
	private final int mScale;

	public Option(int w, int h) {
		mWidth = w;
		mHeight = h;
		mScale = -1;
	}

	public Option(int scale) {
		mWidth = -1;
		mHeight = -1;
		mScale = scale;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public int getScale() {
		return mScale;
	}

	public boolean isByScale() {
		return mScale == -1 ? false : true;
	}
}
