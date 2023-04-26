// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface BaseHomeDemo : NSObject

@property (nonatomic, copy) NSString *scenesName;

- (void)pushDemoViewControllerBlock:(void (^)(BOOL result))block;

@end

NS_ASSUME_NONNULL_END
