//
//  LoginControlCompoments.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/23.
//  Copyright © 2021 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface LoginControlCompoments : NSObject

// 免密登录
+ (void)passwordFreeLogin:(NSString *)userName
                    block:(void (^ __nullable)(BOOL result, NSString * _Nullable errorStr))block;

@end

NS_ASSUME_NONNULL_END
