// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.core.net.http;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.volcengine.vertcdemo.core.R;
import com.volcengine.vertcdemo.core.databinding.ViewTopTipBinding;

public class TopTipView extends LinearLayout {

    private ViewTopTipBinding mViewBinding;

    public TopTipView(Context context) {
        super(context);
        initView();
    }

    public TopTipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TopTipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.view_top_tip, this);
        mViewBinding = ViewTopTipBinding.bind(view);
    }

    public void setNetworkDisconnect() {
        setInfo(R.drawable.close_red, R.string.network_link_down);
    }

    public void setInfo(int iconRes, int textRes) {
        mViewBinding.icon.setImageResource(iconRes);
        mViewBinding.text.setText(textRes);
    }

    public void setInfo(Drawable icon, String text) {
        mViewBinding.icon.setImageDrawable(icon);
        mViewBinding.text.setText(text);
    }
}
