//
//  VoiceChatRoomRaiseHandListsView.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/19.
//  Copyright © 2021 . All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VoiceChatRoomUserListtCell.h"
#import "VoiceChatRTMManager.h"
@class VoiceChatRoomRaiseHandListsView;

NS_ASSUME_NONNULL_BEGIN

static NSString *const KClearRedNotification = @"KClearRedNotification";

@protocol VoiceChatRoomRaiseHandListsViewDelegate <NSObject>

- (void)voiceChatRoomRaiseHandListsView:(VoiceChatRoomRaiseHandListsView *)voiceChatRoomRaiseHandListsView clickButton:(VoiceChatUserModel *)model;

@end

@interface VoiceChatRoomRaiseHandListsView : UIView

@property (nonatomic, copy) NSArray<VoiceChatUserModel *> *dataLists;

@property (nonatomic, weak) id<VoiceChatRoomRaiseHandListsViewDelegate> delegate;

@end

NS_ASSUME_NONNULL_END
