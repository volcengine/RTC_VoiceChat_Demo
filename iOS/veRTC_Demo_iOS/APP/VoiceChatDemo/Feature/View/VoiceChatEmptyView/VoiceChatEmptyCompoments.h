//
//  VoiceChatEmptyView.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/3.
//  Copyright © 2021 . All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatEmptyView : NSObject

- (instancetype)initWithView:(UIView *)view message:(NSString *)message;

- (void)show;

- (void)dismiss;

@end

NS_ASSUME_NONNULL_END
