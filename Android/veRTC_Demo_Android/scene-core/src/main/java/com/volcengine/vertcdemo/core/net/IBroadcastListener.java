package com.volcengine.vertcdemo.core.net;

public interface IBroadcastListener<T> {

    String getEvent();

    Class<T> getDataClass();

    void onListener(T t);
}
