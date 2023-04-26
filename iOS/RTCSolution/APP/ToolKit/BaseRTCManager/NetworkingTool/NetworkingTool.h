// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, RTSStatusCode) {
    // 服务器返回数据异常
    RTSStatusCodeBadServerResponse = -1011,
    // 消息发送失败
    RTSStatusCodeSendMessageFaild = -101,
    // 未知错误
    RTSStatusCodeUnknown = -1,
    // 正确返回
    RTSStatusCodeSuccess = 200,
    
    // 请求数据无法正确解析
    RTSStatusCodeInvalidArgument = 400,
    // 用户在房间中
    RTSStatusCode402 = 402,
    // 用户已经离开房间/用户不存在
    RTSStatusCodeUserNotFound = 404,
    // 房间人数超过限制
    RTSStatusCodeOverRoomLimit = 406,
    // 没有操作权限
    RTSStatusCodeNotAuthorized = 416,
    // 断线时间过长，无法重连
    RTSStatusCodeDisconnectTimeout = 418,
    // 用户已不活跃
    RTSStatusCodeUserIsInactive = 419,
    // 房间已经解散/房间不存在
    RTSStatusCodeRoomDisbanded = 422,
    // 输入内容包含敏感词
    RTSStatusCodeSensitiveWords = 430,
    // 验证码过期
    RTSStatusCodeVerificationCodeExpired = 440,
    // 验证码无效
    RTSStatusCodeInvalidVerificationCode = 441,
    // Token过期
    RTSStatusCodeTokenExpired = 450,
    // 连麦人数到达上限
    RTSStatusCodeReachLinkmicUserCount = 472,
    
    // 系统繁忙
    RTSStatusCodeInternalServerError = 500,
    // 移交主持人失败
    RTSStatusCodeTransferHostFailed = 504,
    // 麦克风上的用户超出限制
    RTSStatusCodeTransferUserOnMicExceedLimit = 506,
    
    // 主播连麦接口没传linkerID
    RTSStatusCodeLinkerParamError = 610,
    // linker不存在，使用过期的linker_id
    RTSStatusCodeLinkerNotExist = 611,
    // 没有权限，非房主调用房主才能调用的接口
    RTSStatusCodeUserRoleNotAuthorized = 620,
    // 正在邀请用户中
    RTSStatusCodeUserIsInviting = 622,
    // 正在邀请用户中
    RTSStatusCodeUserIsNewInviting = 481,
    // 场景冲突(观众连麦时调用主播连麦接口，主播连麦时调用观众连麦接）
    RTSStatusCodeRoomLinkmicSceneConflict = 630,
    
    // 主播正在发起双主播连线
    RTSStatusCodeAudienceApplyOthersHost = 632,
    
    // 与观众连线中，无法发起主播连线
    RTSStatusCodeHostLinkOtherAudience = 643,
    
    // 与主播连线中，无法发起主播连线
    RTSStatusCodeHostLinkOtherHost = 644,
    
    // 正在等待被邀主播的应答
    RTSStatusHostInviteOtherHost = 645,
    
    // Token生成错误
    RTSStatusCodeBuildTokenFaild = 702,
    
    // 获取appInfo错误
    RTSStatusCodeAPPInfoFaild = 800,
    // redis中不存在appInfo
    RTSStatusCodeAPPInfoExistFaild = 801,
    // 检查流量appID错误
    RTSStatusCodeTrafficAPPIDFaild = 802,
    // 查看流量上限
    RTSStatusCodeTrafficFaild = 803,
    // 点播配置错误
    RTSStatusCodeVodFaild = 804,
    // 一起看抖音配置错误
    RTSStatusCodeTWFaild = 805,
    // bid配置错误
    RTSStatusCodeBIDFaild = 806,
};

@interface NetworkingTool : NSObject

+ (NSString *)messageFromResponseCode:(RTSStatusCode)code;

+ (NSString *)getWisd;

+ (NSString *)getDeviceId;

+ (NSString *)MD5ForLower16Bate:(NSString *)str;

+ (NSString *)MD5ForLower32Bate:(NSString *)str;

+ (nullable NSDictionary *)decodeJsonMessage:(NSString *)message;

@end

NS_ASSUME_NONNULL_END
