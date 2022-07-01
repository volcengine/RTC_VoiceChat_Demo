//
//  BaseUserModel.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/6/28.
//  Copyright © 2021 . All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface BaseUserModel : NSObject <NSSecureCoding>

@property (nonatomic, copy) NSString *uid;

@property (nonatomic, copy) NSString *loginToken;

@property (nonatomic, copy) NSString *name;

@end

NS_ASSUME_NONNULL_END
