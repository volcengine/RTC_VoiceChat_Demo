//
//  BytedEffectProtocol.m
//  AFNetworking
//
//  Created by bytedance on 2022/5/7.
//

#import "BytedEffectProtocol.h"

@interface BytedEffectProtocol ()

@property (nonatomic, strong) id<BytedEffectDelegate> bytedEffectDelegate;

@end

@implementation BytedEffectProtocol

- (instancetype)initWithRTCEngineKit:(ByteRTCEngineKit *)rtcEngineKit {
    NSObject<BytedEffectDelegate> *effectBeautyCompoments = [[NSClassFromString(@"EffectBeautyCompoments") alloc] init];
    if (effectBeautyCompoments) {
        self.bytedEffectDelegate = effectBeautyCompoments;
    }
    
    if ([self.bytedEffectDelegate respondsToSelector:@selector(protocol:initWithRTCEngineKit:)]) {
        return [self.bytedEffectDelegate protocol:self
                             initWithRTCEngineKit:rtcEngineKit];
    } else {
        return nil;
    }
}

- (void)showWithType:(EffectBeautyRoleType)type
       fromSuperView:(UIView *)superView
        dismissBlock:(void (^)(BOOL result))block {
    if ([self.bytedEffectDelegate respondsToSelector:@selector(protocol:showWithType:fromSuperView:dismissBlock:)]) {
        [self.bytedEffectDelegate protocol:self
                              showWithType:type
                             fromSuperView:superView
                              dismissBlock:block];
    }
}

- (void)close {
    if ([self.bytedEffectDelegate respondsToSelector:@selector(protocol:close:)]) {
        [self.bytedEffectDelegate protocol:self
                                     close:YES];
    }
}

- (void)resumeLocalEffect {
    if ([self.bytedEffectDelegate respondsToSelector:@selector(protocol:resumeLocalEffect:)]) {
        [self.bytedEffectDelegate protocol:self
                         resumeLocalEffect:YES];
    }
}

- (void)saveBeautyConfig {
    if ([self.bytedEffectDelegate respondsToSelector:@selector(protocol:saveBeautyConfig:)]) {
        [self.bytedEffectDelegate protocol:self
                          saveBeautyConfig:YES];
    }
}

- (void)resetLocalModelArray {
    if ([self.bytedEffectDelegate respondsToSelector:@selector(protocol:resetLocalModelArray:)]) {
        [self.bytedEffectDelegate protocol:self
                      resetLocalModelArray:YES];
    }
}

@end
