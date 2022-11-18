//
//  BytedPlayerProtocol.h
//  Core
//
//  Created by on 2022/5/9.
//

#import <Foundation/Foundation.h>
@class BytedPlayerProtocol;

NS_ASSUME_NONNULL_BEGIN

@protocol BytedPlayerDelegate <NSObject>

- (void)protocol:(BytedPlayerProtocol *)protocol
 startPlayWithUrl:(NSString *)urlString
        superView:(UIView *)superView
        SEIBlcok:(void (^)(NSDictionary *SEIDic))SEIBlcok;

- (void)protocol:(BytedPlayerProtocol *)protocol updatePlayScaleMode:(BOOL)isFill;

- (void)protocolDidPlay:(BytedPlayerProtocol *)protocol;

- (void)protocol:(BytedPlayerProtocol *)protocol replacePlayWithUrl:(NSString *)url;

- (void)protocolDidStop:(BytedPlayerProtocol *)protocol;

- (BOOL)protocolIsSupportSEI;

- (void)protocolStartWithConfiguration;

@end

@interface BytedPlayerProtocol : NSObject

- (void)startWithConfiguration;

- (void)startPlayWithUrl:(NSString *)urlString
               superView:(UIView *)superView
                SEIBlcok:(void (^)(NSDictionary *SEIDic))SEIBlcok;

- (void)updatePlayScaleMode:(BOOL)isFill;

- (void)play;

- (void)replacePlayWithUrl:(NSString *)url;

- (void)stop;

- (BOOL)isSupportSEI;

@end

NS_ASSUME_NONNULL_END
