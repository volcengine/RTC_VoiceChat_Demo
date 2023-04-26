// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.volcengine.vertcdemo.core.R;
import com.volcengine.vertcdemo.core.databinding.ViewCommonKeyValueBinding;

public class KeyValueView extends ConstraintLayout {

    private ViewCommonKeyValueBinding mViewBinding;

    public KeyValueView(Context context) {
        super(context);
        initView();
    }

    public KeyValueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public KeyValueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.view_common_key_value, this);
        mViewBinding = ViewCommonKeyValueBinding.bind(view);
    }

    public void setKeyValue(String key, String value, boolean hasMore) {
        mViewBinding.keyTv.setText(key);
        mViewBinding.valueTv.setText(value);
        mViewBinding.moreIv.setVisibility(hasMore ? VISIBLE : GONE);
    }

    public void setKeyValue(int keyRes, int valueRes, boolean hasMore) {
        mViewBinding.keyTv.setText(keyRes);
        mViewBinding.valueTv.setText(valueRes);
        mViewBinding.moreIv.setVisibility(hasMore ? VISIBLE : GONE);
    }

    public void setKey(String key) {
        mViewBinding.keyTv.setText(key);
    }

    public void setKey(int keyRes) {
        mViewBinding.keyTv.setText(keyRes);
    }

    public void setValue(int valueRes) {
        mViewBinding.valueTv.setText(valueRes);
    }

    public void setValue(String value) {
        mViewBinding.valueTv.setText(value);
    }

    public void setHasMore(boolean hasMore) {
        mViewBinding.moreIv.setVisibility(hasMore ? VISIBLE : GONE);
    }
}
