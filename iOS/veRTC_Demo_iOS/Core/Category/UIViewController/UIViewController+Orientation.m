//
//  UIViewController+Orientation.m
//  quickstart
//
//  Created by bytedance on 2021/3/24.
//  Copyright © 2021 . All rights reserved.
//

#import "UIViewController+Orientation.h"

@implementation UIViewController (Orientation)

- (void)addOrientationNotice {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onDeviceOrientationDidChange)
                                                 name:UIApplicationDidChangeStatusBarOrientationNotification
                                                   object:nil];
    [[UIDevice currentDevice] beginGeneratingDeviceOrientationNotifications];
}

- (void)setAllowAutoRotate:(ScreenOrientation)screenOrientation {
    [[NSNotificationCenter defaultCenter] postNotificationName:@"SetAllowAutoRotateNotification" object:@(screenOrientation)];
    // 更新 AppDelegate -supportedInterfaceOrientationsForWindow 代理信息
}

- (void)onDeviceOrientationDidChange {
    BOOL isLandscape = NO;
    UIInterfaceOrientation interfaceOrientation = [[UIApplication sharedApplication] statusBarOrientation];
    switch (interfaceOrientation) {
        
        case UIInterfaceOrientationUnknown:
            break;
        
        case UIInterfaceOrientationPortrait:
            break;
        
        case UIInterfaceOrientationPortraitUpsideDown:
            break;
        
        case UIInterfaceOrientationLandscapeLeft:
            isLandscape = YES;
            break;
        
        case UIInterfaceOrientationLandscapeRight:
            isLandscape = YES;
            break;
    
        default:
            break;
    }
    [self orientationDidChang:isLandscape];
}

- (void)orientationDidChang:(BOOL)isLandscape {
    //在 UIViewController 重写
    //Rewrite in UIViewController
}

- (void)setDeviceInterfaceOrientation:(UIDeviceOrientation)orientation {
    if ([[UIDevice currentDevice] respondsToSelector:@selector(setOrientation:)]) {
        NSNumber *orientationUnknown = @(0);
        [[UIDevice currentDevice] setValue:orientationUnknown forKey:@"orientation"];
        
        [[UIDevice currentDevice] setValue:[NSNumber numberWithInteger:orientation] forKey:@"orientation"];
    }
}

@end
