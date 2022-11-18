package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;

public class MediaOperateBroadcast implements RTSBizInform {

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
