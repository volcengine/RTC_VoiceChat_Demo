// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class IMEUtils {
    public static void closeIME(View v) {
        InputMethodManager mgr = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
        v.clearFocus();
    }

    public static void openIME(final EditText v) {
        boolean focus = v.requestFocus();
        if (v.hasFocus()) {
            v.post(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager mgr = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                }
            });
        }
    }
}
