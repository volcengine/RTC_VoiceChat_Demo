package com.volcengine.vertcdemo.core.eventbus;

public class SDKJoinChannelSuccessEvent {
    public String channelId;
    public String userId;

    public SDKJoinChannelSuccessEvent(String channelId, String userId) {
        this.channelId = channelId;
        this.userId = userId;
    }
}
