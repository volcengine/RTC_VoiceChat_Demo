//
//  LoginControlComponent.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/23.
//  Copyright © 2021 bytedance. All rights reserved.
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
