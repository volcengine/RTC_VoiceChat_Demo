// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "UIImage+Bundle.h"

@implementation UIImage (Bundle)

#pragma mark - Publish Action

+ (nullable UIImage *)imageNamed:(NSString *)name
                      bundleName:(NSString *)bundle {
    if (name == nil || name.length <= 0) {
        return nil;
    }
    NSString *bundlePath = [[NSBundle mainBundle] pathForResource:bundle ofType:@"bundle"];
    NSBundle *resourceBundle = [NSBundle bundleWithPath:bundlePath];
    UIImage *image = [UIImage imageNamed:name
                                inBundle:resourceBundle
           compatibleWithTraitCollection:nil];
    return image;
}

@end
