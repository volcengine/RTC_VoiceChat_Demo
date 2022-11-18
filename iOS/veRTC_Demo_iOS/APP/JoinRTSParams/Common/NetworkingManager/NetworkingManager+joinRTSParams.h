//
//  NetworkingManager+joinRTSParams.h
//  joinRTSParams
//
//  Created by bytedance on 2022/6/8.
//

#import "NetworkingManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkingManager (joinRTSParams)


#pragma mark - SetAppInfo

/*
 * Set App Info
 * @param dic Dic data
 */
+ (void)setAppInfoWithAppId:(NSDictionary *)dic
                      block:(void (^ __nullable)(NetworkingResponse *response))block;

@end

NS_ASSUME_NONNULL_END
