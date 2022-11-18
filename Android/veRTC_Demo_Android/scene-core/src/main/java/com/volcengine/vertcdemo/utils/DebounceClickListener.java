package com.volcengine.vertcdemo.utils;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.view.View;

import androidx.annotation.NonNull;

import com.volcengine.vertcdemo.core.R;


public class DebounceClickListener implements View.OnClickListener {
    public static final long DURATION = 500L;
    private final View.OnClickListener mListener;
    private final long mDuration;

    private DebounceClickListener(@NonNull View.OnClickListener listener, long duration) {
        this.mListener = listener;
        this.mDuration = duration;
    }

    @SuppressLint("LongLogTag")
    @Override
    public final void onClick(@NonNull View v) {
        Object lastClickTsTag = v.getTag(R.id.view_click_last_ts);
        long lastClickTs = 0L;
        if (lastClickTsTag instanceof Long) {
            lastClickTs = (Long) lastClickTsTag;
        }
        long curClickTs = SystemClock.elapsedRealtime();
        if (curClickTs - lastClickTs < mDuration) {
            return;
        }
        v.setTag(R.id.view_click_last_ts, curClickTs);
        mListener.onClick(v);
    }

    public static DebounceClickListener create(View.OnClickListener listener) {
        return new DebounceClickListener(listener, DURATION);
    }

    public static DebounceClickListener create(View.OnClickListener listener, long duration) {
        return new DebounceClickListener(listener, duration);
    }
}
