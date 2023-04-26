// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 获取正在直播的房间列表接口返回的数据模型
 */
public class GetActiveRoomListResponse extends VoiceChatResponse {

    @SerializedName("room_list")
    public List<VoiceChatRoomInfo> roomList;

    @Override
    public String toString() {
        return "GetActiveRoomListEvent{" +
                "roomList=" + roomList +
                '}';
    }
}
