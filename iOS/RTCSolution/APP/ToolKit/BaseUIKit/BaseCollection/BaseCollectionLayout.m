// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "BaseCollectionLayout.h"

@implementation BaseCollectionLayout

- (CGFloat)minimumInteritemSpacing {
    return 0;
}

- (UIEdgeInsets)sectionInset{
    return UIEdgeInsetsMake(0, 0, 0, 0);
}

- (NSArray*)layoutAttributesForElementsInRect:(CGRect)rect { 
    NSMutableArray* attributes = [[super layoutAttributesForElementsInRect:rect] mutableCopy];
    
    return attributes;
}

@end
