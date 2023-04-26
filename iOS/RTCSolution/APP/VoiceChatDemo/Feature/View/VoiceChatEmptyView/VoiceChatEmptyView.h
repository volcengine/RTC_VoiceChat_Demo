// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatEmptyView : NSObject

- (instancetype)initWithView:(UIView *)view message:(NSString *)message;

- (void)show;

- (void)dismiss;

@end

NS_ASSUME_NONNULL_END
