// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <CommonCrypto/CommonCrypto.h>
#import "NetworkingTool.h"
#import "LocalizatorBundle.h"

@implementation NetworkingTool

+ (NSString *)messageFromResponseCode:(RTSStatusCode)code {
    NSString *message = @"";
    NSString *bundleName = @"ToolKit";
    switch (code) {
        case RTSStatusCodeBadServerResponse:
            message = LocalizedStringFromBundle(@"network_message_1011", bundleName);
            break;
        case RTSStatusCodeSendMessageFaild:
            message = LocalizedStringFromBundle(@"network_message_101", bundleName);
            break;
        case RTSStatusCodeInvalidArgument:
            message = LocalizedStringFromBundle(@"network_message_400", bundleName);
            break;
        case RTSStatusCodeUserNotFound:
            message = LocalizedStringFromBundle(@"network_message_404", bundleName);
            break;
        case RTSStatusCodeUserIsInactive:
            message = LocalizedStringFromBundle(@"network_message_419", bundleName);
            break;
        case RTSStatusCodeOverRoomLimit:
            message = LocalizedStringFromBundle(@"network_message_406", bundleName);
            break;
        case RTSStatusCodeNotAuthorized:
            message = LocalizedStringFromBundle(@"network_message_416", bundleName);
            break;
        case RTSStatusCodeDisconnectTimeout:
            message = LocalizedStringFromBundle(@"network_message_418", bundleName);
            break;
        case RTSStatusCodeRoomDisbanded:
            message = LocalizedStringFromBundle(@"network_message_422", bundleName);
            break;
        case RTSStatusCode402:
            message = LocalizedStringFromBundle(@"network_message_402", bundleName);
            break;
        case RTSStatusCodeSensitiveWords:
            message = LocalizedStringFromBundle(@"network_message_430", bundleName);
            break;
        case RTSStatusCodeVerificationCodeExpired:
            message = LocalizedStringFromBundle(@"network_message_440", bundleName);
            break;
        case RTSStatusCodeInvalidVerificationCode:
            message = LocalizedStringFromBundle(@"network_message_441", bundleName);
            break;
        case RTSStatusCodeTokenExpired:
            message = LocalizedStringFromBundle(@"network_message_450", bundleName);
            break;
        case RTSStatusCodeInternalServerError:
            message = LocalizedStringFromBundle(@"network_message_500", bundleName);
            break;
        case RTSStatusCodeTransferUserOnMicExceedLimit:
            message = LocalizedStringFromBundle(@"network_message_506", bundleName);
            break;
        case RTSStatusCodeTransferHostFailed:
            message = LocalizedStringFromBundle(@"network_message_504", bundleName);
            break;
        case RTSStatusCodeBuildTokenFaild:
            message = LocalizedStringFromBundle(@"network_message_702", bundleName);
            break;
        case RTSStatusCodeReachLinkmicUserCount:
            message = LocalizedStringFromBundle(@"network_message_472", bundleName);
            break;
        case RTSStatusCodeLinkerNotExist:
            message = LocalizedStringFromBundle(@"network_message_611", bundleName);
            break;
        case RTSStatusCodeRoomLinkmicSceneConflict:
            message = LocalizedStringFromBundle(@"network_message_622", bundleName);
            break;
        case RTSStatusCodeAudienceApplyOthersHost:
            message = LocalizedStringFromBundle(@"network_message_632", bundleName);
            break;
        case RTSStatusCodeHostLinkOtherAudience:
            message = LocalizedStringFromBundle(@"network_message_643", bundleName);
            break;
        case RTSStatusCodeHostLinkOtherHost:
            message = LocalizedStringFromBundle(@"network_message_644", bundleName);
            break;
        case RTSStatusHostInviteOtherHost:
            message = LocalizedStringFromBundle(@"network_message_645", bundleName);
            break;
        case RTSStatusCodeAPPInfoFaild:
        case RTSStatusCodeAPPInfoExistFaild:
            message = LocalizedStringFromBundle(@"network_message_801", bundleName);
            break;
        case RTSStatusCodeTrafficAPPIDFaild:
        case RTSStatusCodeTrafficFaild:
            message = LocalizedStringFromBundle(@"network_message_802", bundleName);
            break;
        case RTSStatusCodeVodFaild:
            message = LocalizedStringFromBundle(@"network_message_804", bundleName);
            break;
        case RTSStatusCodeTWFaild:
            message = LocalizedStringFromBundle(@"network_message_805", bundleName);
            break;
        case RTSStatusCodeBIDFaild:
            message = LocalizedStringFromBundle(@"network_message_806", bundleName);
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
        deviceId = [self getWisd];
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
