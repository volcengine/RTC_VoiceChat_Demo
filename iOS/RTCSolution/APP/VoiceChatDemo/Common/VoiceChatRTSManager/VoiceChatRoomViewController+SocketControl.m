// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "VoiceChatRoomViewController+SocketControl.h"

@implementation VoiceChatRoomViewController (SocketControl)

- (void)addSocketListener {
    __weak __typeof(self) wself = self;
    [VoiceChatRTSManager onAudienceJoinRoomWithBlock:^(VoiceChatUserModel * _Nonnull userModel, NSInteger count) {
        if (wself) {
            [wself receivedJoinUser:userModel count:count];
        }
    }];
    
    
    [VoiceChatRTSManager onAudienceLeaveRoomWithBlock:^(VoiceChatUserModel * _Nonnull userModel, NSInteger count) {
        if (wself) {
            [wself receivedLeaveUser:userModel count:count];
        }
    }];

    
    [VoiceChatRTSManager onFinishLiveWithBlock:^(NSString * _Nonnull rommID, NSInteger type) {
        if (wself) {
            [wself receivedFinishLive:type roomID:rommID];
        }
    }];

    
    [VoiceChatRTSManager onJoinInteractWithBlock:^(VoiceChatUserModel * _Nonnull userModel, NSString * _Nonnull seatID) {
        if (wself) {
            [wself receivedJoinInteractWithUser:userModel seatID:seatID];
        }
    }];

    
    [VoiceChatRTSManager onFinishInteractWithBlock:^(VoiceChatUserModel * _Nonnull userModel, NSString * _Nonnull seatID, NSInteger type) {
        if (wself) {
            [wself receivedLeaveInteractWithUser:userModel seatID:seatID type:type];
        }
    }];

    
    [VoiceChatRTSManager onSeatStatusChangeWithBlock:^(NSString * _Nonnull seatID, NSInteger type) {
        if (wself) {
            [wself receivedSeatStatusChange:seatID type:type];
        }
    }];

    
    [VoiceChatRTSManager onMediaStatusChangeWithBlock:^(VoiceChatUserModel * _Nonnull userModel, NSString * _Nonnull seatID, NSInteger mic) {
        if (wself) {
            [wself receivedMediaStatusChangeWithUser:userModel seatID:seatID mic:mic];
        }
    }];

    
    [VoiceChatRTSManager onMessageWithBlock:^(VoiceChatUserModel * _Nonnull userModel, NSString * _Nonnull message) {
        if (wself) {
            message = [message stringByRemovingPercentEncoding];
            [wself receivedMessageWithUser:userModel message:message];
        }
    }];

    //Single Notification Message
    
    [VoiceChatRTSManager onInviteInteractWithBlock:^(VoiceChatUserModel * _Nonnull hostUserModel, NSString * _Nonnull seatID) {
        if (wself) {
            [wself receivedInviteInteractWithUser:hostUserModel seatID:seatID];
        }
    }];
    
    [VoiceChatRTSManager onApplyInteractWithBlock:^(VoiceChatUserModel * _Nonnull userModel, NSString * _Nonnull seatID) {
        if (wself) {
            [wself receivedApplyInteractWithUser:userModel seatID:seatID];
        }
    }];

    [VoiceChatRTSManager onInviteResultWithBlock:^(VoiceChatUserModel * _Nonnull userModel, NSInteger reply) {
        if (wself) {
            [wself receivedInviteResultWithUser:userModel reply:reply];
        }
    }];
    
    [VoiceChatRTSManager onMediaOperateWithBlock:^(NSInteger mic) {
        if (wself) {
            [wself receivedMediaOperatWithUid:mic];
        }
    }];

    [VoiceChatRTSManager onClearUserWithBlock:^(NSString * _Nonnull uid) {
        if (wself) {
            [wself receivedClearUserWithUid:uid];
        }
    }];
}
@end
