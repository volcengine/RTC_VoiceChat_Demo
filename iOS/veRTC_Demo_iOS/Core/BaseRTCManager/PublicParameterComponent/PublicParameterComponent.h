//
//  PublicParameterComponent.h
//  veRTC_Demo
//
//  Created by on 2021/7/2.
//  
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface PublicParameterComponent : NSObject

@property (nonatomic, copy) NSString *appId;

@property (nonatomic, copy) NSString *roomId;

+ (PublicParameterComponent *)share;

+ (void)clear;

@end

NS_ASSUME_NONNULL_END
