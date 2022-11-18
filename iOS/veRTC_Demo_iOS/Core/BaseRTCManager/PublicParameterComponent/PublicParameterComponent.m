//
//  PublicParameterComponent.m
//  veRTC_Demo
//
//  Created by on 2021/7/2.
//  
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
