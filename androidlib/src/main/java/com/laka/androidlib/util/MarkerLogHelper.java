package com.laka.androidlib.util;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MarkerLogHelper
 * @Description: A simple event log with records containing a name, thread ID, and timestamp.
 * @Author: chuan
 * @Date: 09/01/2018
 */

public class MarkerLogHelper {
    /**
     * Minimum duration from first marker to last in an marker log to warrant logging.
     */
    private static final long MIN_DURATION_FOR_LOGGING_MS = 0;

    private static class Marker {
        private final String name;
        private final long thread;
        private final long time;

        private Marker(String name, long thread, long time) {
            this.name = name;
            this.thread = thread;
            this.time = time;
        }
    }

    private boolean isLogVisible = false;
    private String tag;

    private final List<Marker> mMarkers = new ArrayList<>();
    private boolean mFinished = false;

    public MarkerLogHelper(String tag, boolean isLogVisible) {
        this.tag = tag;
        this.isLogVisible = isLogVisible;
    }

    /**
     * 添加log消息
     *
     * @param name log名
     */
    public void add(String name) {
        add(name, Thread.currentThread().getId());
    }

    /**
     * 添加一个marker实体到log中
     *
     * @param name     实体名
     * @param threadId 线程id
     */
    public synchronized void add(String name, long threadId) {
        if (isLogVisible) {
            if (mFinished) {
//                    throw new IllegalStateException("Marker added to finished log");
                LogUtils.error(tag, "Marker added to finished log");
            }

            mMarkers.add(new Marker(name, threadId, SystemClock.elapsedRealtime()));
        }
    }

    /**
     * Closes the log, dumping it to logcat if the time difference between
     * the first and last markers is greater than {@link #MIN_DURATION_FOR_LOGGING_MS}.
     *
     * @param header Header string to print above the marker log.
     */
    public synchronized void finish(String header) {
        mFinished = true;

        if (isLogVisible) {
            long duration = getTotalDuration();
            if (duration <= MIN_DURATION_FOR_LOGGING_MS) {
                return;
            }

            long prevTime = mMarkers.get(0).time;
            LogUtils.debug(tag, "(%-4d ms) %s", duration, header);
            for (Marker marker : mMarkers) {
                long thisTime = marker.time;
                LogUtils.debug(tag, "(+%-4d) [%2d] %s", (thisTime - prevTime), marker.thread, marker.name);
                prevTime = thisTime;
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        // Catch requests that have been collected (and hence end-of-lifed)
        // but had no debugging output printed for them.
        if (!mFinished) {
            finish("Request on the loose");
            LogUtils.error(tag, "Marker log finalized without finish() - uncaught exit point for request");
        }
    }

    /**
     * 计算此次log时间经历的时长
     */
    private long getTotalDuration() {
        if (mMarkers.size() == 0) {
            return 0;
        }

        long first = mMarkers.get(0).time;
        long last = mMarkers.get(mMarkers.size() - 1).time;
        return last - first;
    }
}