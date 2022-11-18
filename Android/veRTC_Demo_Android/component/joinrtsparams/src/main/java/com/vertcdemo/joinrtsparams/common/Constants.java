package com.vertcdemo.joinrtsparams.common;

import com.vertcdemo.joinrtsparams.BuildConfig;

public class Constants {

    /**
     * RTC 需要
     * 每个应用的唯一标识符，由 RTC 控制台申请获得，不同的 AppId 生成的实例在 RTC 中进行音视频通话完全独立，无法互通。
     * https://console.volcengine.com/rtc/listRTC
     */
    public static final String APP_ID = BuildConfig.APP_ID;

    /**
     * 使用 RTC 时需要，AppKey是用来生产rtc token及鉴权rtc token使用的。
     * https://console.volcengine.com/rtc/listRTC
     */
    public static final String APP_KEY = BuildConfig.APP_KEY;

    /**
     * 使用 RTS 时需要，用于服务端调用 RTS openapi 时鉴权使用。
     * 用来确认服务端有这个APPID的所有权 https://console.volcengine.com/iam/keymanage/
     */
    public static final String VOLC_AK = BuildConfig.VOLC_AK;

    /**
     * 使用 RTS 时需要，用于服务端调用 RTS openapi 时鉴权使用。
     * 用来确认服务端有这个APPID的所有权 https://console.volcengine.com/iam/keymanage/
     */
    public static final String VOLC_SK = BuildConfig.VOLC_SK;
}
