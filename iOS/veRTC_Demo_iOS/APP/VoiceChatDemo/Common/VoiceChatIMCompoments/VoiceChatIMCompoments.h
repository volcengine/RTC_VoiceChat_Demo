//
//  VoiceChatIMCompoments.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/23.
//  Copyright © 2021 . All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VoiceChatIMModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatIMCompoments : NSObject

- (instancetype)initWithSuperView:(UIView *)superView;

- (void)addIM:(VoiceChatIMModel *)model;

@end

NS_ASSUME_NONNULL_END
