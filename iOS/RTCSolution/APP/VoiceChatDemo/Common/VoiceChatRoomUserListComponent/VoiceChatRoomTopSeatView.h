// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

static NSString *const NotificationUpdateSeatSwitch = @"NotificationUpdateSeatSwitch";
static NSString *const NotificationResultSeatSwitch = @"NotificationResultSeatSwitch";

@interface VoiceChatRoomTopSeatView : UIView

@property (nonatomic, copy) void (^clickSwitchBlock)(BOOL isOn);

@end

NS_ASSUME_NONNULL_END
