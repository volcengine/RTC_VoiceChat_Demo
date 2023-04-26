// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSInteger, ScreenOrientation) {
    ScreenOrientationLandscapeAndPortrait = 1,
    ScreenOrientationLandscape,
    ScreenOrientationPortrait,
};

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (nonatomic, strong) UIWindow *window;

@property (nonatomic, assign) ScreenOrientation screenOrientation;

@end

