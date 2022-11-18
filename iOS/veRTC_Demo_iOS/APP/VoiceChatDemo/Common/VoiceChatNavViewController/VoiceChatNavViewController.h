//
//  VoiceChatNavViewController.h
//  quickstart
//
//  Created by on 2021/3/22.
//  
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatNavViewController : UIViewController

@property (nonatomic, copy) NSString *navTitle;

@property (nonatomic, strong) UIView *navView;

@property (nonatomic, strong) UIView *bgView;

@property (nonatomic, copy) NSString *rightTitle;

@property (nonatomic, strong) BaseButton *rightButton;

@property (nonatomic, strong) BaseButton *leftButton;

- (void)rightButtonAction:(BaseButton *)sender;

@end

NS_ASSUME_NONNULL_END
