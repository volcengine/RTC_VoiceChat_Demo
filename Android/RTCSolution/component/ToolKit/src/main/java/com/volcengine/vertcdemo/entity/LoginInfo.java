// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.entity;

public class LoginInfo {
    public String user_id;
    public String user_name;
    public String login_token;
    public long created_at;

    @Override
    public String toString() {
        return "LoginInfo{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", login_token='" + login_token + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
