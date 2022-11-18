//
//  VoiceChatUserModel.m
//  SceneRTCDemo
//
//  Created by on 2021/3/16.
//

#import "VoiceChatUserModel.h"

@implementation VoiceChatUserModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"roomID" : @"room_id",
             @"uid" : @"user_id",
             @"name" : @"user_name",
             @"userRole" : @"user_role",
    };
}

- (BOOL)isSpeak {
    if (self.volume >= 60) {
        _isSpeak = YES;
    } else {
        _isSpeak = NO;
    }
    return _isSpeak;
}


@end
