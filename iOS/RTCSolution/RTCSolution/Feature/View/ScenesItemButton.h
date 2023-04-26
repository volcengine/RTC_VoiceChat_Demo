// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>
#import "SceneButtonModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface ScenesItemButton : UIButton

@property (nonatomic, assign) SceneButtonModel *model;

- (void)addItemLayer;

@end

NS_ASSUME_NONNULL_END
