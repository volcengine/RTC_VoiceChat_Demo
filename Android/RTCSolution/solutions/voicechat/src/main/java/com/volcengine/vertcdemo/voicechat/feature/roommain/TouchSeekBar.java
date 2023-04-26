// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.feature.roommain;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 自定义 seekbar 可控制是否响应触摸事件
 */
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
