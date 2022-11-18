package com.volcengine.vertcdemo.core.net;

import com.ss.video.rtc.demo.basic_module.utils.GsonUtils;

public interface IBroadcastListener<T> {

    String getEvent();

    Class<T> getDataClass();

    void onListener(T t);

    default void onData(String data) {
        Class<T> dataClass = getDataClass();
        onListener(GsonUtils.gson().fromJson(data, dataClass));
    }
}
