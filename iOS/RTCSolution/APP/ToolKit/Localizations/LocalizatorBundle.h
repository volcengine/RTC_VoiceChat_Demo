// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

#define LocalizedStringFromBundle(key, bundle) \
        [LocalizatorBundle localizedStringForKey:(key) bundleName:bundle]

NS_ASSUME_NONNULL_BEGIN

@interface LocalizatorBundle : NSObject

+ (NSString *)localizedStringForKey:(NSString *)key bundleName:(nullable NSString *)bundleName;

@end

NS_ASSUME_NONNULL_END
