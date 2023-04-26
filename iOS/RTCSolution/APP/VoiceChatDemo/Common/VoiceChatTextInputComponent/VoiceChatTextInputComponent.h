// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>
#import "VoiceChatRTSManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatTextInputComponent : NSObject

@property (nonatomic, copy) void (^clickSenderBlock)(NSString *text);

- (void)showWithRoomModel:(VoiceChatRoomModel *)roomModel;

@end

NS_ASSUME_NONNULL_END
