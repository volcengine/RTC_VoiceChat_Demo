// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface BaseUserModel : NSObject <NSSecureCoding>

@property (nonatomic, copy) NSString *uid;

@property (nonatomic, copy) NSString *loginToken;

@property (nonatomic, copy) NSString *name;

@end

NS_ASSUME_NONNULL_END
