// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.protocol;

import android.content.Context;
import android.view.View;

import androidx.annotation.IntDef;

import com.volcengine.vertcdemo.common.IAction;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface IVideoPlayer {

    // 默认
    int MODE_NONE = 0;
    // 保持比例适应布局
    int MODE_ASPECT_FIT = 1;
    // 保持比例填充布局
    int MODE_ASPECT_FILL = 2;
    // 不保持比例填充布局
    int MODE_FILL = 3;

    @IntDef({MODE_NONE, MODE_ASPECT_FIT, MODE_ASPECT_FILL, MODE_FILL})
    @Retention(RetentionPolicy.SOURCE)
    @interface ScalingMode {
    }

    /**
     * 配置 Player
     */
    void startWithConfiguration(Context context);


    void setSEICallback(IAction<String> SEICallback);


        /**
         * 设置播放地址、父视图
         * @param url 拉流地址
         * @param container 父类视图
         * @param seiCallback SEI 回调
         */
    void setPlayerUrl(String url, View container);

    /**
     * 更新播放比例模式
     * @param scalingMode 播放比例模式
     */
    void updatePlayScaleModel(@ScalingMode int scalingMode);

    /**
     * 开始播放
     */
    void play();

    /**
     * 更新新的播放地址
     * @param url 新的播放地址
     */
    void replacePlayWithUrl(String url);

    /**
     * 停止播放
     */
    void stop();


    /**
     * 播放器是否支持 SEI 功能
     * @return BOOL YES 支持SEI，NO 不支持 SEI
     */
    boolean isSupportSEI();

    /**
     * 释放播放器资源
     */
    void destroy();
}
