//
//  BaseHomeDemo.h
//  AFNetworking
//
//  Created by on 2022/4/25.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface BaseHomeDemo : NSObject

- (void)pushDemoViewControllerBlock:(void (^)(BOOL result))block;

@end

NS_ASSUME_NONNULL_END
