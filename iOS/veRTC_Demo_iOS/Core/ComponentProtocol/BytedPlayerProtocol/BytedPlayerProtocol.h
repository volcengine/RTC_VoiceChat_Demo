//
//  BytedPlayerProtocol.h
//  Core
//
//  Created by bytedance on 2022/5/9.
//

#import <Foundation/Foundation.h>
@class BytedPlayerProtocol;

NS_ASSUME_NONNULL_BEGIN

@protocol BytedPlayerDelegate <NSObject>

- (void)protocol:(BytedPlayerProtocol *)protocol
 startPlayWithUrl:(NSString *)urlString
        superView:(UIView *)superView;

- (void)protocol:(BytedPlayerProtocol *)protocol updatePlayScaleMode:(BOOL)isFill;

- (void)protocolDidPlay:(BytedPlayerProtocol *)protocol;

- (void)protocol:(BytedPlayerProtocol *)protocol replacePlayWithUrl:(NSString *)url;

- (void)protocolDidStop:(BytedPlayerProtocol *)protocol;


@end

@interface BytedPlayerProtocol : NSObject

- (void)startPlayWithUrl:(NSString *)urlString
               superView:(UIView *)superView;

- (void)updatePlayScaleMode:(BOOL)isFill;

- (void)play;

- (void)replacePlayWithUrl:(NSString *)url;

- (void)stop;

@end

NS_ASSUME_NONNULL_END
