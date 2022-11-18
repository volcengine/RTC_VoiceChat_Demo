package com.volcengine.vertcdemo.voicechat.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class VCRoomInfo implements Parcelable {

    public static final int INTERACT_ON = 0;
    public static final int INTERACT_OFF = 1;
    @IntDef({INTERACT_ON, INTERACT_OFF})
    @Retention(RetentionPolicy.SOURCE)
    public @interface InteractSwitch {
    }

    public static final int ROOM_STATUS_CREATED = 1;
    public static final int ROOM_STATUS_LIVING = 2;
    public static final int ROOM_STATUS_FINISHED = 3;
    public static final int ROOM_STATUS_CHATTING = 4;
    public static final int ROOM_STATUS_PK_ING = 5;
    @IntDef({ROOM_STATUS_CREATED, ROOM_STATUS_LIVING,ROOM_STATUS_FINISHED,ROOM_STATUS_CHATTING,ROOM_STATUS_PK_ING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RoomStatus {
    }

    @SerializedName("app_id")
    public String appId;
    @SerializedName("room_id")
    public String roomId;
    @SerializedName("room_name")
    public String roomName;
    @SerializedName("host_user_id")
    public String hostUserId;
    @SerializedName("host_user_name")
    public String hostUserName;
    @SerializedName("status")
    public int status;
    @SerializedName("enable_audience_interact_apply")
    @InteractSwitch
    public int enableAudienceInteractApply;
    @SerializedName("start_time")
    public long startTime;
    @SerializedName("end_time")
    public long endTime;
    @SerializedName("audience_count")
    public int audienceCount;
    @SerializedName("ext")
    public String extraInfo;

    public boolean enableInteract() {
        return enableAudienceInteractApply == INTERACT_ON;
    }

    @Override
    public String toString() {
        return "VCRoomInfo{" +
                "appId='" + appId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", hostUserId='" + hostUserId + '\'' +
                ", hostUserName='" + hostUserName + '\'' +
                ", status=" + status +
                ", enableAudienceInteractApply=" + enableAudienceInteractApply +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", audienceCount=" + audienceCount +
                ", extraInfo='" + extraInfo + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appId);
        dest.writeString(this.roomId);
        dest.writeString(this.roomName);
        dest.writeString(this.hostUserId);
        dest.writeString(this.hostUserName);
        dest.writeInt(this.status);
        dest.writeInt(this.enableAudienceInteractApply);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeInt(this.audienceCount);
        dest.writeString(this.extraInfo);
    }

    public void readFromParcel(Parcel source) {
        this.appId = source.readString();
        this.roomId = source.readString();
        this.roomName = source.readString();
        this.hostUserId = source.readString();
        this.hostUserName = source.readString();
        this.status = source.readInt();
        this.enableAudienceInteractApply = source.readInt();
        this.startTime = source.readLong();
        this.endTime = source.readLong();
        this.audienceCount = source.readInt();
        this.extraInfo = source.readString();
    }

    public VCRoomInfo() {
    }

    protected VCRoomInfo(Parcel in) {
        this.appId = in.readString();
        this.roomId = in.readString();
        this.roomName = in.readString();
        this.hostUserId = in.readString();
        this.hostUserName = in.readString();
        this.status = in.readInt();
        this.enableAudienceInteractApply = in.readInt();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.audienceCount = in.readInt();
        this.extraInfo = in.readString();
    }

    public static final Parcelable.Creator<VCRoomInfo> CREATOR = new Parcelable.Creator<VCRoomInfo>() {
        @Override
        public VCRoomInfo createFromParcel(Parcel source) {
            return new VCRoomInfo(source);
        }

        @Override
        public VCRoomInfo[] newArray(int size) {
            return new VCRoomInfo[size];
        }
    };
}
