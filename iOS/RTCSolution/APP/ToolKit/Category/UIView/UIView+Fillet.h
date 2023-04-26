// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, FilletStatus) {
    FilletStatusTopLeft = 1 << 0,
    FilletStatusTopRight = 1 << 1,
    FilletStatusBottomLeft = 1 << 0,
    FilletStatusBottomRight = 1 << 0,
};

NS_ASSUME_NONNULL_BEGIN

@interface UIView (Fillet)

- (void)filletWithRadius:(CGFloat)radius corner:(FilletStatus)corner;

- (void)removeAllAutoLayout;

@end

NS_ASSUME_NONNULL_END
