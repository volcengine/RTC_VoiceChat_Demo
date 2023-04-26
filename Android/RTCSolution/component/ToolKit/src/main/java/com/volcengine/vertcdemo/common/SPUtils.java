// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
    private static volatile SharedPreferences sPrefs;

    private SPUtils() {
    }

    private static SharedPreferences getPrefs() {
        if (sPrefs == null) {
            throw new IllegalStateException("Please initSP at first!!!");
        } else {
            return sPrefs;
        }
    }

    public static void initSP(Context context, String prefsName) {
        sPrefs = context.getSharedPreferences(prefsName, 0);
    }

    public static boolean contains(String key) {
        return getPrefs().contains(key);
    }

    public static String getString(String key, String defValue) {
        return getPrefs().getString(key, defValue);
    }

    public static void putString(String key, String value) {
        getPrefs().edit().putString(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        return getPrefs().getInt(key, defValue);
    }

    public static void putInt(String key, int value) {
        getPrefs().edit().putInt(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getPrefs().getBoolean(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        getPrefs().edit().putBoolean(key, value).apply();
    }

    public static void putObject(String key, Object obj) {
        String str = obj == null ? null : GsonUtils.gson().toJson(obj);
        putString(key, str);
    }

    public static <T> T getObject(String key, Class<T> clazz, Object defaultObj) {
        String str = getString(key, "");
        T ret = GsonUtils.gson().fromJson(str, clazz);
        //noinspection unchecked
        return ret == null ? (T) defaultObj : ret;
    }
}
