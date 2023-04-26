// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#ifndef JoinRTSConfig_h
#define JoinRTSConfig_h

/**
 * @brief 使用 RTS 作为业务服务器时需要。上线正式服务时，需要在自己服务端部署。
 */
#define HeadUrl @""

/**
 * @brief 每个应用的唯一标识符，由 RTC 控制台申请获得，不同的 AppId 生成的实例在 RTC 中进行音视频通话完全独立，无法互通。
 * https://console.volcengine.com/rtc/listRTC
 */
#define APPID @""

/**
 * @brief 使用 RTC 时需要，AppKey用来生产rtc token及鉴权rtc token。
 * https://console.volcengine.com/rtc/listRTC
 */
#define APPKey @""

/**
 * @brief 使用 RTS 时需要，用于服务端调用 RTS openapi 时鉴权使用。
 用来确认服务端有这个APPID的所有权
 * https://console.volcengine.com/iam/keymanage/
 */
#define AccessKeyID @""

/**
 * @brief 使用 RTS 时需要，用于服务端调用 RTS openapi 时鉴权使用。用来确认服务端有这个APPID的所有权
 * https://console.volcengine.com/iam/keymanage/
 */
#define SecretAccessKey @""



#endif /* JoinRTSConfig_h */
