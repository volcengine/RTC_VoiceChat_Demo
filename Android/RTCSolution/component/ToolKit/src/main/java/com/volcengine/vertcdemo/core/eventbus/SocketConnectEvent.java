// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.core.eventbus;

public class SocketConnectEvent {
    public ConnectStatus status;

    public SocketConnectEvent(ConnectStatus status) {
        this.status = status;
    }

    public enum ConnectStatus {
        CONNECTED,
        CONNECTING,
        RECONNECTED,
        DISCONNECTED
    }
}
