// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.Keep;

import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.login.LoginActivity;
import com.volcengine.vertcdemo.protocol.ILogin;
import com.volcengine.vertcdemo.utils.CloseAccountManager;

@SuppressWarnings("unused")
@Keep
public class ILoginImpl implements ILogin {

    @Override
    public void showLoginView(Context context) {
        if (context == null) {
            return;
        }
        String token = SolutionDataManager.ins().getToken();
        if (TextUtils.isEmpty(token)) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }
    }

    @Override
    public void closeAccount(IAction<Boolean> action) {
        CloseAccountManager.delete(action);
    }
}
