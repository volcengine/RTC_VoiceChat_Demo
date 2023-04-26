// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatSelectBgView : UIView

@property (nonatomic, copy) void (^clickBlock)(NSString *imageName,
                                               NSString *smallImageName);

- (NSString *)getDefaults;

@end

NS_ASSUME_NONNULL_END
