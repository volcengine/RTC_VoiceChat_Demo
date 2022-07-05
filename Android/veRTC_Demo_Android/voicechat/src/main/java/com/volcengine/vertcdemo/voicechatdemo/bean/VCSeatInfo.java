package com.volcengine.vertcdemo.voicechatdemo.bean;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.voicechatdemo.core.VoiceChatDataManager;

import static com.volcengine.vertcdemo.voicechatdemo.core.VoiceChatDataManager.SEAT_STATUS_LOCKED;

public class VCSeatInfo {
    public int seatIndex = -1;
    @SerializedName("status")
    @VoiceChatDataManager.SeatStatus
    public int status;
    @SerializedName("guest_info")
    public VCUserInfo userInfo;

    public boolean isLocked() {
        return status == SEAT_STATUS_LOCKED;
    }

    public VCSeatInfo deepCopy() {
        VCSeatInfo info = new VCSeatInfo();
        info.seatIndex = seatIndex;
        info.status = status;
        info.userInfo = userInfo == null ? null : userInfo.deepCopy();
        return info;
    }

    @Override
    public String toString() {
        return "VCSeatInfo{" +
                "seatIndex=" + seatIndex +
                ", status=" + status +
                ", userInfo=" + userInfo +
                '}';
    }
}
