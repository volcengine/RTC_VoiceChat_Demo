package com.volcengine.vertcdemo.voicechatdemo.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rtm.RTMBizInform;
import com.volcengine.vertcdemo.voicechatdemo.core.VoiceChatDataManager;

public class SeatChangedBroadcast implements RTMBizInform {

    @SerializedName("seat_id")
    public int seatId;
    @SerializedName("type")
    @VoiceChatDataManager.SeatStatus
    public int type;

    @Override
    public String toString() {
        return "SeatChangedEvent{" +
                "seatId=" + seatId +
                ", type=" + type +
                '}';
    }
}
