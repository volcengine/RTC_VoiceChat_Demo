//
//  PublicParameterCompoments.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/7/2.
//  Copyright © 2021 . All rights reserved.
//

#import "PublicParameterCompoments.h"
#import "LocalUserComponents.h"

@implementation PublicParameterCompoments

+ (PublicParameterCompoments *)share {
    static dispatch_once_t onceToken;
    static PublicParameterCompoments *publicParameterCompoments;
    dispatch_once(&onceToken, ^{
        publicParameterCompoments = [[PublicParameterCompoments alloc] init];
    });
    return publicParameterCompoments;
}

+ (NSDictionary *)addTokenToParams:(NSDictionary *)dic {
    NSMutableDictionary *tokenDic = nil;
    if (dic && [dic isKindOfClass:[NSDictionary class]] && dic.count > 0) {
        tokenDic = [dic mutableCopy];
    } else {
        tokenDic = [[NSMutableDictionary alloc] init];
    }
    if (NOEmptyStr([LocalUserComponents userModel].uid)) {
        [tokenDic setValue:[LocalUserComponents userModel].uid
                    forKey:@"user_id"];
    }
    if (NOEmptyStr([LocalUserComponents userModel].loginToken)) {
        [tokenDic setValue:[LocalUserComponents userModel].loginToken
                    forKey:@"login_token"];
    }
    if (NOEmptyStr([PublicParameterCompoments share].appId)) {
        [tokenDic setValue:[PublicParameterCompoments share].appId
                    forKey:@"app_id"];
    }
    if (NOEmptyStr([PublicParameterCompoments share].roomId)) {
        [tokenDic setValue:[PublicParameterCompoments share].roomId
                    forKey:@"room_id"];
    }
    
    return [tokenDic copy];
}

+ (void)clear {
    [PublicParameterCompoments share].appId = @"";
    [PublicParameterCompoments share].roomId = @"";
}

@end
