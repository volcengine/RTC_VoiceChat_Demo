// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用户数据模型
 */
public class VoiceChatUserInfo implements Parcelable {

    public static final int USER_ROLE_HOST = 1;
    public static final int USER_ROLE_AUDIENCE = 2;
    @IntDef({USER_ROLE_HOST, USER_ROLE_AUDIENCE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UserRole {
    }

    public static final int USER_STATUS_NORMAL = 1;
    public static final int USER_STATUS_INTERACT = 2;
    public static final int USER_STATUS_APPLYING = 3;
    public static final int USER_STATUS_INVITING = 4;
    @IntDef({USER_STATUS_NORMAL, USER_STATUS_INTERACT, USER_STATUS_INVITING, USER_STATUS_APPLYING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UserStatus {
    }

    public static final int USER_STATUS_TYPE_NORMAL = 0;
    public static final int USER_STATUS_TYPE_CHAT = 1;
    public static final int USER_STATUS_TYPE_PK = 2;
    @IntDef({USER_STATUS_TYPE_NORMAL,USER_STATUS_TYPE_CHAT, USER_STATUS_TYPE_PK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UserStatusType {
    }

    public static final int CAMERA_STATUS_OFF = 0;
    public static final int CAMERA_STATUS_ON = 1;
    @IntDef({CAMERA_STATUS_OFF, CAMERA_STATUS_ON})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraStatus {
    }

    public static final int MIC_STATUS_OFF = 0;
    public static final int MIC_STATUS_ON = 1;
    @IntDef({MIC_STATUS_OFF, MIC_STATUS_ON})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MicStatus {
    }

    @SerializedName("room_id")
    public String roomId;
    @SerializedName("user_id")
    public String userId;
    @SerializedName("user_name")
    public String userName;
    @SerializedName("user_role")
    @UserRole
    public int userRole = USER_ROLE_AUDIENCE;
    @SerializedName("status")
    @UserStatus
    public int userStatus = USER_STATUS_NORMAL;
    @SerializedName("mic")
    @MicStatus
    public int mic;
    @SerializedName("camera")
    @CameraStatus
    public int camera;

    public boolean isMicOn() {
        return mic != MIC_STATUS_OFF;
    }

    public boolean isCameraOn() {
        return camera != CAMERA_STATUS_OFF;
    }

    public boolean isHost() {
        return userRole == USER_ROLE_HOST;
    }

    public VoiceChatUserInfo deepCopy() {
        VoiceChatUserInfo info = new VoiceChatUserInfo();
        info.roomId = roomId;
        info.userId = userId;
        info.userName = userName;
        info.userRole = userRole;
        info.userStatus = userStatus;
        info.mic = mic;
        info.camera = camera;
        return info;
    }

    @Override
    public String toString() {
        return "VoiceChatUserInfo{" +
                "roomId='" + roomId + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userRole=" + userRole +
                ", userStatus=" + userStatus +
                ", mic=" + mic +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.roomId);
        dest.writeString(this.userId);
        dest.writeString(this.userName);
        dest.writeInt(this.userRole);
        dest.writeInt(this.userStatus);
        dest.writeInt(this.mic);
        dest.writeInt(this.camera);
    }

    public void readFromParcel(Parcel source) {
        this.roomId = source.readString();
        this.userId = source.readString();
        this.userName = source.readString();
        this.userRole = source.readInt();
        this.userStatus = source.readInt();
        this.mic = source.readInt();
        this.camera = source.readInt();
    }

    public VoiceChatUserInfo() {
    }

    protected VoiceChatUserInfo(Parcel in) {
        this.roomId = in.readString();
        this.userId = in.readString();
        this.userName = in.readString();
        this.userRole = in.readInt();
        this.userStatus = in.readInt();
        this.mic = in.readInt();
        this.camera = in.readInt();
    }

    public static final Parcelable.Creator<VoiceChatUserInfo> CREATOR = new Parcelable.Creator<VoiceChatUserInfo>() {
        @Override
        public VoiceChatUserInfo createFromParcel(Parcel source) {
            return new VoiceChatUserInfo(source);
        }

        @Override
        public VoiceChatUserInfo[] newArray(int size) {
            return new VoiceChatUserInfo[size];
        }
    };
}
