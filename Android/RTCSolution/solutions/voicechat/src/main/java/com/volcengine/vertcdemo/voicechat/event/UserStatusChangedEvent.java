// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.event;

import com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo;

/**
 * 用户状态变化事件
 */
public class UserStatusChangedEvent {

    public VoiceChatUserInfo userInfo;
    @VoiceChatUserInfo.UserStatus
    public int status;

    public UserStatusChangedEvent(VoiceChatUserInfo userInfo, @VoiceChatUserInfo.UserStatus int status) {
        this.userInfo = userInfo;
        this.status = status;
    }
}
