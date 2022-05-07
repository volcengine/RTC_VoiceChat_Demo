//
//  VoiceChatRoomUserListView.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/18.
//  Copyright © 2021 . All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VoiceChatRoomUserListtCell.h"
#import "VoiceChatRTMManager.h"
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
