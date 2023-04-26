// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "BaseRTCManager.h"
#import "LocalizatorBundle.h"

typedef NSString* RTSMessageType;
static RTSMessageType const RTSMessageTypeResponse = @"return";
static RTSMessageType const RTSMessageTypeNotice = @"inform";

@interface BaseRTCManager ()

@property (nonatomic, copy) void (^rtcLoginBlock)(BOOL result);
@property (nonatomic, copy) void (^rtcSetParamsBlock)(BOOL result);
@property (nonatomic, strong) NSMutableDictionary *listenerDic;
@property (nonatomic, strong) NSMutableDictionary *senderDic;

@end

@implementation BaseRTCManager

#pragma mark - Publish Action

- (void)connect:(NSString *)appID
       RTSToken:(NSString *)RTSToken
      serverUrl:(NSString *)serverUrl
      serverSig:(NSString *)serverSig
            bid:(NSString *)bid
          block:(void (^)(BOOL result))block {
    NSString *uid = [LocalUserComponent userModel].uid;
    if (IsEmptyStr(uid)) {
        if (block) {
            block(NO);
        }
        return;
    }
    if (self.rtcEngineKit) {
        [ByteRTCVideo destroyRTCVideo];
        self.rtcEngineKit = nil;
    }
    // 创建 RTC 引擎
    self.rtcEngineKit = [ByteRTCVideo createRTCVideo:appID delegate:self parameters:@{}];
    [self configeRTCEngine];
    // 设置 Business ID
    [self.rtcEngineKit setBusinessId:bid];
    // 登录 RTS
    [self.rtcEngineKit login:RTSToken uid:uid];
    // 登录 RTS 结果回调
    __weak __typeof(self) wself = self;
    self.rtcLoginBlock = ^(BOOL result) {
        wself.rtcLoginBlock = nil;
        if (result) {
            // 设置应用服务器参数
            [wself.rtcEngineKit setServerParams:serverSig url:serverUrl];
        } else {
            wself.rtcSetParamsBlock = nil;
            dispatch_queue_async_safe(dispatch_get_main_queue(), ^{
                if (block) {
                    block(result);
                }
            });
        }
    };
    // 设置应用服务器参数结果回调
    self.rtcSetParamsBlock = ^(BOOL result) {
        wself.rtcSetParamsBlock = nil;
        dispatch_queue_async_safe(dispatch_get_main_queue(), ^{
            if (block) {
                block(result);
            }
        });
    };
}

- (void)disconnect {
    [self.rtcEngineKit logout];
    [ByteRTCVideo destroyRTCVideo];
    self.rtcEngineKit = nil;
    self.rtcLoginBlock = nil;
    self.rtcSetParamsBlock = nil;
}

- (void)emitWithAck:(NSString *)event
               with:(NSDictionary *)item
              block:(RTCSendServerMessageBlock)block {
    if (IsEmptyStr(event)) {
        [self throwErrorAck:RTSStatusCodeInvalidArgument
                    message:@"Lack EventName"
                      block:block];
        return;
    }
    NSString *appId = @"";
    NSString *roomId = @"";
    if ([item isKindOfClass:[NSDictionary class]]) {
        appId = item[@"app_id"];
        roomId = item[@"room_id"];
        if (IsEmptyStr(appId)) {
            [self throwErrorAck:RTSStatusCodeInvalidArgument
                        message:@"Lack AppID"
                          block:block];
            return;
        }
    }
    NSString *wisd = [NetworkingTool getWisd];
    RTSRequestModel *requestModel = [[RTSRequestModel alloc] init];
    requestModel.eventName = event;
    requestModel.app_id = appId;
    requestModel.roomID = roomId;
    requestModel.userID = [LocalUserComponent userModel].uid;
    requestModel.requestID = [NetworkingTool MD5ForLower16Bate:wisd];
    requestModel.content = [item yy_modelToJSONString];
    requestModel.deviceID = [NetworkingTool getDeviceId];
    requestModel.requestBlock = block;
    NSString *json = [requestModel yy_modelToJSONString];
    // 客户端向应用服务器发送一条文本消息（P2Server）
    requestModel.msgid = (NSInteger)[self.rtcEngineKit sendServerMessage:json];
    
    NSString *key = requestModel.requestID;
    [self.senderDic setValue:requestModel forKey:key];
    [self addLog:@"sendServerMessage-" message:json];
}
           
- (void)onSceneListener:(NSString *)key
                  block:(RTCRoomMessageBlock)block {
    if (IsEmptyStr(key)) {
        return;
    }
    [self.listenerDic setValue:block forKey:key];
}

- (void)offSceneListener {
    [self.listenerDic removeAllObjects];
}

#pragma mark - Config

- (void)configeRTCEngine {
    // 需要子类重写
}

+ (NSString *_Nullable)getSdkVersion {
    return [ByteRTCVideo getSDKVersion];
}

#pragma mark - ByteRTCVideoDelegate

// 收到 RTS 登录结果
- (void)rtcEngine:(ByteRTCVideo *)engine onLoginResult:(NSString *)uid errorCode:(ByteRTCLoginErrorCode)errorCode elapsed:(NSInteger)elapsed {
    if (self.rtcLoginBlock) {
        self.rtcLoginBlock((errorCode == ByteRTCLoginErrorCodeSuccess) ? YES : NO);
    }
}

// 收到业务服务器参数设置结果
- (void)rtcEngine:(ByteRTCVideo *)engine onServerParamsSetResult:(NSInteger)errorCode {
    if (self.rtcSetParamsBlock) {
        self.rtcSetParamsBlock((errorCode == RTSStatusCodeSuccess) ? YES : NO);
    }
}

// 收到 RTC/RTS 加入房间结果
- (void)rtcEngine:(ByteRTCVideo *)engine onServerMessageSendResult:(int64_t)msgid error:(ByteRTCUserMessageSendResult)error message:(NSData *)message {
    if (error == ByteRTCUserMessageSendResultSuccess) {
        // 发送成功，等待业务回调信息
        // Successfully sent, waiting for business callback information
    } else {
        // 发送失败
        // Failed to send
        NSString *key = @"";
        for (RTSRequestModel *model in self.senderDic.allValues) {
            if (model.msgid == msgid) {
                key = model.requestID;
                [self throwErrorAck:RTSStatusCodeSendMessageFaild
                            message:[NetworkingTool messageFromResponseCode:RTSStatusCodeSendMessageFaild]
                              block:model.requestBlock];
                NSLog(@"[%@]-收到消息发送结果 %@ msgid %lld request_id %@ ErrorCode %ld", [self class], model.eventName, msgid, key, (long)error);
                break;
            }
        }
        if (NOEmptyStr(key)) {
            [self.senderDic removeObjectForKey:key];
        }
        
        if (error == ByteRTCUserMessageSendResultNotLogin) {
            dispatch_queue_async_safe(dispatch_get_main_queue(), ^{
                [[NSNotificationCenter defaultCenter] postNotificationName:NotificationLoginExpired object:@"logout"];
            });
        }
    }
}
// 收到来自房间外消息时回调
- (void)rtcEngine:(ByteRTCVideo *)engine onUserMessageReceivedOutsideRoom:(NSString *)uid message:(NSString *)message {

    [self dispatchMessageFrom:uid message:message];
    [self addLog:@"onUserMessageReceivedOutsideRoom-" message:message];
}
// SDK 与信令服务器的连接状态改变回调，当连接状态改变时触发。
- (void)rtcEngine:(ByteRTCVideo *)engine connectionChangedToState:(ByteRTCConnectionState)state {
    if (state == ByteRTCConnectionStateDisconnected) {
        for (RTSRequestModel *requestModel in self.senderDic.allValues) {
            if (requestModel.requestBlock) {
                RTSACKModel *ackModel = [[RTSACKModel alloc] init];
                ackModel.code = 400;
                ackModel.message = LocalizedStringFromBundle(@"operation_failed_message", @"ToolKit");
                dispatch_async(dispatch_get_main_queue(), ^{
                    requestModel.requestBlock(ackModel);
                });
            }
        }
        [self.senderDic removeAllObjects];
    }
}

#pragma mark - ByteRTCRoomDelegate

// 收到 RTC/RTS 加入房间结果
- (void)rtcRoom:(ByteRTCRoom *)rtcRoom onRoomStateChanged:(NSString *)roomId
        withUid:(NSString *)uid
          state:(NSInteger)state
      extraInfo:(NSString *)extraInfo {
    dispatch_queue_async_safe(dispatch_get_main_queue(), ^{
        if (state == ByteRTCErrorCodeDuplicateLogin) {
            [[NSNotificationCenter defaultCenter] postNotificationName:NotificationLoginExpired object:@"logout"];
        }
    });
}
// 收到来自房间内消息时回调
- (void)rtcRoom:(ByteRTCRoom *)rtcRoom onRoomMessageReceived:(NSString *)uid message:(NSString *)message {
    [self dispatchMessageFrom:uid message:message];
    [self addLog:@"onRoomMessageReceived-" message:message];
}
// 收到来自房间内用户消息时回调
- (void)rtcRoom:(ByteRTCRoom *)rtcRoom onUserMessageReceived:(NSString *)uid message:(NSString *)message {
    [self dispatchMessageFrom:uid message:message];
    [self addLog:@"onUserMessageReceived-" message:message];
}

#pragma mark - Private Action

- (void)dispatchMessageFrom:(NSString *)uid message:(NSString *)message {
    NSDictionary *dic = [NetworkingTool decodeJsonMessage:message];
    if (!dic || !dic.count) {
        return;
    }
    NSString *messageType = dic[@"message_type"];
    if ([messageType isKindOfClass:[NSString class]] &&
        [messageType isEqualToString:RTSMessageTypeResponse]) {
        [self receivedResponseFrom:uid object:dic];
        return;
    }
    
    if ([messageType isKindOfClass:[NSString class]] &&
        [messageType isEqualToString:RTSMessageTypeNotice]) {
        [self receivedNoticeFrom:uid object:dic];
        return;
    }
}

- (void)receivedResponseFrom:(NSString *)uid object:(NSDictionary *)object {
    RTSACKModel *ackModel = [RTSACKModel modelWithMessageData:object];
    if (IsEmptyStr(ackModel.requestID)) {
        return;
    }
    NSString *key = ackModel.requestID;
    RTSRequestModel *model = self.senderDic[key];
    if (model && [model isKindOfClass:[RTSRequestModel class]]) {
        if (model.requestBlock) {
            dispatch_queue_async_safe(dispatch_get_main_queue(), ^{
                model.requestBlock(ackModel);
            });
        }
    }
    [self.senderDic removeObjectForKey:key];
}

- (void)receivedNoticeFrom:(NSString *)uid object:(NSDictionary *)object {
    RTSNoticeModel *noticeModel = [RTSNoticeModel yy_modelWithJSON:object];
    if (IsEmptyStr(noticeModel.eventName)) {
        return;
    }
    RTCRoomMessageBlock block = self.listenerDic[noticeModel.eventName];
    if (block) {
        dispatch_queue_async_safe(dispatch_get_main_queue(), ^{
            block(noticeModel);
        });
    }
}

- (void)throwErrorAck:(NSInteger)code message:(NSString *)message
                block:(__nullable RTCSendServerMessageBlock)block {
    if (!block) {
        return;
    }
    RTSACKModel *ackModel = [[RTSACKModel alloc] init];
    ackModel.code = code;
    ackModel.message = message;
    dispatch_queue_async_safe(dispatch_get_main_queue(), ^{
        block(ackModel);
    });
}

#pragma mark - Getter

- (NSMutableDictionary *)listenerDic {
    if (!_listenerDic) {
        _listenerDic = [[NSMutableDictionary alloc] init];
    }
    return _listenerDic;
}

- (NSMutableDictionary *)senderDic {
    if (!_senderDic) {
        _senderDic = [[NSMutableDictionary alloc] init];
    }
    return _senderDic;
}

#pragma mark - Tool

- (void)addLog:(NSString *)key message:(NSString *)message {
    NSLog(@"[%@]-%@ %@", [self class], key, [NetworkingTool decodeJsonMessage:message]);
}

@end
