// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.vertcdemo.joinrtsparams.bean;

import com.google.gson.annotations.SerializedName;

public class JoinRTSRequest {

    @SerializedName("scenes_name")
    public final String scenesName;
    @SerializedName("login_token")
    public final String loginToken;

    public JoinRTSRequest(String scenesName, String loginToken) {
        this.scenesName = scenesName;
        this.loginToken = loginToken;
    }

    @Override
    public String toString() {
        return "JoinRTSRequest{" +
                "scenesName='" + scenesName + '\'' +
                ", loginToken='" + loginToken + '\'' +
                '}';
    }
}
