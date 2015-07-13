package com.speakgeo.skopebeta.utils;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class VideoUtil {

	public static synchronized byte[] getBytesFromUri(Context context,
			Uri uri) {
		try {
			ContentResolver cr = context.getContentResolver();
			InputStream is = null;

            if(uri.toString().contains("content"))
			    is = cr.openInputStream(uri);
            else
                is = new FileInputStream(uri.getPath());

			return VideoUtil.readBytes(is);
		} catch (Exception e) {
            e.printStackTrace();
		}
		return null;
	}

    private static byte[] readBytes(InputStream inputStream) throws
            IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
