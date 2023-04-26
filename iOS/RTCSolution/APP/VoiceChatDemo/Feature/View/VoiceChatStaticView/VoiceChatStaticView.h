// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>
#import "VoiceChatRTSManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatStaticView : UIView

@property (nonatomic, strong) VoiceChatRoomModel *roomModel;

- (void)updatePeopleNum:(NSInteger)count;

- (void)updateParamInfoModel:(VoiceChatRoomParamInfoModel *)paramInfoModel;

@end

NS_ASSUME_NONNULL_END
