//
//  BaseIMComponent.h
//  veRTC_Demo
//
//  Created by on 2021/5/23.
//  
//

#import <Foundation/Foundation.h>
#import "BaseIMModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface BaseIMComponent : NSObject

- (instancetype)initWithSuperView:(UIView *)superView;

- (void)addIM:(BaseIMModel *)model;

@end

NS_ASSUME_NONNULL_END
