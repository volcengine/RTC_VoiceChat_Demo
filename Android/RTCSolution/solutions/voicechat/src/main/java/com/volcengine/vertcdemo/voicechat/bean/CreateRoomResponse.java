// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 创建房间接口返回的数据模型
 */
public class CreateRoomResponse extends VoiceChatResponse {

    @SerializedName("room_info")
    public VoiceChatRoomInfo roomInfo;
    @SerializedName("user_info")
    public VoiceChatUserInfo userInfo;
    @SerializedName("rtc_token")
    public String rtcToken;

    @Override
    public String toString() {
        return "CreateRoomEvent{" +
                "roomInfo=" + roomInfo +
                ", userInfo=" + userInfo +
                ", rtcToken='" + rtcToken + '\'' +
                '}';
    }
}
