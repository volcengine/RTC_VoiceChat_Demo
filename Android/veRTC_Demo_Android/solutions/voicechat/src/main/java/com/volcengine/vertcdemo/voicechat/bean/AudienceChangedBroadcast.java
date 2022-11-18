package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;

public class AudienceChangedBroadcast implements RTSBizInform {

    public boolean isJoin;
    @SerializedName("user_info")
    public VCUserInfo userInfo;
    @SerializedName("audience_count")
    public int audienceCount;

    @Override
    public String toString() {
        return "AudienceChangedBroadcast{" +
                "userInfo=" + userInfo +
                ", audienceCount=" + audienceCount +
                '}';
    }
}
