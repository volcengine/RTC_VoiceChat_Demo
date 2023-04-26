// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;

/**
 * 媒体状态发生变化事件
 */
public class MediaChangedEvent implements RTSBizInform {

    @SerializedName("mic")
    @VoiceChatUserInfo.MicStatus
    public int mic;
    @SerializedName("user_info")
    public VoiceChatUserInfo userInfo;

    @Override
    public String toString() {
        return "MediaChangedEvent{" +
                "mic=" + mic +
                ", userInfo=" + userInfo +
                '}';
    }
}
