//
//  PhonePrivacyView.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/8/17.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface PhonePrivacyView : UIView

@property (nonatomic, assign, readonly) BOOL isAgree;

@property (nonatomic, copy) void (^changeAgree)(BOOL isAgree);

@end

NS_ASSUME_NONNULL_END
