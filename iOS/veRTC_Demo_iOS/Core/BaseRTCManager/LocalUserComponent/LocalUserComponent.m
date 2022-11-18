//
//  LocalUserComponent.m
//  veRTC_Demo
//
//  Created by on 2021/5/28.
//  
//

#import "LocalUserComponent.h"

@implementation LocalUserComponent

#pragma mark - Publish Action

+ (BaseUserModel *)userModel {
    NSData *data = [[NSUserDefaults standardUserDefaults] objectForKey:@"KUserinfoDic"];
    BaseUserModel *user;
    if (@available(iOS 11.0, *)) {
        user = [NSKeyedUnarchiver unarchivedObjectOfClass:[BaseUserModel class]
                                                                fromData:data error:nil];
    }
    if (user == nil || ![user isKindOfClass:[BaseUserModel class]]) {
        user = [[BaseUserModel alloc] init];
    }
    return user;
}

+ (void)updateLocalUserModel:(BaseUserModel *)userModel {
    if (userModel && [userModel isKindOfClass:[BaseUserModel class]]) {
        if (@available(iOS 11.0, *)) {
            NSData *data =  [NSKeyedArchiver archivedDataWithRootObject:userModel
                                                  requiringSecureCoding:NO error:nil];
            [[NSUserDefaults standardUserDefaults] setObject:data forKey:@"KUserinfoDic"];
            [[NSUserDefaults standardUserDefaults] synchronize];
        }
    } else if (userModel == nil) {
        [[NSUserDefaults standardUserDefaults] setObject:nil forKey:@"KUserinfoDic"];
        [[NSUserDefaults standardUserDefaults] synchronize];
    }
}

#pragma mark - tool

+ (BOOL)isMatchUserName:(NSString *)userName {
    if (!userName || userName.length <= 0) {
        return YES;
    }
    NSString *match = @"^[\u4e00-\u9fa5a-zA-Z0-9]+$";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF matches %@", match];
    return [predicate evaluateWithObject:userName];
}

+ (BOOL)isMatchRoomID:(NSString *)roomid {
    if (!roomid || roomid.length <= 0) {
        return YES;
    }
    NSString *match = @"^[A-Za-z0-9@._-]+$";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF matches %@", match];
    return [predicate evaluateWithObject:roomid];
}

+ (BOOL)isMatchNumber:(NSString *)number {
    if (!number || number.length <= 0) {
        return YES;
    }
    NSString *match = @"[0-9]*";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF matches %@", match];
    return [predicate evaluateWithObject:number];
}

@end
