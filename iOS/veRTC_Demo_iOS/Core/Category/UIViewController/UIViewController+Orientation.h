//
//  UIViewController+Orientation.h
//  quickstart
//
//  Created by on 2021/3/24.
//  
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSInteger, ScreenOrientation) {
    ScreenOrientationLandscapeAndPortrait = 1,
    ScreenOrientationLandscape,
    ScreenOrientationPortrait,
};

NS_ASSUME_NONNULL_BEGIN

@interface UIViewController (Orientation)

- (void)setDeviceInterfaceOrientation:(UIDeviceOrientation)orientation;

- (void)addOrientationNotice;

- (void)orientationDidChang:(BOOL)isLandscape;

- (void)setAllowAutoRotate:(ScreenOrientation)screenOrientation;

@end

NS_ASSUME_NONNULL_END
