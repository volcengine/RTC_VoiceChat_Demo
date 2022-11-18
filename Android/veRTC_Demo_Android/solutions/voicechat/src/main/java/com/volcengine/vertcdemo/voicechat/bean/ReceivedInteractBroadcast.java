package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;

public class ReceivedInteractBroadcast implements RTSBizInform {

    @SerializedName("host_info")
    public VCUserInfo userInfo;
    @SerializedName("seat_id")
    public int seatId;

    @Override
    public String toString() {
        return "ReceivedInteractBroadcast{" +
                "userInfo=" + userInfo +
                ", seatId=" + seatId +
                '}';
    }
}
