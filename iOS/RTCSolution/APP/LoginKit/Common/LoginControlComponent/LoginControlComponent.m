// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "LoginControlComponent.h"
#import "NetworkingManager+Login.h"

@implementation LoginControlComponent

+ (void)passwordFreeLogin:(NSString *)userName block:(void (^)(BOOL, NSString * _Nullable))block {
    [NetworkingManager passwordFreeLogin:userName
                                   block:^(BaseUserModel * _Nullable userModel,
                                           NSString * _Nullable loginToken,
                                           NetworkingResponse * _Nonnull response) {
        BOOL result = response.result;
        if (response.result) {
            [LocalUserComponent updateLocalUserModel:userModel];
        }
        if (block) {
            block(result, result? nil : response.message);
        }
    }];
}

@end
