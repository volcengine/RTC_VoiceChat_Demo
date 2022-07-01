//
//  VoiceChatDemo.m
//  AFNetworking
//
//  Created by bytedance on 2022/4/21.
//

#import "VoiceChatDemo.h"
#import "VoiceChatRoomListsViewController.h"
#import <Core/NetworkReachabilityManager.h>

@implementation VoiceChatDemo

- (void)pushDemoViewControllerBlock:(void (^)(BOOL result))block {
    [VoiceChatRTCManager shareRtc].networkDelegate = [NetworkReachabilityManager sharedManager];
    [[VoiceChatRTCManager shareRtc] connect:@"svc"
                                 loginToken:[LocalUserComponents userModel].loginToken
                                      block:^(BOOL result) {
        if (result) {
            VoiceChatRoomListsViewController *next = [[VoiceChatRoomListsViewController alloc] init];
            UIViewController *topVC = [DeviceInforTool topViewController];
            [topVC.navigationController pushViewController:next animated:YES];
        } else {
            [[ToastComponents shareToastComponents] showWithMessage:@"连接失败"];
        }
        if (block) {
            block(result);
        }
    }];
}

@end
