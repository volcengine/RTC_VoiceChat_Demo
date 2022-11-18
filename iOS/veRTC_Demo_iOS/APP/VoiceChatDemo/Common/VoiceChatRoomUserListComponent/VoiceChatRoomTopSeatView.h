//
//  VoiceChatRoomTopSeatView.h
//  veRTC_Demo
//
//  Created by on 2021/11/30.
//  
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

static NSString *const NotificationUpdateSeatSwitch = @"NotificationUpdateSeatSwitch";
static NSString *const NotificationResultSeatSwitch = @"NotificationResultSeatSwitch";

@interface VoiceChatRoomTopSeatView : UIView

@property (nonatomic, copy) void (^clickSwitchBlock)(BOOL isOn);

@end

NS_ASSUME_NONNULL_END
