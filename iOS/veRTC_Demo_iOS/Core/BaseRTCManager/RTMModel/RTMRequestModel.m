//
//  RTMRequestModel.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/17.
//  Copyright © 2021 bytedance. All rights reserved.
//

#import "RTMRequestModel.h"

@implementation RTMRequestModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"eventName" : @"event_name",
             @"appID" : @"app_id",
             @"roomID" : @"room_id",
             @"userID" : @"user_id",
             @"requestID" : @"request_id",
             @"deviceID" : @"device_id"};
}

@end
