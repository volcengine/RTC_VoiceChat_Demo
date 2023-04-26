// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "BytedEffectProtocol.h"

@interface BytedEffectProtocol ()

@property (nonatomic, strong) id<BytedEffectDelegate> bytedEffectDelegate;

@end

@implementation BytedEffectProtocol

- (instancetype)initWithRTCEngineKit:(id)rtcEngineKit {
    // 开源代码暂不支持美颜相关功能，体验效果请下载Demo
    // Open source code does not support beauty related functions, please download Demo to experience the effect
    NSObject<BytedEffectDelegate> *effectBeautyComponent = [[NSClassFromString(@"EffectBeautyComponent") alloc] init];
    if (effectBeautyComponent) {
        self.bytedEffectDelegate = effectBeautyComponent;
    }
    
    if ([self.bytedEffectDelegate respondsToSelector:@selector(protocol:initWithRTCEngineKit:)]) {
        return [self.bytedEffectDelegate protocol:self
                             initWithRTCEngineKit:rtcEngineKit];
    } else {
        return nil;
    }
}

- (void)showWithView:(UIView *)superView
        dismissBlock:(void (^)(BOOL result))block {
    if ([self.bytedEffectDelegate respondsToSelector:@selector(protocol:showWithView:dismissBlock:)]) {
        [self.bytedEffectDelegate protocol:self
                              showWithView:superView
                              dismissBlock:block];
    }
}

- (void)resume {
    if ([self.bytedEffectDelegate respondsToSelector:@selector(protocol:resume:)]) {
        [self.bytedEffectDelegate protocol:self
                                    resume:YES];
    }
}

- (void)reset {
    if ([self.bytedEffectDelegate respondsToSelector:@selector(protocol:reset:)]) {
        [self.bytedEffectDelegate protocol:self
                                     reset:YES];
    }
}

@end
