//
//  VoiceChatTextInputComponent.h
//  veRTC_Demo
//
//  Created by on 2021/11/30.
//  
//

#import <Foundation/Foundation.h>
#import "VoiceChatRTMManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatTextInputComponent : NSObject

@property (nonatomic, copy) void (^clickSenderBlock)(NSString *text);

- (void)showWithRoomModel:(VoiceChatRoomModel *)roomModel;

@end

NS_ASSUME_NONNULL_END
