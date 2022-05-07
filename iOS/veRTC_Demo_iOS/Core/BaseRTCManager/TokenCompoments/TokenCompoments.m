//
//  TokenCompoments.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/7/2.
//  Copyright © 2021 . All rights reserved.
//

#import "TokenCompoments.h"
#import "LocalUserComponents.h"

@implementation TokenCompoments

+ (void)updateToken:(NSString *)token {
    if (IsEmptyStr(token)) {
        token = @"";
    }
    [[NSUserDefaults standardUserDefaults] setObject:token forKey:@"KTokenCompoments"];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

+ (NSString *)token {
    NSString *token = [[NSUserDefaults standardUserDefaults] objectForKey:@"KTokenCompoments"];
    return token;
}

+ (NSDictionary *)addTokenToParams:(NSDictionary *)dic {
    NSMutableDictionary *tokenDic = nil;
    if (dic && [dic isKindOfClass:[NSDictionary class]] && dic.count > 0) {
        tokenDic = [dic mutableCopy];
    } else {
        tokenDic = [[NSMutableDictionary alloc] init];
    }
    if (NOEmptyStr([TokenCompoments token])) {
        [tokenDic setValue:[TokenCompoments token] forKey:@"login_token"];
    }
    if (NOEmptyStr([LocalUserComponents userModel].uid)) {
        [tokenDic setValue:[LocalUserComponents userModel].uid forKey:@"user_id"];
    }
    return [tokenDic copy];
}

@end
