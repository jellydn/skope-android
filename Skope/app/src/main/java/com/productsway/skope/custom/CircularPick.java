package com.productsway.skope.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.productsway.skope.R;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public class CircularPick extends View {
    /** Delegate for Change event listener */
    private CircularPickChangeListener mDelegate;

    /** The current progress */
    private int progress = 0;

    /** The current angle (degree) */
    private int angle = 0;

    /** The maximum progress amount */
    private int maxProgress = 100;

    /** Range of touch */
    private float rangeOfTouch = 100;
    
    
    private Context mContext;
    private Bitmap progressMark;
    private Bitmap progressMarkPressed;

    private Paint progressColor;
    private Paint progressBgColor;
    private RectF rect = new RectF();
    private float innerRadius;
    private float outerRadius;
    private int progressWidth;
    private int progressBorder;
    private float cx;
    private float cy;
    private float left;
    private float right;
    private float top;
    private float bottom;
    private float startPointX;
    private float startPointY;
    private float markPointX;
    private float markPointY;

    private boolean isPressing = false;

    /**
     * Instantiates control.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     * @param defStyle
     *            the def style
     */
    public CircularPick(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    /**
     * Instantiates control.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     */
    public CircularPick(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    /**
     * Instantiates control.
     *
     * @param context
     *            the context
     */
    public CircularPick(Context context) {
        super(context);
        mContext = context;
        init();
    }

    /**
     * Initialize.
     */
    public void init() {
        progressWidth = 50;
        progressBorder = 20;

        progressColor = new Paint();
        progressBgColor = new Paint();

        progressBgColor.setColor(Color.TRANSPARENT);
        progressColor.setColor(Color.TRANSPARENT);


        progressColor.setStrokeWidth(this.progressWidth - progressBorder);
        progressBgColor.setStrokeWidth(this.progressWidth);

        progressColor.setStyle(Paint.Style.STROKE);
        progressBgColor.setStyle(Paint.Style.STROKE);

        progressColor.setAntiAlias(true);
        progressBgColor.setAntiAlias(true);

        progressMark = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_circular_pick_mark);
        progressMarkPressed = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ic_circular_pick_mark_active);

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getWidth();
        int height = getHeight();

        int size = (width > height) ? height : width;

        size -= progressMark.getWidth();

        cx = width / 2;
        cy = height / 2;

        outerRadius = size / 2;
        innerRadius = outerRadius - progressWidth;

        left = cx - outerRadius;
        right = cx + outerRadius;
        top = cy - outerRadius;
        bottom = cy + outerRadius;

        startPointX = cx;
        startPointY = cy - outerRadius;

        if(markPointX == 0) {
            markPointX = startPointX;
            markPointY = startPointY;
        }
Log.d("SAN","AA "+markPointX);
        rect.set(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(cx, cy, outerRadius, progressBgColor);
        canvas.drawArc(rect, 270, angle, false, progressColor);

        drawMarker(canvas);

        super.onDraw(canvas);
    }

    private void drawMarker(Canvas canvas) {
        Log.d("SAN","BB "+markPointX);
        float drawableX = markPointX - (progressMark.getWidth() / 2);
        float drawableY = markPointY - (progressMark.getWidth() / 2);

        if (isPressing) {
            canvas.drawBitmap(progressMarkPressed, drawableX, drawableY, null);
        } else {
            canvas.drawBitmap(progressMark, drawableX, drawableY, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.movingMark(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                this.movingMark(x, y);
                break;
            case MotionEvent.ACTION_UP:
                isPressing = false;
                invalidate();

                break;
        }
        return true;
    }

    private void movingMark(float x, float y) {
        float distance = (float) Math.sqrt(Math.pow((x - cx), 2) + Math.pow((y - cy), 2));
        if (distance < outerRadius + rangeOfTouch && distance > innerRadius - rangeOfTouch) {
            isPressing = true;

            double newAngle = Math.atan2(x - cx, cy - y);

            this.markPointX = (float) (cx + outerRadius * Math.cos(newAngle - (Math.PI /2)));
            this.markPointY = (float) (cy + outerRadius * Math.sin(newAngle - (Math.PI /2)));
            Log.d("SAN","CC "+markPointX);
            float degrees = (float) ((float) ((Math.toDegrees(newAngle) + 360.0)) % 360.0);
            if (degrees < 0) {
                degrees += 2 * Math.PI;
            }

            setAngle(Math.round(degrees));

            invalidate();
        }
    }

    /**
     * Set the angle.
     *
     * @param angle
     *            the new angle
     */
    public void setAngle(int angle) {
        //prevent loop
//        if(Math.abs(angle-this.angle)>270) {
//            if(angle<180) angle = 360;
//            else angle = 0;
//
//            markPointX = startPointX;
//            markPointY = startPointY;
//        }

        //
        this.angle = angle;
        this.progress = Math.round(this.angle / 360.0f  * this.maxProgress);

        if(mDelegate != null)
            mDelegate.onProgressChange(this, this.progress);
    }

    /**
     * Set the progress.
     *
     * @param progress
     *            the new progress
     */
    public void setProgress(int progress) {
        if(progress>this.maxProgress) return;

        this.progress = progress;
        this.angle = Math.round((float)this.progress / this.maxProgress  * 360);

        if(mDelegate != null)
            mDelegate.onProgressChange(this, this.progress);
    }

    /**
     * Get the progress.
     *
     * @return the progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Set the bar width.
     *
     * @param progressWidth
     *            the new bar width
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * Get max of progress
     *
     * @return the max of progress
     */
    public int getMaxProgress() {
        return maxProgress;
    }

    /**
     * Set the max progress.
     *
     * @param maxProgress
     *            the new max progress
     */
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    /**
     * Get range of touch.
     *
     * @return the range of touch
     */
    public float getRangeOfTouch() {
        return rangeOfTouch;
    }

    /**
     * Set range of touch
     *
     * @param rangeOfTouch
     *            the new range of touch
     */
    public void setRangeOfTouch(float rangeOfTouch) {
        this.rangeOfTouch = rangeOfTouch;
    }

    /**
     * Set circular pick change listener.
     *
     * @param listener
     *            the circular pick change listener
     */
    public void setCircularPickChangeListener(CircularPickChangeListener listener) {
        mDelegate = listener;
    }
}