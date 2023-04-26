// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.utils;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("unused")
@Keep
public class AgreementManager {
    public interface ResultCallback {
        void onResult(boolean result);
    }

    public static <T extends AppCompatActivity & ResultCallback> void check(@NonNull T activity) {
        activity.onResult(true);
    }
}
