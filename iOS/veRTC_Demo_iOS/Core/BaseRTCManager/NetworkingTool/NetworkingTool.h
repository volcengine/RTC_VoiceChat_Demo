//
//  NetworkingTool.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/17.
//  Copyright © 2021 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, RTMStatusCode) {
    // 服务器返回数据异常
    RTMStatusCodeBadServerResponse = -1011,
    // 消息发送失败
    RTMStatusCodeSendMessageFaild = -101,
    // 未知错误
    RTMStatusCodeUnknown = -1,
    // 正确返回
    RTMStatusCodeSuccess = 200,
    
    // 请求数据无法正确解析
    RTMStatusCodeInvalidArgument = 400,
    // 用户已经离开房间/用户不存在
    RTMStatusCodeUserNotFound = 404,
    // 房间人数超过限制
    RTMStatusCodeOverRoomLimit = 406,
    // 没有操作权限
    RTMStatusCodeNotAuthorized = 416,
    // 断线时间过长，无法重连
    RTMStatusCodeDisconnectTimeout = 418,
    // 用户已不活跃
    RTMStatusCodeUserIsInactive = 419,
    // 房间已经解散/房间不存在
    RTMStatusCodeRoomDisbanded = 422,
    // 输入内容包含敏感词
    RTMStatusCodeSensitiveWords = 430,
    // 验证码过期
    RTMStatusCodeVerificationCodeExpired = 440,
    // 验证码无效
    RTMStatusCodeInvalidVerificationCode = 441,
    // Token过期
    RTMStatusCodeTokenExpired = 450,
    // 连麦人数到达上限
    RTMStatusCodeReachLinkmicUserCount = 472,
    
    // 系统繁忙
    RTMStatusCodeInternalServerError = 500,
    // 移交主持人失败
    RTMStatusCodeTransferHostFailed = 504,
    // 麦克风上的用户超出限制
    RTMStatusCodeTransferUserOnMicExceedLimit = 506,
    
    // 主播连麦接口没传linkerID
    RTMStatusCodeLinkerParamError = 610,
    // linker不存在，使用过期的linker_id
    RTMStatusCodeLinkerNotExist = 611,
    // 没有权限，非房主调用房主才能调用的接口
    RTMStatusCodeUserRoleNotAuthorized = 620,
    // 正在邀请用户中
    RTMStatusCodeUserIsInviting = 622,
    // 场景冲突(观众连麦时调用主播连麦接口，主播连麦时调用观众连麦接）
    RTMStatusCodeRoomLinkmicSceneConflict = 630,
    
    // 主播正在发起双主播连线
    RTMStatusCodeAudienceApplyOthersHost = 632,
    
    // 与观众连线中，无法发起主播连线
    RTMStatusCodeHostLinkOtherAudience = 643,
    
    // 与主播连线中，无法发起主播连线
    RTMStatusCodeHostLinkOtherHost = 644,
    
    // 正在等待被邀主播的应答
    RTMStatusHostInviteOtherHost = 645,
    
    // Token生成错误
    RTMStatusCodeBuildTokenFaild = 702,
};

@interface NetworkingTool : NSObject

+ (NSString *)messageFromResponseCode:(RTMStatusCode)code;

+ (NSString *)getWisd;

+ (NSString *)getDeviceId;

+ (NSString *)MD5ForLower16Bate:(NSString *)str;

+ (NSString *)MD5ForLower32Bate:(NSString *)str;

+ (nullable NSDictionary *)decodeJsonMessage:(NSString *)message;

@end

NS_ASSUME_NONNULL_END
