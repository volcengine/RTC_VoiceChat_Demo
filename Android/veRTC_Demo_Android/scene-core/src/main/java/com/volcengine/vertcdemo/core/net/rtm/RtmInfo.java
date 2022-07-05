package com.volcengine.vertcdemo.core.net.rtm;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * RTM连接必要参数
 */
public class RtmInfo implements Parcelable {
    public static final String KEY_RTM = "key_rtm";

    @SerializedName("app_id")
    public String appId;
    @SerializedName("rtm_token")
    public String rtmToken;
    @SerializedName("server_url")
    public String serverUrl;
    @SerializedName("server_signature")
    public String serverSignature;

    public RtmInfo(String appId, String rtmToken, String serverUrl, String serverSignature) {
        this.appId = appId;
        this.rtmToken = rtmToken;
        this.serverUrl = serverUrl;
        this.serverSignature = serverSignature;
    }

    @Override
    public String toString() {
        return "RtmInfo{" +
                "appId='" + appId + '\'' +
                ", rtmToken='" + rtmToken + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", serverSignature='" + serverSignature + '\'' +
                '}';
    }

    /**
     * 参数是否有效
     * @return 返回true为有效
     */
    public boolean isValid() {
        return !TextUtils.isEmpty(appId)
                && !TextUtils.isEmpty(rtmToken)
                && !TextUtils.isEmpty(serverUrl)
                && !TextUtils.isEmpty(serverSignature);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appId);
        dest.writeString(this.rtmToken);
        dest.writeString(this.serverUrl);
        dest.writeString(this.serverSignature);
    }

    public void readFromParcel(Parcel source) {
        this.appId = source.readString();
        this.rtmToken = source.readString();
        this.serverUrl = source.readString();
        this.serverSignature = source.readString();
    }

    protected RtmInfo(Parcel in) {
        this.appId = in.readString();
        this.rtmToken = in.readString();
        this.serverUrl = in.readString();
        this.serverSignature = in.readString();
    }

    public static final Parcelable.Creator<RtmInfo> CREATOR = new Parcelable.Creator<RtmInfo>() {
        @Override
        public RtmInfo createFromParcel(Parcel source) {
            return new RtmInfo(source);
        }

        @Override
        public RtmInfo[] newArray(int size) {
            return new RtmInfo[size];
        }
    };
}
