// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "LocalizatorBundle.h"
#import "Localizator.h"

@implementation LocalizatorBundle

+ (NSString *)localizedStringForKey:(NSString *)key bundleName:(nullable NSString *)bundleName {
    return [Localizator localizedStringForKey:key bundleName:bundleName];
}

@end
