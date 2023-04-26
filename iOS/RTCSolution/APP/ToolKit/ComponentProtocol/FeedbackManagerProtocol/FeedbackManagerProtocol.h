// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>
@class FeedbackManagerProtocol;

NS_ASSUME_NONNULL_BEGIN

@protocol FeedbackManagerDelegate <NSObject>

- (instancetype)protocol:(FeedbackManagerProtocol *)protocol
       initWithSuperView:(UIView *)superView
               scenesDic:(NSDictionary *)scenesDic;
        

@end

@interface FeedbackManagerProtocol : NSObject

/**
 * @brief 初始化
 * @param superView 入口的父视图
 * @param scenesDic 场景列表
 */
- (instancetype)initWithSuperView:(UIView *)superView
                        scenesDic:(NSDictionary *)scenesDic;

@end

NS_ASSUME_NONNULL_END
