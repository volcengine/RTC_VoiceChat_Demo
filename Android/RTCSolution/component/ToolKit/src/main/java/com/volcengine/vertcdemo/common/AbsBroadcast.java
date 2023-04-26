// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.common;

import com.volcengine.vertcdemo.core.net.IBroadcastListener;

public class AbsBroadcast<T> implements IBroadcastListener {

    private final String mEvent;
    private final Class<T> mClz;
    private final On<T> mOn;

    public AbsBroadcast(String event, Class<T> clz, On<T> on) {
        mEvent = event;
        mClz = clz;
        mOn = on;
    }

    public String getEvent() {
        return mEvent;
    }

    @Override
    public final void onData(String data) {
        onListener(GsonUtils.gson().fromJson(data, mClz));
    }

    public void onListener(T t) {
        if (mOn != null) {
            mOn.on(t);
        }
    }

    public interface On<T> {
        void on(T t);
    }
}
