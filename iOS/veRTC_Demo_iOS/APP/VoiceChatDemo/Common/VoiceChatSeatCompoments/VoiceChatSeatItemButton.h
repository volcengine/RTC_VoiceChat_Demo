//
//  VoiceChatSeatItemButton.h
//  quickstart
//
//  Created by bytedance on 2021/3/24.
//  Copyright © 2021 . All rights reserved.
//

#import "BaseButton.h"

typedef NS_ENUM(NSInteger, VoiceChatSheetStatus) {
    VoiceChatSheetStatusInvite = 0,
    VoiceChatSheetStatusKick,
    VoiceChatSheetStatusOpenMic,
    VoiceChatSheetStatusCloseMic,
    VoiceChatSheetStatusLock,
    VoiceChatSheetStatusUnlock,
    VoiceChatSheetStatusApply,      //观众申请上麦
    VoiceChatSheetStatusLeave,      //嘉宾主动下麦
};

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatSeatItemButton : BaseButton

@property (nonatomic, copy) NSString *desTitle;

@property (nonatomic, assign) VoiceChatSheetStatus sheetState;

@end

NS_ASSUME_NONNULL_END
