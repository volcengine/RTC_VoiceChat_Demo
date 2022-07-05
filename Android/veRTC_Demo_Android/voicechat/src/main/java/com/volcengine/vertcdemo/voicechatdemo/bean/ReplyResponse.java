package com.volcengine.vertcdemo.voicechatdemo.bean;

import com.google.gson.annotations.SerializedName;

public class ReplyResponse extends VoiceChatResponse {

    @SerializedName("user_info")
    public VCUserInfo userInfo;

    @Override
    public String toString() {
        return "ReplyResponse{" +
                "userInfo=" + userInfo +
                '}';
    }
}
