//
//  VoiceChatTextInputView.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/30.
//  Copyright © 2021 . All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatTextInputView : UIView

@property (nonatomic, copy) void (^clickSenderBlock)(NSString *text);

- (instancetype)initWithMessage:(NSString *)message;

- (void)show;

- (void)dismiss:(void (^)(NSString *text))block;

@end

NS_ASSUME_NONNULL_END
