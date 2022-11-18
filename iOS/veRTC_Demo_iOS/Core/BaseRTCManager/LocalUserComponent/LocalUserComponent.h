//
//  LocalUserComponent.h
//  veRTC_Demo
//
//  Created by on 2021/5/28.
//  
//

#import <Foundation/Foundation.h>
#import "BaseUserModel.h"

@interface LocalUserComponent : NSObject

+ (BaseUserModel *)userModel;

+ (void)updateLocalUserModel:(BaseUserModel *)userModel;

+ (BOOL)isMatchUserName:(NSString *)userName;

+ (BOOL)isMatchRoomID:(NSString *)roomid;

+ (BOOL)isMatchNumber:(NSString *)number;

@end
