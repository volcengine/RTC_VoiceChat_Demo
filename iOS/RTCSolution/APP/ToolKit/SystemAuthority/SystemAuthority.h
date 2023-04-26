// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, AuthorizationType) {
    // Microphone
    AuthorizationTypeAudio = 0,
    // Camera
    AuthorizationTypeCamera,
} NS_AVAILABLE_IOS(7_0) __TVOS_PROHIBITED;

@interface SystemAuthority : NSObject

/*
 * Get system authorization status
 * @param type Microphone/camera
 * @param block Callback
 */
+ (void)authorizationStatusWithType:(AuthorizationType)type block:(void(^)(BOOL isAuthorize))block;

/*
 * Determine whether the current device is authorized type, if not authorized, jump to system settings
 * @param type Microphone/camera
 */
+ (void)autoJumpWithAuthorizationStatusWithType:(AuthorizationType)type;

@end

NS_ASSUME_NONNULL_END
