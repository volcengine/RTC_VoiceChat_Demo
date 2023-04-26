// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "BytedPlayerProtocol.h"

@interface BytedPlayerProtocol ()

@property (nonatomic, strong) id<BytedPlayerDelegate> bytePlayerDeleagte;

@end

@implementation BytedPlayerProtocol

- (instancetype)init {
    if (self = [super init]) {
        NSObject<BytedPlayerDelegate> *playerComponent = [[NSClassFromString(@"BytePlayerComponent") alloc] init];
        if (playerComponent) {
            self.bytePlayerDeleagte = playerComponent;
        }
    }

    return self;
}

- (void)setPlayerWithURL:(NSString *)urlString
               superView:(UIView *)superView
                SEIBlcok:(void (^)(NSDictionary *SEIDic))SEIBlcok {
    if ([self.bytePlayerDeleagte respondsToSelector:@selector(protocol:setPlayerWithURL:superView:SEIBlcok:)]) {
        [self.bytePlayerDeleagte protocol:self setPlayerWithURL:urlString superView:superView SEIBlcok:SEIBlcok];
    }
}

- (void)updatePlayScaleMode:(PullScalingMode)scalingMode {
    if ([self.bytePlayerDeleagte respondsToSelector:@selector(protocol:updatePlayScaleMode:)]) {
        [self.bytePlayerDeleagte protocol:self updatePlayScaleMode:scalingMode];
    }
}

- (void)play {
    if ([self.bytePlayerDeleagte respondsToSelector:@selector(protocolDidPlay:)]) {
        [self.bytePlayerDeleagte protocolDidPlay:self];
    }
}

- (void)replacePlayWithUrl:(NSString *)url {
    if ([self.bytePlayerDeleagte respondsToSelector:@selector(protocol:replacePlayWithUrl:)]) {
        [self.bytePlayerDeleagte protocol:self replacePlayWithUrl:url];
    }
}

- (void)stop {
    if ([self.bytePlayerDeleagte respondsToSelector:@selector(protocolDidStop:)]) {
        [self.bytePlayerDeleagte protocolDidStop:self];
    }
}

- (BOOL)isSupportSEI {
    if ([self.bytePlayerDeleagte respondsToSelector:@selector(protocolIsSupportSEI)]) {
        return [self.bytePlayerDeleagte protocolIsSupportSEI];
    } else {
        return NO;
    }
}

- (void)startWithConfiguration {
    if ([self.bytePlayerDeleagte respondsToSelector:@selector(protocolStartWithConfiguration)]) {
        [self.bytePlayerDeleagte protocolStartWithConfiguration];
    }
}

- (void)destroy {
    if ([self.bytePlayerDeleagte respondsToSelector:@selector(protocolDestroy:)]) {
        [self.bytePlayerDeleagte protocolDestroy:self];
    }
}

@end
