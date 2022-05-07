//
//  VoiceChatSelectBgView.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/26.
//  Copyright © 2021 . All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatSelectBgView : UIView

@property (nonatomic, copy) void (^clickBlock)(NSString *imageName,
                                               NSString *smallImageName);

- (NSString *)getDefaults;

@end

NS_ASSUME_NONNULL_END
