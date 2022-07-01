//
//  PublicParameterCompoments.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/7/2.
//  Copyright © 2021 . All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface PublicParameterCompoments : NSObject

@property (nonatomic, copy) NSString *appId;

@property (nonatomic, copy) NSString *roomId;

+ (PublicParameterCompoments *)share;

+ (NSDictionary *)addTokenToParams:(NSDictionary * _Nullable)dic;

+ (void)clear;

@end

NS_ASSUME_NONNULL_END
