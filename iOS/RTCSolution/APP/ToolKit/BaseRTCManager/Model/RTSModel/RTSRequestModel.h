// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>
@class RTSACKModel;

NS_ASSUME_NONNULL_BEGIN

typedef void (^RTCSendServerMessageBlock)(RTSACKModel *ackModel);

@interface RTSRequestModel : NSObject

@property (nonatomic, copy) NSString *app_id;
@property (nonatomic, copy) NSString *roomID;
@property (nonatomic, copy) NSString *userID;
@property (nonatomic, copy) NSString *requestID;
@property (nonatomic, assign) NSInteger msgid;
@property (nonatomic, copy) NSString *eventName;
@property (nonatomic, copy) NSString *content;
@property (nonatomic, copy) NSString *deviceID;
@property (nonatomic, copy) RTCSendServerMessageBlock requestBlock;

@end

NS_ASSUME_NONNULL_END
