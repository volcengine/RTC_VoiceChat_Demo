// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, ButtonStatus) {
    ButtonStatusNone,
    ButtonStatusActive,
    ButtonStatusIng,
    ButtonStatusIllegal,
};

@interface BaseButton : UIButton

@property (nonatomic, assign) ButtonStatus status;

- (void)bingImage:(UIImage *)image status:(ButtonStatus)status;

@end

NS_ASSUME_NONNULL_END
