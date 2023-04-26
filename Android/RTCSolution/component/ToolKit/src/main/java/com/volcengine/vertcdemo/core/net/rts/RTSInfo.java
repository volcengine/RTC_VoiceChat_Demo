// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.core.net.rts;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * RTM连接必要参数
 */
public class RTSInfo implements Parcelable {
    public static final String KEY_RTS = "key_rts";

    @SerializedName("app_id")
    public String appId;
    @SerializedName("rtm_token")
    public String rtsToken;
    @SerializedName("server_url")
    public String serverUrl;
    @SerializedName("server_signature")
    public String serverSignature;
    @SerializedName("bid")
    public String bid;

    public RTSInfo(String appId, String rtsToken, String serverUrl, String serverSignature, String bid) {
        this.appId = appId;
        this.rtsToken = rtsToken;
        this.serverUrl = serverUrl;
        this.serverSignature = serverSignature;
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "RtmInfo{" +
                "appId='" + appId + '\'' +
                ", rtmToken='" + rtsToken + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", serverSignature='" + serverSignature + '\'' +
                ", bid='" + bid + '\'' +
                '}';
    }

    /**
     * 参数是否有效
     * @return 返回true为有效
     */
    public boolean isValid() {
        return !TextUtils.isEmpty(appId)
                && !TextUtils.isEmpty(rtsToken)
                && !TextUtils.isEmpty(serverUrl)
                && !TextUtils.isEmpty(serverSignature)
                && !TextUtils.isEmpty(bid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appId);
        dest.writeString(this.rtsToken);
        dest.writeString(this.serverUrl);
        dest.writeString(this.serverSignature);
        dest.writeString(this.bid);
    }

    public void readFromParcel(Parcel source) {
        this.appId = source.readString();
        this.rtsToken = source.readString();
        this.serverUrl = source.readString();
        this.serverSignature = source.readString();
        this.bid = source.readString();
    }

    protected RTSInfo(Parcel in) {
        this.appId = in.readString();
        this.rtsToken = in.readString();
        this.serverUrl = in.readString();
        this.serverSignature = in.readString();
        this.bid = in.readString();
    }

    public static final Parcelable.Creator<RTSInfo> CREATOR = new Parcelable.Creator<RTSInfo>() {
        @Override
        public RTSInfo createFromParcel(Parcel source) {
            return new RTSInfo(source);
        }

        @Override
        public RTSInfo[] newArray(int size) {
            return new RTSInfo[size];
        }
    };
}
