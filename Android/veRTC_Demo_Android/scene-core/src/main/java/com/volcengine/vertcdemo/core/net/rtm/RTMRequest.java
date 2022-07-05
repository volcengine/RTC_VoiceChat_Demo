package com.volcengine.vertcdemo.core.net.rtm;

import com.volcengine.vertcdemo.core.net.IRequestCallback;

/**
 * RTM 模拟http请求的封装
 *
 * @param <T>
 */
public class RTMRequest<T extends RTMBizResponse> {
    /**
     * 请求的接口名
     */
    public String eventName;
    /**
     * 请求的回调
     */
    public IRequestCallback<T> callback;
    /**
     * 业务服务器返回的数据类型
     */
    public Class<T> resultClass;

    public RTMRequest(String eventName, IRequestCallback<T> callback, Class<T> resultClass) {
        this.eventName = eventName;
        this.callback = callback;
        this.resultClass = resultClass;
    }
}
