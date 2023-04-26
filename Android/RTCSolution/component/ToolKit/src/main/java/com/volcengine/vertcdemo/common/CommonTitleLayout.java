// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.volcengine.vertcdemo.core.R;
import com.volcengine.vertcdemo.core.databinding.LayoutCommonTitleBinding;

/**
 * 通用的标题控件
 *
 * 包含：左侧按钮、标题、副标题、右侧按钮/右侧文案
 *
 * 说明：
 * 如果不主动设置，则该项为空，即没有默认值
 */
public class CommonTitleLayout extends RelativeLayout {

    private LayoutCommonTitleBinding mViewBinding;

    public CommonTitleLayout(Context context) {
        super(context);
        initView();
    }

    public CommonTitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CommonTitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.layout_common_title, this);
        mViewBinding = LayoutCommonTitleBinding.bind(view);
    }

    /**
     * 设置左边图标设置为返回，设置点击事件
     * @param onClickListener 点击事件
     */
    public void setLeftBack(OnClickListener onClickListener) {
        mViewBinding.titleBarLeftIv.setImageResource(R.drawable.common_back_arrow);
        mViewBinding.titleBarLeftIv.setOnClickListener(onClickListener);
    }

    /**
     * 设置左边图标和点击事件
     * @param drawableRes 图标资源id
     * @param onClickListener 点击事件
     */
    public void setLeftIcon(int drawableRes, OnClickListener onClickListener) {
        mViewBinding.titleBarLeftIv.setImageResource(drawableRes);
        mViewBinding.titleBarLeftIv.setOnClickListener(onClickListener);
    }

    /**
     * 设置左边图标设置为刷新，设置点击事件
     * @param onClickListener 点击事件
     */
    public void setRightRefresh(OnClickListener onClickListener) {
        mViewBinding.titleBarRightIv.setImageResource(R.drawable.common_refresh);
        mViewBinding.titleBarRightIv.setOnClickListener(onClickListener);
    }

    /**
     * 设置右边图标和点击事件
     * @param drawableRes 图标资源id
     * @param onClickListener 点击事件
     */
    public void setRightIcon(int drawableRes, OnClickListener onClickListener) {
        mViewBinding.titleBarRightIv.setImageResource(drawableRes);
        mViewBinding.titleBarRightIv.setOnClickListener(onClickListener);
    }

    /**
     * 设置右边文案和点击事件
     * @param textRes 文案资源id
     * @param onClickListener 点击事件
     */
    public void setRightText(int textRes, OnClickListener onClickListener) {
        mViewBinding.titleBarRightTv.setText(textRes);
        mViewBinding.titleBarRightTv.setOnClickListener(onClickListener);
    }

    /**
     * 设置右侧文案
     * @param text 文案内容
     * @param onClickListener 点击事件
     */
    public void setRightText(String text, OnClickListener onClickListener) {
        mViewBinding.titleBarRightTv.setText(text);
        mViewBinding.titleBarRightTv.setOnClickListener(onClickListener);
    }

    /**
     * 设置主标题
     * @param textRes 文案资源id
     */
    public void setTitle(int textRes) {
        mViewBinding.titleBarTitleTv.setText(textRes);
    }

    /**
     * 设置主标题
     * @param text 文案内容
     */
    public void setTitle(String text) {
        mViewBinding.titleBarTitleTv.setText(text);
    }

    /**
     * 设置副标题
     * @param textRes 文案资源id
     */
    public void setSubTitle(int textRes) {
        mViewBinding.titleBarSubTitleTv.setText(textRes);
    }

    /**
     * 设置副标题
     * @param text 文案内容
     */
    public void setSubTitle(String text) {
        mViewBinding.titleBarSubTitleTv.setText(text);
    }
}
