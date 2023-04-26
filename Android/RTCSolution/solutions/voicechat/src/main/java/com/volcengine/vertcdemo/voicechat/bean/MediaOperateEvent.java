// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;

/**
 * 媒体被房主开关事件
 */
public class MediaOperateEvent implements RTSBizInform {

    @SerializedName("mic")
    @VoiceChatUserInfo.MicStatus
    public int mic;

    @Override
    public String toString() {
        return "MediaOperateEvent{" +
                "mic=" + mic +
                '}';
    }
}
