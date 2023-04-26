// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.protocol;

import android.content.Context;
import android.content.DialogInterface;

import com.ss.bytertc.engine.RTCVideo;

public interface IEffect {

    void initWithRTCVideo(RTCVideo rtcVideo);

    void showEffectDialog(Context context, DialogInterface.OnDismissListener dismissListener);

    void resume();

    void reset();
}
