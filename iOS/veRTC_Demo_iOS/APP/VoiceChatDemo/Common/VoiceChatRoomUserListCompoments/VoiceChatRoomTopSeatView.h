//
//  VoiceChatRoomTopSeatView.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/30.
//  Copyright © 2021 . All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

static NSString *const NotificationUpdateSeatSwitch = @"NotificationUpdateSeatSwitch";
static NSString *const NotificationResultSeatSwitch = @"NotificationResultSeatSwitch";

@interface VoiceChatRoomTopSeatView : UIView

@property (nonatomic, copy) void (^clickSwitchBlock)(BOOL isOn);

@end

NS_ASSUME_NONNULL_END
