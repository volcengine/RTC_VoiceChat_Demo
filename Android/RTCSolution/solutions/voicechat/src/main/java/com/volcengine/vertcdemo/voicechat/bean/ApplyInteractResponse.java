// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 观众申请连线接口返回的数据模型
 */
public class ApplyInteractResponse extends VoiceChatResponse {

    @SerializedName("is_need_apply")
    public boolean needApply = false;

    @Override
    public String toString() {
        return "ReplyMicOnEvent{" +
                "needApply=" + needApply +
                '}';
    }
}
