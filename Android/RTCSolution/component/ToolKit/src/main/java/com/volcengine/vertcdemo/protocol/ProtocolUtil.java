// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.protocol;

import android.view.View;

import androidx.annotation.Nullable;

import java.util.Set;

public class ProtocolUtil {

    private static volatile IEffect sIEffect = null;

    /**
     * 获取美颜接口
     * @return 美颜接口的实现
     */
    public static @Nullable IEffect getIEffect() {
        if (sIEffect != null) {
            return sIEffect;
        }
        try {
            Class<?> clazz = Class.forName("com.volcengine.vertcdemo.effect.IEffectImpl");
            Object obj = clazz.newInstance();
            if (obj instanceof IEffect) {
                sIEffect = (IEffect) obj;
                return sIEffect;
            } else {
                return null;
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 初始化反馈
     * @param view 反馈控件的父布局
     */
    public static void initFeedback(View view, Set<String> availableSceneNameAbbr) {
        try {
            Class<?> clazz = Class.forName("com.vertcdemo.feedback.IFeedbackImpl");
            Object obj = clazz.newInstance();
            if (obj instanceof IFeedback) {
                ((IFeedback) obj).initWithViewGroup(view, availableSceneNameAbbr);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static IVideoPlayer getPlayerInstance() {
        try {
            Class<?> clazz = Class.forName("com.volcengine.vertcdemo.playerkit.IVideoPlayerImpl");
            Object obj = clazz.newInstance();
            if (obj instanceof IVideoPlayer) {
                return (IVideoPlayer) obj;
            } else {
                return null;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
