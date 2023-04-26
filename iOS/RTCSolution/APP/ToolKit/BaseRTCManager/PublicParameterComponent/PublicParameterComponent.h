// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface PublicParameterComponent : NSObject

@property (nonatomic, copy) NSString *appId;

@property (nonatomic, copy) NSString *roomId;

+ (PublicParameterComponent *)share;

+ (void)clear;

@end

NS_ASSUME_NONNULL_END
