// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.core.annotation;

import static com.volcengine.vertcdemo.core.annotation.CameraStatus.OFF;
import static com.volcengine.vertcdemo.core.annotation.CameraStatus.ON;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({OFF, ON})
@Retention(RetentionPolicy.SOURCE)
public @interface CameraStatus {
    /**
     * 摄像头状态：关
     */
    public static final int OFF = 0;
    /**
     * 摄像头状态：开
     */
    public static final int ON = 1;
}