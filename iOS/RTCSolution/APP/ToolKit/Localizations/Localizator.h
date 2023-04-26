// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

#define LocalizedString(key) \
[Localizator localizedStringForKey:(key) bundleName:HomeBundleName]
        
@interface Localizator : NSObject

+ (NSString *)localizedStringForKey:(NSString *)key bundleName:(nullable NSString *)bundleName;

+ (NSString *)getLanguageKey;

@end

NS_ASSUME_NONNULL_END
