//
//  UIDevice+idfvCheck.m
//  veRTC_Demo
//
//  Created by   on 2022/8/3.
//  Copyright © 2022  . All rights reserved.
//

#import "UIDevice+idfvCheck.h"
#import <objc/runtime.h>

#ifdef DEBUG

@implementation UIDevice (idfvCheck)

+ (void)load {
    static dispatch_once_t oncetoken;
    dispatch_once(&oncetoken, ^{
        SEL originalSelector = @selector(identifierForVendor);
        SEL swizzledSelector = @selector(hook_identifierForVendor);
        Method originalMethod = class_getInstanceMethod([self class], originalSelector);
        Method swizzledMehod = class_getInstanceMethod([self class], swizzledSelector);
        BOOL success = class_addMethod([self class], originalSelector, method_getImplementation(swizzledMehod), method_getTypeEncoding(swizzledMehod));
        if (success) {
            class_replaceMethod([self class], swizzledSelector, method_getImplementation(originalMethod), method_getTypeEncoding(originalMethod));
        } else {
            method_exchangeImplementations(originalMethod, swizzledMehod);
        }
    });
}

- (NSUUID *)hook_identifierForVendor {
    NSLog(@"warning:use identifierForVendor.");
    return [self hook_identifierForVendor];
}

@end

#endif
