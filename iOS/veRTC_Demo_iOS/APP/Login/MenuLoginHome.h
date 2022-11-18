//
//  MenuLoginHome.h
//  veLogin-veLogin
//
//  Created by bytedance on 2022/7/29.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface MenuLoginHome : NSObject

+ (void)popLoginVC:(BOOL)isAnimation;

+ (void)logout:(void (^)(BOOL result))block;

@end

NS_ASSUME_NONNULL_END
