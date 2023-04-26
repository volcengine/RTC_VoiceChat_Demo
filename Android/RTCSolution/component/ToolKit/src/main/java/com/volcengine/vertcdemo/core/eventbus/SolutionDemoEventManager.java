// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.core.eventbus;

import org.greenrobot.eventbus.EventBus;

public class SolutionDemoEventManager {

    private static final EventBus sInstance = EventBus.getDefault();

    public static void post(Object object) {
        sInstance.post(object);
    }

    public static void register(Object object) {
        if (!sInstance.isRegistered(object)) {
            sInstance.register(object);
        }
    }

    public static void unregister(Object object) {
        sInstance.unregister(object);
    }
}
