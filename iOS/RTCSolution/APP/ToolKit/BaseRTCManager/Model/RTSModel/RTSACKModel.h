// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface RTSACKModel : NSObject

@property (nonatomic, copy) NSString *requestID;
@property (nonatomic, assign) NSInteger code;
@property (nonatomic, copy) NSString *message;
@property (nonatomic, copy) NSString *timestamp;
@property (nonatomic, assign, readonly) BOOL result;
@property (nonatomic, strong) id response;

+ (instancetype)modelWithMessageData:(id)data;

@end

NS_ASSUME_NONNULL_END
