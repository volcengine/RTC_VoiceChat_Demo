// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import androidx.annotation.IntDef;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 结束直播事件
 */
public class FinishLiveEvent implements RTSBizInform {

    public static final int FINISH_TYPE_NORMAL = 1;
    public static final int FINISH_TYPE_TIMEOUT = 2;
    public static final int FINISH_TYPE_AGAINST = 3;

    @IntDef({FINISH_TYPE_NORMAL, FINISH_TYPE_TIMEOUT, FINISH_TYPE_AGAINST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FinishType {
    }

    @SerializedName("room_id")
    public String roomId;
    @SerializedName("type")
    @FinishType
    public int type;

    @Override
    public String toString() {
        return "FinishLiveEvent{" +
                "roomId='" + roomId + '\'' +
                ", type=" + type +
                '}';
    }
}
