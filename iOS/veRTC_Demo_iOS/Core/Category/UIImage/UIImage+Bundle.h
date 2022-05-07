//
//  UIImage+Bundle.h
//  veRTCDemo
//
//  Created by bytedance on 2022/4/2.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIImage (Bundle)

+ (nullable UIImage *)imageNamed:(NSString *)name
                      bundleName:(NSString *)bundle;

@end

NS_ASSUME_NONNULL_END
