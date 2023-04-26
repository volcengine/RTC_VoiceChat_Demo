// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface PhonePrivacyView : UIView

@property (nonatomic, assign, readonly) BOOL isAgree;

@property (nonatomic, copy) void (^changeAgree)(BOOL isAgree);

@end

NS_ASSUME_NONNULL_END
