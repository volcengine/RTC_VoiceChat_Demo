// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.protocol;

import com.volcengine.vertcdemo.core.net.rts.RTSBaseClient;

public interface IGetGameParams {

    void getSudAppId(RTSBaseClient client, SudAppIdCallback callback);

    void getGameCode(RTSBaseClient client, GameCodeCallback callback);

    interface SudAppIdCallback {

        void onSuccess(String appId, String appKey);

        void onFailed();
    }

    interface GameCodeCallback {

        void onSuccess(String code);

        void onFailed();
    }
}
