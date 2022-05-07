//
//  VoiceChatTextInputCompoments.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/30.
//  Copyright © 2021 . All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VoiceChatRTMManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatTextInputCompoments : NSObject

@property (nonatomic, copy) void (^clickSenderBlock)(NSString *text);

- (void)showWithRoomModel:(VoiceChatRoomModel *)roomModel;

@end

NS_ASSUME_NONNULL_END
