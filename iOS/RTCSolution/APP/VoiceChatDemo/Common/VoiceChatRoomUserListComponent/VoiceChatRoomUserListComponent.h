// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>
#import "VoiceChatRoomAudienceListsView.h"
#import "VoiceChatRoomRaiseHandListsView.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatRoomUserListComponent : NSObject

- (void)showRoomModel:(VoiceChatRoomModel *)roomModel
               seatID:(NSString *)seatID
         dismissBlock:(void (^)(void))dismissBlock;

- (void)update;

- (void)updateWithRed:(BOOL)isRed;

@end

NS_ASSUME_NONNULL_END
