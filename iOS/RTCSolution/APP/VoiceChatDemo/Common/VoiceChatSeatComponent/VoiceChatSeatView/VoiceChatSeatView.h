// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>
#import "VoiceChatRTSManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatSeatView : UIView

@property (nonatomic, copy) void (^clickBlock)(VoiceChatSeatModel *seatModel);

@property (nonatomic, copy) NSArray<VoiceChatSeatModel *> *seatList;

- (void)addSeatModel:(VoiceChatSeatModel *)seatModel;

- (void)removeUserModel:(VoiceChatUserModel *)userModel;

- (void)updateSeatModel:(VoiceChatSeatModel *)seatModel;

- (void)updateSeatVolume:(NSDictionary *)volumeDic;

@end

NS_ASSUME_NONNULL_END
