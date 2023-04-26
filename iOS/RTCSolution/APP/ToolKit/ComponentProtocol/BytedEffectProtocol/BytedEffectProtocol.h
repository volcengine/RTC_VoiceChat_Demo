// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

@class BytedEffectProtocol;

NS_ASSUME_NONNULL_BEGIN

@protocol BytedEffectDelegate <NSObject>

- (instancetype)protocol:(BytedEffectProtocol *)protocol
    initWithRTCEngineKit:(id)rtcEngineKit;

- (void)protocol:(BytedEffectProtocol *)protocol
    showWithView:(UIView *)superView
    dismissBlock:(void (^)(BOOL result))block;

- (void)protocol:(BytedEffectProtocol *)protocol resume:(BOOL)result;

- (void)protocol:(BytedEffectProtocol *)protocol reset:(BOOL)result;

@end

@interface BytedEffectProtocol : NSObject

/**
 * @brief 初始化
 * @param rtcEngineKit 对象
 */
- (instancetype)initWithRTCEngineKit:(id)rtcEngineKit;

/**
 * @brief 展示美颜面板
 * @param superView 展示的 UIView
 * @param block 美颜面板消失后的回调
 */
- (void)showWithView:(UIView *)superView
        dismissBlock:(void (^)(BOOL result))block;

/**
 * @brief 恢复美颜效果
 */
- (void)resume;

/**
 * @brief 重置美颜缓存数据
 */
- (void)reset;

@end

NS_ASSUME_NONNULL_END
