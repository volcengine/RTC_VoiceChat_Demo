//
//  VoiceChatSeatModel.m
//  veRTC_Demo
//
//  Created by on 2021/11/23.
//  
//

#import "VoiceChatSeatModel.h"

@implementation VoiceChatSeatModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"userModel" : @"guest_info"};
}

@end
