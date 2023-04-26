// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "MenuLoginHome.h"
#import "NetworkingManager.h"
#import "MenuLoginViewController.h"

@implementation MenuLoginHome

#pragma mark - Publish Action

+ (void)popLoginVC:(BOOL)isAnimation {
    // 开启网络监听
    [[NetworkReachabilityManager sharedManager] startMonitoring];
    
    if (IsEmptyStr([LocalUserComponent userModel].loginToken)) {
        [MenuLoginHome showLoginViewControllerAnimated:isAnimation];
    }
}

+ (void)closeAccount:(void (^)(BOOL result))block {
    if (block) {
        block(YES);
    }
}

#pragma mark - Private Action

+ (void)showLoginViewControllerAnimated:(BOOL)isAnimation {
    UIViewController *topVC = [DeviceInforTool topViewController];
    MenuLoginViewController *loginVC = [[MenuLoginViewController alloc] init];
    loginVC.modalPresentationStyle = UIModalPresentationFullScreen;
    [topVC presentViewController:loginVC animated:isAnimation completion:^{

    }];
}

@end
