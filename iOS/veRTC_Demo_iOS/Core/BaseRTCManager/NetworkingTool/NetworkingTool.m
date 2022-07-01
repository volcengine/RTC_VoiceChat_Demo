//
//  NetworkingTool.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/17.
//  Copyright © 2021 bytedance. All rights reserved.
//

#import <CommonCrypto/CommonCrypto.h>
#import "NetworkingTool.h"

@implementation NetworkingTool

+ (NSString *)messageFromResponseCode:(RTMStatusCode)code {
    NSString *message = @"";
    switch (code) {
        case RTMStatusCodeBadServerResponse:
            message = @"服务器数据异常";
            break;
        case RTMStatusCodeSendMessageFaild:
            message = @"服务请求失败";
            break;
        case RTMStatusCodeInvalidArgument:
            message = @"请求数据无法正确解析";
            break;
        case RTMStatusCodeUserNotFound:
            message = @"用户不存在";
            break;
        case RTMStatusCodeOverRoomLimit:
            message = @"房间人数超过限制";
            break;
        case RTMStatusCodeNotAuthorized:
            message = @"没有操作权限";
            break;
        case RTMStatusCodeDisconnectTimeout:
            message = @"断线时间过长，无法重连";
            break;
        case RTMStatusCodeRoomDisbanded:
            message = @"房间已经解散";
            break;
        case RTMStatusCodeSensitiveWords:
            message = @"输入内容包含敏感词，请重新输入";
            break;
        case RTMStatusCodeVerificationCodeExpired:
            message = @"验证码过期，请重新发送验证码";
            break;
        case RTMStatusCodeInvalidVerificationCode:
            message = @"验证码不正确，请重新发送验证码";
            break;
        case RTMStatusCodeTokenExpired:
            message = @"登录已经过期，请重新登录";
            break;
        case RTMStatusCodeInternalServerError:
            message = @"系统繁忙，请稍后重试";
            break;
        case RTMStatusCodeTransferUserOnMicExceedLimit:
            message = @"当前麦位已满";
            break;
        case RTMStatusCodeTransferHostFailed:
            message = @"移交主持人失败";
            break;
        case RTMStatusCodeBuildTokenFaild:
            message = @"服务端Token生成失败，请重试";
            break;
        case RTMStatusCodeReachLinkmicUserCount:
            message = @"连麦人数到达上限";
            break;
        case RTMStatusCodeLinkerNotExist:
            message = @"连麦已过期";
            break;
        case RTMStatusCodeRoomLinkmicSceneConflict:
            message = @"主播正在连线中";
            break;
        case RTMStatusCodeAudienceApplyOthersHost:
            message = @"主播正在发起双主播连线";
            break;
        case RTMStatusCodeHostLinkOtherAudience:
            message = @"与观众连线中，无法发起主播连线";
            break;
        case RTMStatusCodeHostLinkOtherHost:
            message = @"主播连线中，无法发起主播连线";
            break;
        case RTMStatusHostInviteOtherHost:
            message = @"正在等待被邀主播的应答";
            break;
        default:
            break;
    }
    return message;
}

+ (NSString *)getWisd {
    NSString *timeStr = [NSString stringWithFormat:@"%.0f", ([[NSDate date] timeIntervalSince1970] * 1000)];
    NSString *arcStr = [NSString stringWithFormat:@"%ld", (long)arc4random()%10000];
    return [NSString stringWithFormat:@"%@%@", timeStr, arcStr];
}

+ (NSString *)getDeviceId {
    NSString *deviceId = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceId_key"];
    if (!deviceId || ![deviceId isKindOfClass:[NSString class]] || deviceId.length <= 0) {
        NSString *wisd = [self getWisd];
        deviceId = [self MD5ForLower16Bate:wisd];
        [[NSUserDefaults standardUserDefaults] setValue:deviceId forKey:@"deviceId_key"];
        [[NSUserDefaults standardUserDefaults] synchronize];
    }
    return deviceId;
}

+ (NSString *)MD5ForLower16Bate:(NSString *)str {
    NSString *md5Str = [self MD5ForLower32Bate:str];
    NSString *string;
    if (md5Str.length >= 24) {
        string = [md5Str substringWithRange:NSMakeRange(8, 16)];
    }
    return string;
}

+ (NSString *)MD5ForLower32Bate:(NSString *)str {
    const char *input = [str UTF8String];
    unsigned char result[CC_MD5_DIGEST_LENGTH];
    CC_MD5(input, (CC_LONG)strlen(input), result);
    NSMutableString *digest = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH * 2];
    for (NSInteger i = 0; i < CC_MD5_DIGEST_LENGTH; i++) {
        [digest appendFormat:@"%02x", result[i]];
    }
    return digest;
}

+ (NSDictionary *)decodeJsonMessage:(NSString *)message {
    NSData *jsonData = [message dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary *dic = nil;
    if (jsonData) {
        dic = [NSJSONSerialization JSONObjectWithData:jsonData options:kNilOptions error:NULL];
        if (![dic isKindOfClass:[NSDictionary class]]) dic = nil;
    }
    return dic;
}

@end
