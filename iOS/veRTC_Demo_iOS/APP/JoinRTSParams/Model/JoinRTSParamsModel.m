//
//  JoinRTSParamsModel.m
//  JoinRTSParams
//
//  Created by bytedance on 2022/7/15.
//

#import "JoinRTSParamsModel.h"

@implementation JoinRTSParamsModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"appId" : @"app_id",
             @"RTSToken" : @"rtm_token",
             @"serverUrl" : @"server_url",
             @"serverSignature" : @"server_signature",
             @"bid" : @"bid"
    };
}

@end
