package com.volcengine.vertcdemo.voicechatdemo.feature.roommain;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TouchSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    public boolean mTouchEnable;

    public TouchSeekBar(@NonNull Context context) {
        super(context);
    }

    public TouchSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTouchAble(boolean enable) {
        mTouchEnable = enable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mTouchEnable && super.onTouchEvent(event);
    }
}
