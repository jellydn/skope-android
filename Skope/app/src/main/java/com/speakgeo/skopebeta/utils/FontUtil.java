package com.speakgeo.skopebeta.utils;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontUtil {
	public static void setFont(Context context, TextView view, String link) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(),link);
		view.setTypeface(tf);
	}
	
	public static void setFontForAllTextViewsInHierarchy(Context context, View aViewGroup, String link) {
		Typeface aFont = Typeface.createFromAsset(context.getAssets(),link);
		
    	if (aViewGroup instanceof TextView) {
            ((TextView)aViewGroup).setTypeface(aFont);
            return;
        }
    	
	    for (int i=0; i<((ViewGroup) aViewGroup).getChildCount(); i++) {
	        View _v = ((ViewGroup) aViewGroup).getChildAt(i);
	        if (_v instanceof TextView) {
	            ((TextView)_v).setTypeface(aFont);
	        } else if (_v instanceof ViewGroup) {
	            setFontForAllTextViewsInHierarchy(context, (ViewGroup) _v, link);
	        }
	    }
	}
}
