package dev.skope.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by TienLQ on 3/14/15.
 */
public class Utils {

    /* Calculation utilities */
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static int getHeightScreen(Context context) {
        Point size = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            windowManager.getDefaultDisplay().getSize(size);
            return size.y;
        } else {
            Display d = windowManager.getDefaultDisplay();
            return d.getHeight();
        }
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static int getWidthScreen(Context context) {
        Point size = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            windowManager.getDefaultDisplay().getSize(size);
            return size.x;
        } else {
            Display d = windowManager.getDefaultDisplay();
            return d.getWidth();
        }
    }

    public static boolean checkIsHeightVersion(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            return true;
        } else {
            return false;
        }
    }

}
