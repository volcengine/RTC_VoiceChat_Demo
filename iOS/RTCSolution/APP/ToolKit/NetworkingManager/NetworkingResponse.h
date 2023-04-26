// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NetworkingResponse : NSObject

@property (nonatomic, assign) NSInteger code;

@property (nonatomic, copy) NSString *message;

@property (nonatomic, assign) NSTimeInterval timestamp;

@property (nonatomic, assign, readonly) BOOL result;

@property (nonatomic, copy) NSDictionary *response;

+ (instancetype)dataToResponseModel:(id _Nullable)data;

+ (instancetype)badServerResponse;

@end

NS_ASSUME_NONNULL_END
