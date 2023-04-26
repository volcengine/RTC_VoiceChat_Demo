// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;

import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.SEAT_STATUS_LOCKED;

/**
 * 座位数据模型
 */
public class VoiceChatSeatInfo {
    public int seatIndex = -1;
    @SerializedName("status")
    @VoiceChatDataManager.SeatStatus
    public int status;
    @SerializedName("guest_info")
    public VoiceChatUserInfo userInfo;

    public boolean isLocked() {
        return status == SEAT_STATUS_LOCKED;
    }

    public VoiceChatSeatInfo deepCopy() {
        VoiceChatSeatInfo info = new VoiceChatSeatInfo();
        info.seatIndex = seatIndex;
        info.status = status;
        info.userInfo = userInfo == null ? null : userInfo.deepCopy();
        return info;
    }

    @Override
    public String toString() {
        return "VoiceChatSeatInfo{" +
                "seatIndex=" + seatIndex +
                ", status=" + status +
                ", userInfo=" + userInfo +
                '}';
    }
}
