//
//  NetworkingManager.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/16.
//  Copyright © 2021 bytedance. All rights reserved.
//

#import <Core/NetworkingManager.h>

NS_ASSUME_NONNULL_BEGIN

@interface NetworkingManager (Login)

#pragma mark - Login

/*
 * Password Free Login
 * @param userName User Name
 * @param block Callback
 */
+ (void)passwordFreeLogin:(NSString *)userName
                    block:(void (^ __nullable)(BaseUserModel * _Nullable userModel,
                                               NSString * _Nullable loginToken,
                                               NetworkingResponse *response))block;

@end

NS_ASSUME_NONNULL_END
