package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;

public class CreateRoomResponse extends VoiceChatResponse {

    @SerializedName("room_info")
    public VCRoomInfo roomInfo;
    @SerializedName("user_info")
    public VCUserInfo userInfo;
    @SerializedName("rtc_token")
    public String rtcToken;

    @Override
    public String toString() {
        return "CreateRoomResponse{" +
                "roomInfo=" + roomInfo +
                ", userInfo=" + userInfo +
                ", rtcToken='" + rtcToken + '\'' +
                '}';
    }
}
