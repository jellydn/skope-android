package com.productsway.skope.utils;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MeasureUtil {
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float calculateZoomLevel(Context context, int km) {
        double equatorLength = 6378140; // in meters
        double widthInPixels = MeasureUtil.convertDpToPixel(220f, context); // control width
        double metersPerPixel = equatorLength / 256;
        float zoomLevel = 1;
        while ((metersPerPixel * widthInPixels) > (km*1000)) {
            metersPerPixel /= 2;
            zoomLevel++;
        }
        return zoomLevel;
    }
}
