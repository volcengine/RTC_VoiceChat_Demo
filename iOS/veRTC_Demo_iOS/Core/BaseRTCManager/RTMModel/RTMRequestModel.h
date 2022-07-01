//
//  RTMRequestModel.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/17.
//  Copyright © 2021 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
@class RTMACKModel;

NS_ASSUME_NONNULL_BEGIN

typedef void (^RTCSendServerMessageBlock)(RTMACKModel *ackModel);

@interface RTMRequestModel : NSObject

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
