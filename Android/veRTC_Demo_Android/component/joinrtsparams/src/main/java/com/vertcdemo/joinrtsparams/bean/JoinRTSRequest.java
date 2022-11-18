package com.vertcdemo.joinrtsparams.bean;

import com.google.gson.annotations.SerializedName;

public class JoinRTSRequest {

    @SerializedName("scenes_name")
    public String scenesName;
    @SerializedName("login_token")
    public String loginToken;
    @SerializedName("account_id")
    public String volcAccountId;
    @SerializedName("vod_space")
    public String vodSpace;
    @SerializedName("content_partner")
    public String contentPartner;
    @SerializedName("content_category")
    public String contentCategory;

    @Override
    public String toString() {
        return "JoinRTSRequest{" +
                "scenesName='" + scenesName + '\'' +
                ", loginToken='" + loginToken + '\'' +
                ", volcAccountId='" + volcAccountId + '\'' +
                ", vodSpace='" + vodSpace + '\'' +
                ", contentPartner='" + contentPartner + '\'' +
                ", contentCategory='" + contentCategory + '\'' +
                '}';
    }
}
