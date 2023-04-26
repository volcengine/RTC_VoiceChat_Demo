// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
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
    // 需要在 UIViewController 重写
}

- (void)setDeviceInterfaceOrientation:(UIDeviceOrientation)orientation {
    if (@available(iOS 16.0, *)) {
        [self setNeedsUpdateOfSupportedInterfaceOrientations];
        UIWindowScene *windowScene = (UIWindowScene *)[UIApplication sharedApplication].connectedScenes.allObjects.firstObject;
        if (![windowScene isKindOfClass:[UIWindowScene class]]) {
            return;
        }
        UIInterfaceOrientationMask mask = [self getOrientationMaskWithDeviceOrientation:orientation];
        UIWindowSceneGeometryPreferencesIOS *preferences = [[UIWindowSceneGeometryPreferencesIOS alloc] initWithInterfaceOrientations:mask];
        [windowScene requestGeometryUpdateWithPreferences:preferences errorHandler:^(NSError * _Nonnull error) {

        }];
    } else {
        if ([[UIDevice currentDevice] respondsToSelector:@selector(setOrientation:)]) {
            NSNumber *orientationUnknown = @(0);
            [[UIDevice currentDevice] setValue:orientationUnknown forKey:@"orientation"];
            
            [[UIDevice currentDevice] setValue:[NSNumber numberWithInteger:orientation] forKey:@"orientation"];
        }
    }
}

- (UIInterfaceOrientationMask)getOrientationMaskWithDeviceOrientation:(UIDeviceOrientation)orientation {
    UIInterfaceOrientationMask orientationMask = UIInterfaceOrientationMaskPortrait;
    switch (orientation) {
        case UIDeviceOrientationPortrait:
            orientationMask = UIInterfaceOrientationMaskPortrait;
            break;
        case UIDeviceOrientationPortraitUpsideDown:
            orientationMask = UIInterfaceOrientationMaskPortraitUpsideDown;
            break;
        case UIDeviceOrientationLandscapeLeft:
            orientationMask = UIInterfaceOrientationMaskLandscapeRight;
            break;
        case UIDeviceOrientationLandscapeRight:
            orientationMask = UIInterfaceOrientationMaskLandscapeLeft;
            break;
            
        default:
            
            break;
    }
    return orientationMask;
}

@end
