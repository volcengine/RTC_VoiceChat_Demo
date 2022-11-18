package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;

public class MediaChangedBroadcast implements RTSBizInform {

    @SerializedName("mic")
    @VCUserInfo.MicStatus
    public int mic;
    @SerializedName("user_info")
    public VCUserInfo userInfo;

    @Override
    public String toString() {
        return "MediaChangedBroadcast{" +
                "mic=" + mic +
                ", userInfo=" + userInfo +
                '}';
    }
}
