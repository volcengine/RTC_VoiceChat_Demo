#import <Foundation/Foundation.h>
#import "BaseRTCManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkReachabilityManager : NSObject

+ (instancetype)sharedManager;

- (void)startMonitoring;

- (void)stopMonitoring;

- (NSString *)getNetType;

@end

@interface NetworkReachabilityManager (RTCNetworking) <RTCNetworkProtocol>

@end

NS_ASSUME_NONNULL_END
