package com.speakgeo.skopebeta.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

public class InterceptTouchEventChildDrawerLayout extends DrawerLayout {
    private int mInterceptTouchEventChildId;

    public void setInterceptTouchEventChildId(int id) {
        this.mInterceptTouchEventChildId = id;
    }

    public InterceptTouchEventChildDrawerLayout(Context context) {
        super(context);
    }

    public InterceptTouchEventChildDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mInterceptTouchEventChildId > 0) {
            View scroll = findViewById(mInterceptTouchEventChildId);
            if (scroll != null) {
                Rect rect = new Rect();
                scroll.getHitRect(rect);
                if (rect.contains((int) ev.getX(), (int) ev.getY())) {
                    return false;
                }
            }
        }
        return super.onTouchEvent(ev);
    }
}
