// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>
#import "VoiceChatRTSManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatHostAvatarView : UIView

@property (nonatomic, strong) VoiceChatUserModel *userModel;

- (void)updateHostVolume:(NSNumber *)volume;

- (void)updateHostMic:(UserMic)userMic;

@end

NS_ASSUME_NONNULL_END
