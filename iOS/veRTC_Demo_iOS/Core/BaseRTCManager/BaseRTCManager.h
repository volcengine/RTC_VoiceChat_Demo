//
//  BaseRTCManager.h
//  veRTC_Demo
//
//  Created by on 2021/12/16.
//
//

#import <Foundation/Foundation.h>
#import <VolcEngineRTC/objc/ByteRTCVideo.h>
#import <VolcEngineRTC/objc/ByteRTCRoom.h>
#import <YYModel/YYModel.h>
#import "RTMRequestModel.h"
#import "RTMACKModel.h"
#import "RTMNoticeModel.h"
#import "LocalUserComponent.h"
#import "PublicParameterComponent.h"
#import "NetworkingTool.h"

NS_ASSUME_NONNULL_BEGIN

typedef void (^RTCRoomMessageBlock)(RTMNoticeModel *noticeModel);

@interface BaseRTCManager : NSObject <ByteRTCVideoDelegate, ByteRTCRoomDelegate>

@property (nonatomic, strong, nullable) ByteRTCVideo *rtcEngineKit;

/// 若 RTS 房间和 RTC 房间生命周期相同，则共用一个 room 对象
/// If the RTS room and the RTC room have the same life cycle, share a room object
@property (nonatomic, strong, nullable) ByteRTCRoom *rtcRoom;

/// 加入 RTC 房间 block 回调
/// Add RTC room block callback
@property (nonatomic, copy, nullable) void (^rtcJoinRoomBlock)(NSString *roomId,
                                                               NSInteger errorCode,
                                                               NSInteger joinType);

/// 相同用户进房，被踢下线
/// The same user entered the room and was kicked off the line
@property (nonatomic, copy, nullable) void (^rtcSameUserJoinRoomBlock)(NSString *roomId, NSInteger errorCode);

/// 业务标识参数
/// Business ID parameter
@property (nonatomic, copy, readonly) NSString *businessId;

/// 开启 RTS 连接
/// Open RTS connection
- (void)connect:(NSString *)appID
       RTSToken:(NSString *)RTMToken
      serverUrl:(NSString *)serverUrl
      serverSig:(NSString *)serverSig
            bid:(NSString *)bid
          block:(void (^)(BOOL result))block;

/// 关闭连接
/// close the connection
- (void)disconnect;

/// 接口请求
/// interface request
- (void)emitWithAck:(NSString *)event
               with:(NSDictionary *)item
              block:(__nullable RTCSendServerMessageBlock)block;
           
/// 注册广播监听
/// Register broadcast listener
- (void)onSceneListener:(NSString *)key
                  block:(RTCRoomMessageBlock)block;

/// 移除广播监听
/// Remove broadcast listener
- (void)offSceneListener;

#pragma mark - Multi Room

/// 加入 RTS 房间, 若 RTS 房间和 RTC 房间生命周期不同，需要单独加入 RTS 房间。
/// Join the RTS room, if the life cycle of the RTS room and the RTC room is different, you need to join the RTS room separately.
- (void)joinMultiRTSRoomByToken:(NSString *)token
                         roomID:(NSString *)roomID
                         userID:(NSString *)userID;

/// 离开 RTS 房间
/// Leave the RTS room
- (void)leaveMultiRTSRoom;

/// 获取 RTC SDK 版本号
/// Get Sdk Version
+ (NSString *_Nullable)getSdkVersion;

#pragma mark - Config

/// 父类每次初始化 rtcEngineKit 时会调用，子类直接覆写实现。
/// The parent class will call each time rtcEngineKit is initialized, and the subclass directly overrides the implementation.
- (void)configeRTCEngine;

@end

NS_ASSUME_NONNULL_END
