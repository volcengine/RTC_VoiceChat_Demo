package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;

public class AudienceApplyBroadcast implements RTSBizInform {

    public boolean hasNewApply;
    @SerializedName("user_info")
    public VCUserInfo userInfo;
    @SerializedName("seat_id")
    public int seatId;

    @Override
    public String toString() {
        return "AudienceApplyBroadcast{" +
                "userInfo=" + userInfo +
                ", seatId=" + seatId +
                '}';
    }
}
