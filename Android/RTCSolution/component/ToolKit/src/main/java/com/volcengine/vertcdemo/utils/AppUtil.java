// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

public class AppUtil {

    private static Context mAppCxt;

    public static void initApp(Context context) {
        mAppCxt = context.getApplicationContext();
    }

    public static Context getApplicationContext() {
        if (mAppCxt == null) {
            throw new IllegalStateException("Please init app at first!");
        } else {
            return mAppCxt;
        }
    }

    public static String getAppVersionName() {
        if (mAppCxt == null) {
            return "";
        }
        try {
            PackageInfo pInfo = mAppCxt.getPackageManager()
                    .getPackageInfo(mAppCxt.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }
}
