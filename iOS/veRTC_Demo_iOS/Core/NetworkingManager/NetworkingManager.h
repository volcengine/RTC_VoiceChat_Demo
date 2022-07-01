//
//  NetworkingManager.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/16.
//  Copyright © 2021 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NetworkingResponse.h"
#import "NetworkReachabilityManager.h"
#import "BaseUserModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkingManager : NSObject

#pragma mark - Base

+ (void)postWithEventName:(NSString *)eventName
                  content:(NSDictionary *)content
                    block:(void (^ __nullable)(NetworkingResponse *response))block;

#pragma mark - User

/*
 * Change User Name
 * @param userName User Name
 * @param loginToken Login token
 * @param block Callback
 */
+ (void)changeUserName:(NSString *)userName
            loginToken:(NSString *)loginToken
                 block:(void (^ __nullable)(NetworkingResponse *response))block;

#pragma mark -setAppInfo
/*
 * setAppInfo
 * @param appId appId
 * @param appKey appKey
 * @param volcAk volc_ak
 * @param volc_sk volc_sk
 * @param block Callback
 */
+ (void)setAppInfoWithAppId:(NSString *)appId
                     appKey:(NSString *)appKey
                     volcAk:(NSString *)volcAk
                     volcSk:(NSString *)volcSk
              volcAccountID:(NSString *)volcAccountID
                   vodSpace:(NSString *)vodSpace
                      block:(void (^ __nullable)(NetworkingResponse *response))block;

#pragma mark - RTM


/*
 * Join RTM
 * @param scenes Scenes name
 * @param loginToken Login token
 * @param block Callback
 */
+ (void)joinRTM:(NSString *)scenes
     loginToken:(NSString *)loginToken
          block:(void (^ __nullable)(NSString * _Nullable appID,
                                     NSString * _Nullable RTMToken,
                                     NSString * _Nullable serverUrl,
                                     NSString * _Nullable serverSig,
                                     NetworkingResponse *response))block;

@end

NS_ASSUME_NONNULL_END
