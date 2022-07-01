//
//  BytedEffectProtocol.h
//  AFNetworking
//
//  Created by bytedance on 2022/5/7.
//

#import <Foundation/Foundation.h>
#import <VolcEngineRTC/objc/rtc/ByteRTCEngineKit.h>
@class BytedEffectProtocol;

typedef NS_ENUM(NSUInteger, EffectBeautyRoleType) {
    EffectBeautyRoleTypeHost = 0,
    EffectBeautyRoleTypeGuest
};

NS_ASSUME_NONNULL_BEGIN

@protocol BytedEffectDelegate <NSObject>

- (instancetype)protocol:(BytedEffectProtocol *)protocol
    initWithRTCEngineKit:(ByteRTCEngineKit *)rtcEngineKit;

- (void)protocol:(BytedEffectProtocol *)protocol
    showWithType:(EffectBeautyRoleType)type
   fromSuperView:(UIView *)superView
    dismissBlock:(void (^)(BOOL result))block;

- (void)protocol:(BytedEffectProtocol *)protocol close:(BOOL)result;

- (void)protocol:(BytedEffectProtocol *)protocol resumeLocalEffect:(BOOL)result;

- (void)protocol:(BytedEffectProtocol *)protocol saveBeautyConfig:(BOOL)result;

- (void)protocol:(BytedEffectProtocol *)protocol resetLocalModelArray:(BOOL)result;

@end

@interface BytedEffectProtocol : NSObject


/**
 * Initialization
 * @param rtcEngineKit Rtc Engine
 */
- (instancetype)initWithRTCEngineKit:(ByteRTCEngineKit *)rtcEngineKit;

/**
 * Show effect beauty view
 * @param type Role Type
 * @param superView Super view
 * @param block Callback
 */
- (void)showWithType:(EffectBeautyRoleType)type
       fromSuperView:(UIView *)superView
        dismissBlock:(void (^)(BOOL result))block;

/**
 * Close effect beauty view
 */
- (void)close;

/**
 * Resume the last selected beauty
 */
- (void)resumeLocalEffect;

/**
 * Save beauty config
 */
- (void)saveBeautyConfig;

/**
 * Reset Local Array Data
 */
- (void)resetLocalModelArray;

@end

NS_ASSUME_NONNULL_END
