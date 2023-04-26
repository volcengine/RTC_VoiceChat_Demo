// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;

/**
 * 收到连线邀请结果事件
 */
public class InteractResultEvent implements RTSBizInform {

    @SerializedName("reply")
    @VoiceChatDataManager.ReplyType
    public int reply;
    @SerializedName("user_info")
    public VoiceChatUserInfo userInfo;

    @Override
    public String toString() {
        return "InteractResultEvent{" +
                "reply=" + reply +
                ", userInfo=" + userInfo +
                '}';
    }
}
