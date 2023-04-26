// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.core.eventbus;

public class RefreshUserNameEvent {
    public boolean isSuccess;
    public String userName;
    public String errorMsg;

    public RefreshUserNameEvent(String userName, boolean isSuccess) {
        this.userName = userName;
        this.isSuccess = isSuccess;
    }
}
