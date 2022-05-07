//
//  RTMACKModel.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/21.
//  Copyright © 2021 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface RTMACKModel : NSObject

@property (nonatomic, copy) NSString *requestID;
@property (nonatomic, assign) NSInteger code;
@property (nonatomic, copy) NSString *message;
@property (nonatomic, copy) NSString *timestamp;
@property (nonatomic, assign, readonly) BOOL result;
@property (nonatomic, strong) id response;

+ (instancetype)modelWithMessageData:(id)data;

@end

NS_ASSUME_NONNULL_END
