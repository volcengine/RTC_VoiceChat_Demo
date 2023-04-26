// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.core;

import static com.volcengine.vertcdemo.core.SolutionConstants.SP_KEY_DEVICE_ID;

import android.text.TextUtils;

import com.volcengine.vertcdemo.common.SPUtils;
import com.volcengine.vertcdemo.core.eventbus.RefreshUserNameEvent;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.eventbus.AppTokenExpiredEvent;

import java.util.UUID;

public class SolutionDataManager {

    private static final Object sDidLock = new Object();

    private static final SolutionDataManager sInstance = new SolutionDataManager();

    public static SolutionDataManager ins() {
        return sInstance;
    }

    public void store(String token, String userId, String userName) {
        SPUtils.putString(SolutionConstants.SP_KEY_TOKEN, token);
        SPUtils.putString(SolutionConstants.SP_KEY_USER_ID, userId);
        SPUtils.putString(SolutionConstants.SP_KEY_USER_NAME, userName);

        SolutionDemoEventManager.post(new RefreshUserNameEvent(userName, true));
    }

    public void setUserId(String userId) {
        SPUtils.putString(SolutionConstants.SP_KEY_USER_ID, userId);
    }

    public String getUserId() {
        return SPUtils.getString(SolutionConstants.SP_KEY_USER_ID, "");
    }

    public void setUserName(String userName) {
        SPUtils.putString(SolutionConstants.SP_KEY_USER_NAME, userName);
    }

    public String getUserName() {
        return SPUtils.getString(SolutionConstants.SP_KEY_USER_NAME, "");
    }

    public void setToken(String token) {
        SPUtils.putString(SolutionConstants.SP_KEY_TOKEN, token);
    }

    public String getToken() {
        return SPUtils.getString(SolutionConstants.SP_KEY_TOKEN, "");
    }

    public String getDeviceId() {
        String did = SPUtils.getString(SP_KEY_DEVICE_ID, "");
        if (TextUtils.isEmpty(did)) {
            synchronized (sDidLock) {
                did = SPUtils.getString(SP_KEY_DEVICE_ID, "");
                if (TextUtils.isEmpty(did)) {
                    String uuid = UUID.randomUUID().toString();
                    int deviceId = Math.abs(uuid.hashCode());
                    did = String.valueOf(deviceId);
                    SPUtils.putString(SP_KEY_DEVICE_ID, did);
                }
            }
        }
        return did;
    }

    public void logout() {
        setUserId("");
        setUserName("");
        setToken("");
        SolutionDemoEventManager.post(new AppTokenExpiredEvent());
    }
}
