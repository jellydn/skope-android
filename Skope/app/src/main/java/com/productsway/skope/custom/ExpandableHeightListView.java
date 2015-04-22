package com.productsway.skope.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class ExpandableHeightListView extends ExpandableListView
{
    boolean expanded = false;

    public ExpandableHeightListView(Context context)
    {
        super(context);
    }

    public ExpandableHeightListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ExpandableHeightListView(Context context, AttributeSet attrs,
            int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public boolean isExpanded()
    {
        return expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (isExpanded())
        {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * Set expand mode, if set as false, the Gridview works as a normal Gridview
     * @param expanded value
     */
    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }
}