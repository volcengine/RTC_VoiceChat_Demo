// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.utils;

import androidx.annotation.Keep;

import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.core.SolutionDataManager;

@SuppressWarnings("unused")
@Keep
public class CloseAccountManager {

    public static void delete(IAction<Boolean> action) {
        SolutionDataManager.ins().logout();
        if (action != null) {
            action.act(true);
        }
    }
}
