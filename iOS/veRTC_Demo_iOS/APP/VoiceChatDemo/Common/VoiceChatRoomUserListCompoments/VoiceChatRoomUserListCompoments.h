//
//  VoiceChatRoomUserListCompoments.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/19.
//  Copyright © 2021 . All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VoiceChatRoomAudienceListsView.h"
#import "VoiceChatRoomRaiseHandListsView.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatRoomUserListCompoments : NSObject

- (void)showRoomModel:(VoiceChatRoomModel *)roomModel
               seatID:(NSString *)seatID
         dismissBlock:(void (^)(void))dismissBlock;

- (void)update;

- (void)updateWithRed:(BOOL)isRed;

@end

NS_ASSUME_NONNULL_END
