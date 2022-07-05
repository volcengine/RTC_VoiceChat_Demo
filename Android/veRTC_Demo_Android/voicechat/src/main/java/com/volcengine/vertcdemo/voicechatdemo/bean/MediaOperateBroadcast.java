package com.volcengine.vertcdemo.voicechatdemo.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rtm.RTMBizInform;

public class MediaOperateBroadcast implements RTMBizInform {

    @SerializedName("mic")
    @VCUserInfo.MicStatus
    public int mic;

    @Override
    public String toString() {
        return "MediaOperateBroadcast{" +
                "mic=" + mic +
                '}';
    }
}
