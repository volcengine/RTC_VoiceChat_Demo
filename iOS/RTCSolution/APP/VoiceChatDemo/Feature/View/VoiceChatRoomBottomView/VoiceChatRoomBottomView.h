// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>
#import "VoiceChatRoomItemButton.h"
#import "VoiceChatRTSManager.h"
@class VoiceChatRoomBottomView;

typedef NS_ENUM(NSInteger, VoiceChatRoomBottomStatus) {
    VoiceChatRoomBottomStatusPhone = 0,
    VoiceChatRoomBottomStatusMusic,
    VoiceChatRoomBottomStatusLocalMic,
    VoiceChatRoomBottomStatusEnd,
    VoiceChatRoomBottomStatusInput,
};

@protocol VoiceChatRoomBottomViewDelegate <NSObject>

- (void)voiceChatRoomBottomView:(VoiceChatRoomBottomView *_Nonnull)voiceChatRoomBottomView
                     itemButton:(VoiceChatRoomItemButton *_Nullable)itemButton
                didSelectStatus:(VoiceChatRoomBottomStatus)status;

@end

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatRoomBottomView : UIView

@property (nonatomic, weak) id<VoiceChatRoomBottomViewDelegate> delegate;

- (void)updateBottomLists:(VoiceChatUserModel *)userModel;

- (void)updateButtonStatus:(VoiceChatRoomBottomStatus)status isSelect:(BOOL)isSelect;

- (void)updateButtonStatus:(VoiceChatRoomBottomStatus)status isRed:(BOOL)isRed;

@end

NS_ASSUME_NONNULL_END
