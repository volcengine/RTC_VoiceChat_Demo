// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 回复连线接口返回的数据模型
 */
public class InteractReplyResponse extends VoiceChatResponse {

    @SerializedName("user_info")
    public VoiceChatUserInfo userInfo;

    @Override
    public String toString() {
        return "InteractReplyEvent{" +
                "userInfo=" + userInfo +
                '}';
    }
}
