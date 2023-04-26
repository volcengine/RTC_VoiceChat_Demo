// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface MenuLoginHome : NSObject

/**
 * @brief 展示登录界面
 * @param isAnimation 是否展示动画
 */
+ (void)showLoginViewControllerAnimated:(BOOL)isAnimation;

/**
 * @brief 注销接口
 * @param block callback
 */
+ (void)closeAccount:(void (^)(BOOL result))block;

@end

NS_ASSUME_NONNULL_END
