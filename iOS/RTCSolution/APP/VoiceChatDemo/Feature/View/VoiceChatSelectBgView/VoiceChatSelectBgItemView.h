// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "BaseButton.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatSelectBgItemView : BaseButton

@property (nonatomic, assign) BOOL isSelected;

- (instancetype)initWithIndex:(NSInteger)index;

- (NSString *)getBackgroundImageName;

- (NSString *)getBackgroundSmallImageName;

@end

NS_ASSUME_NONNULL_END
