//
//  VoiceChatRoomUserListtCell.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/19.
//  Copyright © 2021 . All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VoiceChatRTMManager.h"
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
