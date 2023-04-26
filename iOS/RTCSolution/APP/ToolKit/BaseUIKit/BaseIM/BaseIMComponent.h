// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>
#import "BaseIMModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface BaseIMComponent : NSObject

- (instancetype)initWithSuperView:(UIView *)superView;

- (void)addIM:(BaseIMModel *)model;

- (void)updaetHidden:(BOOL)isHidden;

- (void)updateUserInteractionEnabled:(BOOL)isEnabled;

@end

NS_ASSUME_NONNULL_END
