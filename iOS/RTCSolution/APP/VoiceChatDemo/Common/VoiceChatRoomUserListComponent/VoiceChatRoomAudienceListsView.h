// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>
#import "VoiceChatRoomUserListtCell.h"
#import "VoiceChatRTSManager.h"
@class VoiceChatRoomAudienceListsView;

NS_ASSUME_NONNULL_BEGIN

@protocol VoiceChatRoomAudienceListsViewDelegate <NSObject>

- (void)voiceChatRoomAudienceListsView:(VoiceChatRoomAudienceListsView *)voiceChatRoomAudienceListsView clickButton:(VoiceChatUserModel *)model;

@end


@interface VoiceChatRoomAudienceListsView : UIView

@property (nonatomic, copy) NSArray<VoiceChatUserModel *> *dataLists;

@property (nonatomic, weak) id<VoiceChatRoomAudienceListsViewDelegate> delegate;

@end

NS_ASSUME_NONNULL_END
