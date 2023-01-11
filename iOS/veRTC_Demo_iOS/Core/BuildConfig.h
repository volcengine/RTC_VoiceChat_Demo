//
//  BuildConfig.h
//  Pods
//
//  Created by on 2022/6/13.
//

#ifndef BuildConfig_h
#define BuildConfig_h

/*
 使用 RTS 作为业务服务器时需要。
 上线正式服务时，需要在自己服务端部署。
 */
#define HeadUrl @""

/*
 RTC 需要
 每个应用的唯一标识符，由 RTC 控制台申请获得，不同的 AppId 生成的实例在 RTC 中进行音视频通话完全独立，无法互通。
 https://console.volcengine.com/rtc/listRTC
 */
#define APPID @""

/*
 使用 RTC 时需要，AppKey用来生产rtc token及鉴权rtc token。
 https://console.volcengine.com/rtc/listRTC
 */
#define APPKey @""

/*
 使用 RTS 时需要，用于服务端调用 RTS openapi 时鉴权使用。
 用来确认服务端有这个APPID的所有权
 https://console.volcengine.com/iam/keymanage/
 */
#define AccessKeyID @""

/*
 使用 RTS 时需要，用于服务端调用 RTS openapi 时鉴权使用。
 用来确认服务端有这个APPID的所有权
 https://console.volcengine.com/iam/keymanage/
 */
#define SecretAccessKey @""

#endif /* BuildConfig_h */
