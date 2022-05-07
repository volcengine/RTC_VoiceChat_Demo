//
//  VoiceChatHostAvatarView.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/29.
//  Copyright © 2021 . All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VoiceChatRTMManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatHostAvatarView : UIView

@property (nonatomic, strong) VoiceChatUserModel *userModel;

- (void)updateHostVolume:(NSNumber *)volume;

- (void)updateHostMic:(UserMic)userMic;

@end

NS_ASSUME_NONNULL_END
