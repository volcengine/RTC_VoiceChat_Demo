// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {
    private static final Gson sGson = (new GsonBuilder()).serializeNulls().create();

    private GsonUtils() {
    }

    public static Gson gson() {
        return sGson;
    }

    public static <T> T fromObject(Object obj, Class<T> classOfT) {
        if (obj == null) {
            return null;
        } else {
            String str = gson().toJson(obj);
            return gson().fromJson(str, classOfT);
        }
    }
}
