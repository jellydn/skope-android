package com.speakgeo.skopebeta.utils;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;

public class ImageUtil {

	/**
	 * Round corner of bitmap.
	 *
	 * @param bitmap
	 *            Bitmap source.
	 * @return Bitmap dest.
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);
		Shader shader = new BitmapShader(bitmap, Shader.TileMode.MIRROR,
				Shader.TileMode.MIRROR);

		Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		paint.setAntiAlias(true);
		paint.setShader(shader);
		RectF rec = new RectF(0, 0, bitmap.getWidth(), bitmap.getWidth());
		c.drawRoundRect(rec, bitmap.getWidth(), bitmap.getWidth(), paint);
		return bmp;
	}

	public static synchronized Bitmap resizeBitmapFromUri(Context context,
			Uri uri) {
		try {
			ContentResolver cr = context.getContentResolver();
			InputStream is = null;

			is = cr.openInputStream(uri);
			int size = ImageUtil.calculateBitmapSize(is);
			is.close();

			is = cr.openInputStream(uri);

			Bitmap bm = ImageUtil.resizeUri(is, size);
			is.close();

			return bm;
		} catch (Exception e) {
		}
		return null;
	}

	private static int calculateBitmapSize(InputStream is) {
		int size = 1;
		try {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, opt);
			int w = opt.outWidth;

			while (w > 300) {
				w /= 300;
				size++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return size;
	}

	private static Bitmap resizeUri(InputStream is, int size) {
		Bitmap bm = null;
		try {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = false;
			opt.inSampleSize = size;
			bm = BitmapFactory.decodeStream(is, null, opt);

			return bm;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
