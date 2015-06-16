package com.speakgeo.skopebeta.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

public class CircleViewGroup extends RelativeLayout {
    private Path clipPath;

    public CircleViewGroup(Context context) {
        super(context);
    }

    public CircleViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CircleViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(clipPath == null) {
            clipPath = new Path();
            int width = getWidth();
            int height = getHeight();
            int size = (width > height) ? height : width;
            size /= 2;
            clipPath.addCircle(size, size, size, Path.Direction.CW);
        }
        canvas.clipPath(clipPath);
        super.dispatchDraw(canvas);
    }
}
