// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>
#import "ToolKit.h"

NS_ASSUME_NONNULL_BEGIN

@interface BaseViewController : UIViewController

@property (nonatomic, strong) UIView *navView;

@property (nonatomic, copy) NSString *navTitle;

@property (nonatomic, copy) NSString *navRightTitle;

@property (nonatomic, strong) UIImage *navLeftImage;

@property (nonatomic, strong) UIImage *navRightImage;

- (void)rightButtonAction:(BaseButton *)sender;

@end

NS_ASSUME_NONNULL_END
