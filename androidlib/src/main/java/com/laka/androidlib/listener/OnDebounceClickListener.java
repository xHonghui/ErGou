package com.laka.androidlib.listener;

import android.util.Log;
import android.view.View;

/**
 * @ClassName: OnDebounceClickListener
 * @Description: 防抖动的OnClickListener
 * @Author: chuan
 * @Date: 28/03/2018
 */

public abstract class OnDebounceClickListener implements View.OnClickListener {
    private final static String TAG = OnDebounceClickListener.class.getCanonicalName();
    private final static int CLICK_THRESHOLD = 1500;

    private long mLastClickTime = 0;

    @Override
    public void onClick(View v) {
        Log.d(TAG, "c : " + System.currentTimeMillis() + " ; l : " + mLastClickTime);
        if (System.currentTimeMillis() - mLastClickTime >= CLICK_THRESHOLD) {
            handleClickEvent(v);
            mLastClickTime = System.currentTimeMillis();
        }
    }

    public abstract void handleClickEvent(View v);
}
