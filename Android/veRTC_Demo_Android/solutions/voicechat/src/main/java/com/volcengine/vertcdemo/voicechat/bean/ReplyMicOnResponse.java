package com.volcengine.vertcdemo.voicechat.bean;

import com.google.gson.annotations.SerializedName;

public class ReplyMicOnResponse extends VoiceChatResponse {

    @SerializedName("is_need_apply")
    public boolean needApply = false;

    @Override
    public String toString() {
        return "ReplyMicOnRequest{" +
                "needApply=" + needApply +
                '}';
    }
}
