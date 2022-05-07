//
//  LocalUserComponents.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/28.
//  Copyright © 2021 . All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseUserModel.h"

@interface LocalUserComponents : NSObject

+ (BaseUserModel *)userModel;

+ (void)updateLocalUserModel:(BaseUserModel *)userModel;

+ (BOOL)isMatchUserName:(NSString *)userName;

+ (BOOL)isMatchRoomID:(NSString *)roomid;

+ (BOOL)isMatchNumber:(NSString *)number;

@end
