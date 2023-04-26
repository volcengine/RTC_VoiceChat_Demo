// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>
#import <VolcEngineRTC/objc/ByteRTCVideo.h>
#import <VolcEngineRTC/objc/ByteRTCRoom.h>

#import <YYModel/YYModel.h>
#import "RTSRequestModel.h"
#import "RTSACKModel.h"
#import "RTSNoticeModel.h"
#import "LocalUserComponent.h"
#import "PublicParameterComponent.h"
#import "NetworkingTool.h"
#import "RTCJoinModel.h"

NS_ASSUME_NONNULL_BEGIN

typedef void (^RTCRoomMessageBlock)(RTSNoticeModel *noticeModel);

@interface BaseRTCManager : NSObject <ByteRTCVideoDelegate, ByteRTCRoomDelegate>
// RTC 引擎
@property (nonatomic, strong, nullable) ByteRTCVideo *rtcEngineKit;

/**
 * @brief 开启 RTS 连接
 * @param appID APPID，初始化 ByteRTCVideo 需要。
 * @param RTSToken RTS token，加入 RTS 房间需要。
 * @param serverUrl RTS 服务器地址，设置应用服务器参数需要。
 * @param serverSig RTS 签名，设置应用服务器参数需要。
 * @param bid business ID，可以根据不同 business ID 下发不同 RTC 配置。
 * @param block 开启连接结果回调
 */
- (void)connect:(NSString *)appID
       RTSToken:(NSString *)RTSToken
      serverUrl:(NSString *)serverUrl
      serverSig:(NSString *)serverSig
            bid:(NSString *)bid
          block:(void (^)(BOOL result))block;

/**
 * @brief 关闭 RTS 连接
 */
- (void)disconnect;

/**
 * @brief 发出 RTS 请求
 * @param event 请求事件KEY
 * @param item 请求参数
 * @param block 发出 RTS 请求结果回调
 */
- (void)emitWithAck:(NSString *)event
               with:(NSDictionary *)item
              block:(__nullable RTCSendServerMessageBlock)block;

/**
 * @brief 注册 RTS 监听
 * @param key 注册监听需要的key
 * @param block 当收到监听时回调
 */
- (void)onSceneListener:(NSString *)key
                  block:(RTCRoomMessageBlock)block;

/**
 * @brief 移除 RTS 监听
 */
- (void)offSceneListener;


#pragma mark - Config

/**
 * @brief 每次初始化 rtcEngineKit 时会调用，子类重写实现。
 */
- (void)configeRTCEngine;

/**
 * @brief 获取 RTC SDK 版本号
 */
+ (NSString *_Nullable)getSdkVersion;

@end

NS_ASSUME_NONNULL_END
