package com.volcengine.vertcdemo.voicechatdemo.core.event;

import com.volcengine.vertcdemo.voicechatdemo.bean.VCUserInfo;

public class UserStatusChangedEvent {

    public VCUserInfo userInfo;
    @VCUserInfo.UserStatus
    public int status;

    public UserStatusChangedEvent(VCUserInfo userInfo, @VCUserInfo.UserStatus int status) {
        this.userInfo = userInfo;
        this.status = status;
    }
}
