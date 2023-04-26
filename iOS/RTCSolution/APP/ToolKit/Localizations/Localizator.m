// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "Localizator.h"

static NSString *const Chinese_Simple = @"zh-Hans";
static NSString *const English_US = @"en";
static NSString *const LocalizedFalse = @"Localized_Parsing_Failed_Key";

@interface Localizator ()

@end

@implementation Localizator

#pragma mark - Publish Action

+ (NSString *)localizedStringForKey:(NSString *)key bundleName:(NSString *)bundleName {
    // 获取组件的 Localizable.bundle 路径
    NSString *bundlePath = @"";
    if (bundleName == nil || bundleName.length == 0) {
        bundleName = @"Localizable";
        bundlePath = [[NSBundle mainBundle] pathForResource:bundleName ofType:@"bundle"];
    } else {
        bundlePath = [[[NSBundle mainBundle] pathForResource:bundleName ofType:@"bundle"] stringByAppendingPathComponent:@"Localizable.bundle"];
    }
    
    // 获取 Localizable 中对应语言的路径
    NSString *curLanguage = [Localizator getCurrentLanguage];
    NSString *lprojPath = [[NSBundle bundleWithPath:bundlePath] pathForResource:curLanguage ofType:@"lproj"];
    if (IsEmptyStr(lprojPath)) {
        lprojPath = [[NSBundle bundleWithPath:bundlePath] pathForResource:Chinese_Simple ofType:@"lproj"];
    }
    NSBundle *resourceBundle = [NSBundle bundleWithPath:lprojPath];
    
    // 根据 key 获取 value
    NSString *valueString = NSLocalizedStringWithDefaultValue(key, @"Localizable", resourceBundle, LocalizedFalse, @"");
    
    // 解析失败后，到 ToolKit 组件中再查找一次
    if ([valueString isEqualToString:LocalizedFalse]) {
        valueString = [Localizator localizedToolKitStringForKey:key];
    }
    return valueString;
}

+ (NSString *)getLanguageKey {
    return [Localizator localizedToolKitStringForKey:@"language_code"];
}

#pragma mark - Private Action

+ (NSString *)localizedToolKitStringForKey:(NSString *)key {
    NSString *bundlePath = [[[NSBundle mainBundle] pathForResource:@"ToolKit" ofType:@"bundle"] stringByAppendingPathComponent:@"Localizable.bundle"];
    NSString *curLanguage = [Localizator getCurrentLanguage];
    NSString *lprojPath = [[NSBundle bundleWithPath:bundlePath] pathForResource:curLanguage ofType:@"lproj"];
    if (IsEmptyStr(lprojPath)) {
        lprojPath = [[NSBundle bundleWithPath:bundlePath] pathForResource:Chinese_Simple ofType:@"lproj"];
    }
    NSBundle *resourceBundle = [NSBundle bundleWithPath:lprojPath];
    NSString *valueString = [resourceBundle localizedStringForKey:key value:@"" table:nil];
    return valueString;
}

+ (NSString *)getCurrentLanguage {
    // 获取默认语言为英文
    // Get the default language to English.
    NSString *curLanguage = @"";
    NSString *systemLanguage = [Localizator getSystemLanguage];
    if ([systemLanguage hasPrefix:Chinese_Simple]) {
        curLanguage = Chinese_Simple;
    } else if ([systemLanguage hasPrefix:English_US]) {
        curLanguage = English_US;
    } else {
        curLanguage = English_US;
    }
    return curLanguage;
}

+ (NSString *)getSystemLanguage {
    // 获取系统设置语言
    // Get system language settings
    NSArray *preferredLanguages = [NSLocale preferredLanguages];
    NSString *currLanguage = @"";
    if (preferredLanguages.count > 0) {
        currLanguage = preferredLanguages[0];
    }
    return currLanguage;
}

@end
