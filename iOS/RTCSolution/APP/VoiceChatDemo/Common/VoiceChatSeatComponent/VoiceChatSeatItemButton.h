// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "BaseButton.h"

typedef NS_ENUM(NSInteger, VoiceChatSheetStatus) {
    VoiceChatSheetStatusInvite = 0,
    VoiceChatSheetStatusKick,
    VoiceChatSheetStatusOpenMic,
    VoiceChatSheetStatusCloseMic,
    VoiceChatSheetStatusLock,
    VoiceChatSheetStatusUnlock,
    // 观众申请上麦
    VoiceChatSheetStatusApply,
    // 嘉宾主动下麦
    VoiceChatSheetStatusLeave,
};

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatSeatItemButton : BaseButton

@property (nonatomic, copy) NSString *desTitle;

@property (nonatomic, assign) VoiceChatSheetStatus sheetState;

@end

NS_ASSUME_NONNULL_END
