// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.core.eventbus;

/**
 * SDK 重连进房事件
 */
public class SDKReconnectToRoomEvent {

    public String roomId;

    public SDKReconnectToRoomEvent(String roomId) {
        this.roomId = roomId;
    }
}
