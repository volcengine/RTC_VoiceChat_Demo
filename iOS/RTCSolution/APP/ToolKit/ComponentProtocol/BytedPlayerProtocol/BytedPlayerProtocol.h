// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@class BytedPlayerProtocol;

typedef NS_ENUM(NSInteger, PullScalingMode) {
    PullScalingModeNone,
    PullScalingModeAspectFit,
    PullScalingModeAspectFill,
    PullScalingModeFill
};

NS_ASSUME_NONNULL_BEGIN

@protocol BytedPlayerDelegate <NSObject>

- (void)protocol:(BytedPlayerProtocol *)protocol
 setPlayerWithURL:(NSString *)urlString
        superView:(UIView *)superView
        SEIBlcok:(void (^)(NSDictionary *SEIDic))SEIBlcok;

- (void)protocol:(BytedPlayerProtocol *)protocol updatePlayScaleMode:(PullScalingMode)scalingMode;

- (void)protocolDidPlay:(BytedPlayerProtocol *)protocol;

- (void)protocolDidStop:(BytedPlayerProtocol *)protocol;

- (void)protocolDestroy:(BytedPlayerProtocol *)protocol;

- (void)protocol:(BytedPlayerProtocol *)protocol replacePlayWithUrl:(NSString *)url;

- (BOOL)protocolIsSupportSEI;

- (void)protocolStartWithConfiguration;

@end

@interface BytedPlayerProtocol : NSObject

/**
 * @brief 启动配置 Player
 */
- (void)startWithConfiguration;

/**
 * @brief 设置播放地址、父视图
 * @param urlString 拉流地址
 * @param superView 父类视图
 * @param SEIBlcok SEI 回调
 */
- (void)setPlayerWithURL:(NSString *)urlString
               superView:(UIView *)superView
                SEIBlcok:(void (^)(NSDictionary *SEIDic))SEIBlcok;

/**
 * @brief 更新播放比例模式
 * @param scalingMode 播放比例模式
 */
- (void)updatePlayScaleMode:(PullScalingMode)scalingMode;

/**
 * @brief 开始播放
 */
- (void)play;

/**
 * @brief 停止播放
 */
- (void)stop;

/**
 * @brief 更新新的播放地址
 * @param url 新的播放地址
 */
- (void)replacePlayWithUrl:(NSString *)url;

/**
 * @brief 播放器是否支持 SEI 功能
 * @return BOOL YES 支持SEI，NO 不支持 SEI
 */
- (BOOL)isSupportSEI;

/**
 * @brief 释放播放器
 */
- (void)destroy;

@end

NS_ASSUME_NONNULL_END
