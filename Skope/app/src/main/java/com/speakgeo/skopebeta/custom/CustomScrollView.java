package com.speakgeo.skopebeta.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

public class CustomScrollView extends ScrollView {
    private OnScrollListener onScrollListener;

    public CustomScrollView(Context context)
    {
        super(context);
    }
    public CustomScrollView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        View view = (View)getChildAt(getChildCount()-1);
        int d = view.getBottom();
        d -= (getHeight()+getScrollY());
        if(d==0)
        {
            if(onScrollListener != null)
                onScrollListener.onScrollToEnd();
        }
        else
            super.onScrollChanged(l,t,oldl,oldt);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public interface OnScrollListener {
        public void onScrollToEnd();
    }
}
