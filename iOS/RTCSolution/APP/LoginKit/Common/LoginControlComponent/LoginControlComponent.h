// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface LoginControlComponent : NSObject

// 免密登录
+ (void)passwordFreeLogin:(NSString *)userName
                    block:(void (^ __nullable)(BOOL result, NSString * _Nullable errorStr))block;

@end

NS_ASSUME_NONNULL_END
