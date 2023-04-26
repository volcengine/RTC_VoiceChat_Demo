// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "NetworkingManager+Login.h"
#import <YYModel/YYModel.h>

@implementation NetworkingManager (Login)

#pragma mark - Login

+ (void)passwordFreeLogin:(NSString *)userName
                    block:(void (^)(BaseUserModel * _Nullable,
                                    NSString * _Nullable,
                                    NetworkingResponse * _Nonnull))block {
    NSDictionary *content = @{@"user_name" : userName ?: @""};
    [self postWithEventName:@"passwordFreeLogin"
                      space:@"login"
                    content:content
                      block:^(NetworkingResponse *response) {
        BaseUserModel *userModel = nil;
        NSString *loginToken = nil;
        if (response.result) {
            userModel = [BaseUserModel yy_modelWithJSON:response.response];
            loginToken = response.response[@"login_token"];
        }
        if (block) {
            block(userModel, loginToken, response);
        }
    }];
}

@end
