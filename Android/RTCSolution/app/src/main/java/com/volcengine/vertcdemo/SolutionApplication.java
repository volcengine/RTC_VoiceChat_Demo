// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo;

import android.app.Application;

import com.volcengine.vertcdemo.common.SPUtils;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.net.http.AppNetworkStatusUtil;
import com.volcengine.vertcdemo.utils.AppUtil;

public class SolutionApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SPUtils.initSP(this, "solution_application_sp");
        AppUtil.initApp(this);
        new CrashHandler(this);

        AppNetworkStatusUtil.registerNetworkCallback(this);
    }
}
