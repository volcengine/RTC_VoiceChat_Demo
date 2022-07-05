package com.volcengine.vertcdemo.common;

import com.volcengine.vertcdemo.core.net.IBroadcastListener;

public class AbsBroadcast<T> implements IBroadcastListener<T> {

    private final String mEvent;
    private final Class<T> mClz;
    private final On<T> mOn;

    public AbsBroadcast(String event, Class<T> clz, On<T> on) {
        mEvent = event;
        mClz = clz;
        mOn = on;
    }

    @Override
    public String getEvent() {
        return mEvent;
    }

    @Override
    public Class<T> getDataClass() {
        return mClz;
    }

    @Override
    public void onListener(T t) {
        if (mOn != null) {
            mOn.on(t);
        }
    }

    public interface On<V> {
        void on(V t);
    }
}
