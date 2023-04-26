// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "PublicParameterComponent.h"
#import "LocalUserComponent.h"

@implementation PublicParameterComponent

+ (PublicParameterComponent *)share {
    static dispatch_once_t onceToken;
    static PublicParameterComponent *publicParameterComponent;
    dispatch_once(&onceToken, ^{
        publicParameterComponent = [[PublicParameterComponent alloc] init];
    });
    return publicParameterComponent;
}

+ (void)clear {
    [PublicParameterComponent share].appId = @"";
    [PublicParameterComponent share].roomId = @"";
}

@end
