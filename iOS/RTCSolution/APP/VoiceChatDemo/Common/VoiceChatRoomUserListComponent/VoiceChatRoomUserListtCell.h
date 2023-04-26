// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>
#import "VoiceChatRTSManager.h"
@class VoiceChatRoomUserListtCell;

NS_ASSUME_NONNULL_BEGIN

@protocol VoiceChatRoomUserListtCellDelegate <NSObject>

- (void)VoiceChatRoomUserListtCell:(VoiceChatRoomUserListtCell *)VoiceChatRoomUserListtCell clickButton:(id)model;

@end

@interface VoiceChatRoomUserListtCell : UITableViewCell

@property (nonatomic, strong) VoiceChatUserModel *model;

@property (nonatomic, weak) id<VoiceChatRoomUserListtCellDelegate> delegate;

@end

NS_ASSUME_NONNULL_END
