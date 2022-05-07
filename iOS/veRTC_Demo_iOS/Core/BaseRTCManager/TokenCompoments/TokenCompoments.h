//
//  TokenCompoments.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/7/2.
//  Copyright © 2021 . All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface TokenCompoments : NSObject

+ (void)updateToken:(NSString *)token;

+ (NSString *)token;

+ (NSDictionary *)addTokenToParams:(NSDictionary * _Nullable)dic;

@end

NS_ASSUME_NONNULL_END
