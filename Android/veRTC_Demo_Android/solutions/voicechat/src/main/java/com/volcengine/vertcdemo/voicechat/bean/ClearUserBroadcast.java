package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;

public class ClearUserBroadcast implements RTSBizInform {

    @SerializedName("room_id")
    public String roomId;
    @SerializedName("user_id")
    public String userId;

    @Override
    public String toString() {
        return "ClearUserBroadcast{" +
                "roomId='" + roomId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
