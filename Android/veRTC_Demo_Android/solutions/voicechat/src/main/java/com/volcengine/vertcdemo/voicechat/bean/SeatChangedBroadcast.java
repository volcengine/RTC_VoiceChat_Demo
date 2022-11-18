package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;

public class SeatChangedBroadcast implements RTSBizInform {

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
