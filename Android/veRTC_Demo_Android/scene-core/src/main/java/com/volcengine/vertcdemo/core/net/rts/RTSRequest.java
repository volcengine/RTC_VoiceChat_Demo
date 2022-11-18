package com.volcengine.vertcdemo.core.net.rts;

import androidx.annotation.Nullable;

import com.ss.video.rtc.demo.basic_module.utils.GsonUtils;
import com.volcengine.vertcdemo.core.net.IRequestCallback;

/**
 * RTM 模拟http请求的封装
 *
 * @param <T>
 */
public final class RTSRequest<T extends RTSBizResponse> {
    /**
     * 请求的接口名
     */
    public String eventName;
    /**
     * 请求的回调
     */
    @Nullable
    public final IRequestCallback<T> callback;

    /**
     * 业务服务器返回的数据类型
     */
    public Class<T> resultClass;

    public RTSRequest(String eventName, @Nullable IRequestCallback<T> callback, Class<T> resultClass) {
        this.eventName = eventName;
        this.callback = callback;
        this.resultClass = resultClass;
    }

    public void notifySuccess(@Nullable String data) {
        if (data == null || callback == null) {
            return;
        }
        if (resultClass == null) {
            callback.onSuccess(null);
            return;
        }
        T result = GsonUtils.gson().fromJson(data, resultClass);
        callback.onSuccess(result);
    }

    public void notifyError(int errorCode, @Nullable String message) {
        if (callback != null) {
            callback.onError(errorCode, message);
        }
    }
}
