// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import androidx.annotation.IntDef;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 连线状态发生变化事件
 */
public class InteractChangedEvent implements RTSBizInform {

    public static final int FINISH_INTERACT_TYPE_HOST = 1;
    public static final int FINISH_INTERACT_TYPE_SELF = 2;
    @IntDef({FINISH_INTERACT_TYPE_HOST, FINISH_INTERACT_TYPE_SELF})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FinishInteractType {
    }

    public boolean isStart;
    @SerializedName("user_info")
    public VoiceChatUserInfo userInfo;
    @SerializedName("seat_id")
    public int seatId;
    @SerializedName("type")
    @FinishInteractType
    public int type = FINISH_INTERACT_TYPE_HOST;

    public boolean isByHost() {
        return type == FINISH_INTERACT_TYPE_HOST;
    }

    @Override
    public String toString() {
        return "InteractChangedEvent{" +
                "isStart=" + isStart +
                ", userInfo=" + userInfo +
                ", seatId=" + seatId +
                ", type=" + type +
                '}';
    }
}
