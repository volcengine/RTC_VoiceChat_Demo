// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "NetworkingManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkingManager (joinRTSParams)


#pragma mark - SetAppInfo

/*
 * Set App Info
 * @param dic Dic data
 */
+ (void)setAppInfoWithAppId:(NSDictionary *)dic
                      block:(void (^ __nullable)(NetworkingResponse *response))block;

@end

NS_ASSUME_NONNULL_END
