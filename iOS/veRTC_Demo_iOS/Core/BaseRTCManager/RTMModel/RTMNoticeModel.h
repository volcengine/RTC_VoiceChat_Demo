//
//  RTMNoticeModel.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/22.
//  Copyright © 2021 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface RTMNoticeModel : NSObject

@property (nonatomic, copy) NSString *eventName;
@property (nonatomic, copy) NSString *timestamp;
@property (nonatomic, strong) NSDictionary *data;

@end

NS_ASSUME_NONNULL_END
