// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "BaseButton.h"

NS_ASSUME_NONNULL_BEGIN

@interface MenuItemButton : BaseButton

@property (nonatomic, copy) NSString *desTitle;

@property (nonatomic, assign) BOOL isAction;

@property (nonatomic, assign) NSInteger tagNum;

@end

NS_ASSUME_NONNULL_END
