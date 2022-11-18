//
//  NetworkingManager.h
//  veRTC_Demo
//
//  Created by on 2021/12/16.
//  
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



@end

NS_ASSUME_NONNULL_END
