// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "DeviceInforTool.h"

@implementation DeviceInforTool

#pragma mark - Publish Action

+ (BOOL)isIpad {
    NSString *deviceType = [UIDevice currentDevice].model;
    if ([deviceType isEqualToString:@"iPhone"]) {
        // iPhone
        return NO;
    } else if ([deviceType isEqualToString:@"iPod touch"]) {
        // iPod Touch
        return NO;
    } else if ([deviceType isEqualToString:@"iPad"]) {
        // iPad
        return YES;
    }
    return NO;
}

+ (CGFloat)getStatusBarHight {
    float statusBarHeight = 0;
    if (@available(iOS 13.0, *)) {
        UIStatusBarManager *statusBarManager = [UIApplication sharedApplication].windows.firstObject.windowScene.statusBarManager;
        statusBarHeight = statusBarManager.statusBarFrame.size.height;
    } else {
        statusBarHeight = [UIApplication sharedApplication].statusBarFrame.size.height;
    }
    return statusBarHeight;
}

+ (CGFloat)getVirtualHomeHeight {
    CGFloat virtualHomeHeight = 0;
    if (@available(iOS 11.0, *)) {
        UIWindow *keyWindow = [UIApplication sharedApplication].keyWindow;
        virtualHomeHeight = keyWindow.safeAreaInsets.bottom;
    }
    return virtualHomeHeight;
}

+ (UIEdgeInsets)getSafeAreaInsets{
    UIEdgeInsets safeAreaInsets = UIEdgeInsetsZero;
    if (@available(iOS 11.0, *)) {
        UIWindow *keyWindow = [UIApplication sharedApplication].keyWindow;
        safeAreaInsets = keyWindow.safeAreaInsets;
    }
    return safeAreaInsets;
}

+ (UIViewController *)topViewController {
    UIViewController *resultVC;
    resultVC = [self _topViewController:[[UIApplication sharedApplication].keyWindow rootViewController]];
    while (resultVC.presentedViewController) {
        resultVC = [self _topViewController:resultVC.presentedViewController];
    }
    if ([resultVC isKindOfClass:[UIAlertController class]]) {
        resultVC = [self _topViewController:[[UIApplication sharedApplication].keyWindow rootViewController]];
    }
    return resultVC;
}

+ (UIViewController *)_topViewController:(UIViewController *)vc {
    if ([vc isKindOfClass:[UINavigationController class]]) {
        return [self _topViewController:[(UINavigationController *)vc topViewController]];
    } else if ([vc isKindOfClass:[UITabBarController class]]) {
        return [self _topViewController:[(UITabBarController *)vc selectedViewController]];
    } else {
        return vc;
    }
    return nil;
}

+ (void)backToRootViewController {
    UIViewController *rootViewController = [[UIApplication sharedApplication].keyWindow rootViewController];
    UIViewController *presentedViewController = nil;
    
    if ([rootViewController isKindOfClass:[UITabBarController class]]) {
        rootViewController = [(UITabBarController *)rootViewController selectedViewController];
    }
    
    presentedViewController = rootViewController;
    NSMutableArray<UIViewController *> *presentedViewControllers = [NSMutableArray array];
    while (presentedViewController.presentedViewController) {
        [presentedViewControllers addObject:presentedViewController.presentedViewController];
        presentedViewController = presentedViewController.presentedViewController;
    }
    
    if (presentedViewControllers.count > 0) {
        [self dismissViewControllers:presentedViewControllers topIndex:presentedViewControllers.count - 1 complete:^{
            [self popToRootViewController:rootViewController];
        }];
    }
    else {
        [self popToRootViewController:rootViewController];
    }
}

+ (void)dismissViewControllers:(NSArray<UIViewController *> *)array topIndex:(NSInteger)index complete:(void(^)(void))complete {
    if (index < 0) {
        if (complete) {
            complete();
        }
    }
    else {
        [array[index] dismissViewControllerAnimated:NO completion:^{
            [self dismissViewControllers:array topIndex:index - 1 complete:complete];
        }];
    }
}

+ (void)popToRootViewController:(UIViewController *)viewController {
    if ([viewController isKindOfClass:[UINavigationController class]]) {
        [(UINavigationController *)viewController popToRootViewControllerAnimated:YES];
    }
    else if ([viewController isKindOfClass:[UIViewController class]]) {
        [viewController.navigationController popToRootViewControllerAnimated:YES];
    }
}

@end
