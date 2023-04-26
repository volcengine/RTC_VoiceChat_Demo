// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "SystemAuthority.h"
#import <AVFoundation/AVFoundation.h>
#import <UIKit/UIKit.h>

@implementation SystemAuthority

+ (void)authorizationStatusWithType:(AuthorizationType)type block:(void (^)(BOOL isAuthorize))block {
    AVAuthorizationStatus authStatus = [[self class] getAuthStatusWithType:type];
    if (authStatus == AVAuthorizationStatusNotDetermined) {
        AVMediaType mediaType = (type == AuthorizationTypeAudio) ? AVMediaTypeAudio : AVMediaTypeVideo;
        [AVCaptureDevice requestAccessForMediaType:mediaType completionHandler:^(BOOL granted) {
            dispatch_queue_async_safe(dispatch_get_main_queue(), ^{
                if (block) {
                    block(granted);
                }
            });
        }];
    } else if (authStatus == AVAuthorizationStatusRestricted ||
               authStatus == AVAuthorizationStatusDenied) {
        // 未经授权
        if (block) {
            block(NO);
        }
    } else if (authStatus == AVAuthorizationStatusAuthorized) {
        // 授权
        if (block) {
            block(YES);
        }
    } else {
        // 未知类型
        if (block) {
            block(NO);
        }
    }
}

+ (void)autoJumpWithAuthorizationStatusWithType:(AuthorizationType)type {
    [[self class] authorizationStatusWithType:type block:^(BOOL isAuthorize) {
        if (!isAuthorize) {
            // 跳转到『设置』
            NSURL *url = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
            if ([[UIApplication sharedApplication] canOpenURL:url]) {
                if (@available(iOS 10.0, *)) {
                    [[UIApplication sharedApplication] openURL:url options:@{} completionHandler:^(BOOL success) {
                        
                    }];
                } else {
                    [[UIApplication sharedApplication] openURL:url];
                }
            }
        }
    }];
}

#pragma mark - Private Action

+ (AVAuthorizationStatus)getAuthStatusWithType:(AuthorizationType)type {
    AVAuthorizationStatus authStatus = AVAuthorizationStatusNotDetermined;
    if (type == AuthorizationTypeAudio) {
        authStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeAudio];
    } else if (type == AuthorizationTypeCamera) {
        authStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
    }
    return authStatus;
}

@end
