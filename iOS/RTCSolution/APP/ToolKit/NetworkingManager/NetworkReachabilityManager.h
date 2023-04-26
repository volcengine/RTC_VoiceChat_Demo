// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>
#import "BaseRTCManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkReachabilityManager : NSObject

+ (instancetype)sharedManager;

- (void)startMonitoring;

- (void)stopMonitoring;

- (NSString *)getNetType;

@end

NS_ASSUME_NONNULL_END
