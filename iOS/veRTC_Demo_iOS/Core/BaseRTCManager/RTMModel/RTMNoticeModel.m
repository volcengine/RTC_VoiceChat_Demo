//
//  RTMNoticeModel.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/22.
//  Copyright © 2021 bytedance. All rights reserved.
//

#import "RTMNoticeModel.h"

@implementation RTMNoticeModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"eventName" : @"event"};
}

@end
