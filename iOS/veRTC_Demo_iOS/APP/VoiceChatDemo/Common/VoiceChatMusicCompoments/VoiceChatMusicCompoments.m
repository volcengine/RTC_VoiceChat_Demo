//
//  VoiceChatMusicCompoments.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/30.
//  Copyright © 2021 . All rights reserved.
//

#import "VoiceChatMusicCompoments.h"
#import "VoiceChatMusicView.h"

@interface VoiceChatMusicCompoments ()

@property (nonatomic, strong) UIButton *maskButton;

@end

@implementation VoiceChatMusicCompoments

#pragma mark - Publish Action

- (instancetype)init {
    self = [super init];
    if (self) {
        UIViewController *rootVC = [DeviceInforTool topViewController];
        [rootVC.view addSubview:self.maskButton];
        [self.maskButton mas_makeConstraints:^(MASConstraintMaker *make) {
            make.width.left.height.equalTo(rootVC.view);
            make.top.equalTo(rootVC.view).offset(SCREEN_HEIGHT);
        }];
        
        VoiceChatMusicView *musicView = [[VoiceChatMusicView alloc] init];
        [self.maskButton addSubview:musicView];
        [musicView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.bottom.equalTo(self.maskButton);
            make.height.mas_equalTo(196 + [DeviceInforTool getVirtualHomeHeight]);
        }];
    }
    return self;
}

- (void)show {
    self.maskButton.hidden = NO;
    
    // Start animation
    [self.maskButton.superview layoutIfNeeded];
    [self.maskButton.superview setNeedsUpdateConstraints];
    [UIView animateWithDuration:0.25
                     animations:^{
        [self.maskButton mas_updateConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.maskButton.superview).offset(0);
        }];
        [self.maskButton.superview layoutIfNeeded];
    }];
}

#pragma mark - Private Action

- (void)maskButtonAction {
    self.maskButton.hidden = YES;
    [self.maskButton mas_updateConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.maskButton.superview).offset(SCREEN_HEIGHT);
    }];
}

#pragma mark - Getter

- (UIButton *)maskButton {
    if (!_maskButton) {
        _maskButton = [[UIButton alloc] init];
        [_maskButton addTarget:self action:@selector(maskButtonAction) forControlEvents:UIControlEventTouchUpInside];
        [_maskButton setBackgroundColor:[UIColor clearColor]];
    }
    return _maskButton;
}

@end
