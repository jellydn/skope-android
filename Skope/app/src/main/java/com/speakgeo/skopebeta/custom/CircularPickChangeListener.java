package com.speakgeo.skopebeta.custom;

/**
 * Skope
 * <p/>
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */
public interface CircularPickChangeListener {
    /**
     * On progress change.
     *
     * @param view
     *            the view
     * @param newProgress
     *            the new progress
     */
    public void onProgressChange(CircularPick view, int newProgress);
}