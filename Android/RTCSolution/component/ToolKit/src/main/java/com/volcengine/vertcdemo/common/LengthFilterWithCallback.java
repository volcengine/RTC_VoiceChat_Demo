// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.common;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

public class LengthFilterWithCallback implements InputFilter {

    private final int mMax;
    private final OverflowCallback mOverflowCallback;

    public LengthFilterWithCallback(int max, OverflowCallback overflowCallback) {
        mMax = max;
        mOverflowCallback = overflowCallback;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                               int dstart, int dend) {
        if (TextUtils.isEmpty(source)) {
            mOverflowCallback.isOverflow(false);
            return "";
        }
        int keep = mMax - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            mOverflowCallback.isOverflow(true);
            return "";
        } else if (keep >= end - start) {
            mOverflowCallback.isOverflow(false);
            return null; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    mOverflowCallback.isOverflow(true);
                    return "";
                }
            }
            mOverflowCallback.isOverflow(true);
            return source.subSequence(start, keep);
        }
    }

    public interface OverflowCallback {
        void isOverflow(boolean overflow);
    }
}
