// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface RTCJoinModel : NSObject

@property (nonatomic, copy) NSString *roomId;
@property (nonatomic, assign) NSInteger errorCode;
@property (nonatomic, assign) NSInteger joinType;

+ (RTCJoinModel *)modelArrayWithClass:(NSString *)extraInfo
                                state:(NSInteger)state
                               roomId:(NSString *)roomId;

@end

NS_ASSUME_NONNULL_END
