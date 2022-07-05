package com.volcengine.vertcdemo.core.annotation;

import static com.volcengine.vertcdemo.core.annotation.MicStatus.OFF;
import static com.volcengine.vertcdemo.core.annotation.MicStatus.ON;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({OFF, ON})
@Retention(RetentionPolicy.SOURCE)
public @interface MicStatus {
    /**
     * 麦克风状态：关
     */
    int OFF = 0;
    /**
     * 麦克风状态：开
     */
    int ON = 1;
}

