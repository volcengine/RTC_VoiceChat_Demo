package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;

public class InteractResultBroadcast implements RTSBizInform {

    @SerializedName("reply")
    @VoiceChatDataManager.ReplyType
    public int reply;
    @SerializedName("user_info")
    public VCUserInfo userInfo;

    @Override
    public String toString() {
        return "InteractResultBroadcast{" +
                "reply=" + reply +
                ", userInfo=" + userInfo +
                '}';
    }
}
