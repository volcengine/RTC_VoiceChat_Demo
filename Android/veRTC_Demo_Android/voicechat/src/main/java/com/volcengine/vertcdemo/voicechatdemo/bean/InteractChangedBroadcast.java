package com.volcengine.vertcdemo.voicechatdemo.bean;

import androidx.annotation.IntDef;

import com.google.gson.annotations.SerializedName;
import com.volcengine.vertcdemo.core.net.rtm.RTMBizInform;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class InteractChangedBroadcast implements RTMBizInform {

    public static final int FINISH_INTERACT_TYPE_HOST = 1;
    public static final int FINISH_INTERACT_TYPE_SELF = 2;
    @IntDef({FINISH_INTERACT_TYPE_HOST, FINISH_INTERACT_TYPE_SELF})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FinishInteractType {
    }

    public boolean isStart;
    @SerializedName("user_info")
    public VCUserInfo userInfo;
    @SerializedName("seat_id")
    public int seatId;
    @SerializedName("type")
    @FinishInteractType
    public int type = FINISH_INTERACT_TYPE_HOST;

    public boolean isByHost() {
        return type == FINISH_INTERACT_TYPE_HOST;
    }

    @Override
    public String toString() {
        return "InteractChangedBroadcast{" +
                "isStart=" + isStart +
                ", userInfo=" + userInfo +
                ", seatId=" + seatId +
                ", type=" + type +
                '}';
    }
}
