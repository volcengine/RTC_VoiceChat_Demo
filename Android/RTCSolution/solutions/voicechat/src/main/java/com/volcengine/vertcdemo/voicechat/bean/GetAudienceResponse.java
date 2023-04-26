// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 获取观众列表接口返回的数据模型
 */
public class GetAudienceResponse extends VoiceChatResponse {
    @SerializedName("audience_list")
    public List<VoiceChatUserInfo> audienceList;

    @Override
    public String toString() {
        return "GetAudienceEvent{" +
                "audienceList=" + audienceList +
                '}';
    }
}
