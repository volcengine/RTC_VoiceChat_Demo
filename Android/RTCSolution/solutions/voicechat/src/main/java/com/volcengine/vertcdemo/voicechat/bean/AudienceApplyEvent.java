// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;

/**
 * 观众申请上麦事件
 */
public class AudienceApplyEvent implements RTSBizInform {

    public boolean hasNewApply;
    @SerializedName("user_info")
    public VoiceChatUserInfo userInfo;
    @SerializedName("seat_id")
    public int seatId;

    @Override
    public String toString() {
        return "AudienceApplyEvent{" +
                "userInfo=" + userInfo +
                ", seatId=" + seatId +
                '}';
    }
}
