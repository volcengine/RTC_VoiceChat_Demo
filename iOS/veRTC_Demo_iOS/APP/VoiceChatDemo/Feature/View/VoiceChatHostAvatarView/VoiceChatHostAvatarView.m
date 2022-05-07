//
//  VoiceChatHostAvatarView.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/29.
//  Copyright © 2021 . All rights reserved.
//

#import "VoiceChatHostAvatarView.h"
#import "GCDTimer.h"

@interface VoiceChatHostAvatarView ()

@property (nonatomic, strong) UIView *animationView;
@property (nonatomic, strong) UILabel *avatarLabel;
@property (nonatomic, strong) UIImageView *avatarBgImageView;
@property (nonatomic, strong) UILabel *userNameLabel;
@property (nonatomic, strong) UIImageView *centerImageView;
@property (nonatomic, strong) UIView *maskView;
@property (nonatomic, strong) GCDTimer *timer;
@property (nonatomic, strong) NSNumber *volume;
@end

@implementation VoiceChatHostAvatarView

- (instancetype)init {
    self = [super init];
    if (self) {
        [self addSubviewAndConstraints];
        __weak __typeof(self) wself = self;
        [self.timer startTimerWithSpace:0.6 block:^(BOOL result) {
            [wself timerMethod];
        }];
    }
    return self;
}

- (void)setUserModel:(VoiceChatUserModel *)userModel {
    _userModel = userModel;
    if (userModel) {
        if (userModel.mic) {
            if (NOEmptyStr(userModel.name) && userModel.name.length > 0) {
                self.avatarLabel.text = [userModel.name substringToIndex:1];
            }
            self.centerImageView.hidden = YES;
            self.maskView.hidden = YES;
            self.avatarBgImageView.hidden = NO;
            if (userModel.isSpeak) {
                self.animationView.hidden = NO;
                self.avatarBgImageView.image = [UIImage imageNamed:@"voicechat_border_small" bundleName:HomeBundleName];
            } else {
                self.avatarBgImageView.image = [UIImage imageNamed:@"voicechat_small_bg" bundleName:HomeBundleName];
                self.animationView.hidden = YES;
            }
        } else {
            self.animationView.hidden = YES;
            self.avatarLabel.text = @"";
            self.centerImageView.hidden = NO;
            self.maskView.hidden = NO;
            self.avatarBgImageView.hidden = YES;
        }
        self.userNameLabel.text = userModel.name;
    }
}

- (void)updateHostVolume:(NSNumber *)volume {
    _volume = volume;
}

- (void)updateHostMic:(UserMic)userMic {
    VoiceChatUserModel *tempModel = self.userModel;
    tempModel.mic = userMic;
    self.userModel = tempModel;
}

#pragma mark - Private Action

- (void)timerMethod {
    VoiceChatUserModel *tempModel = self.userModel;
    tempModel.volume = _volume.floatValue;
    self.userModel = tempModel;
}

- (void)addSubviewAndConstraints {
    [self addSubview:self.animationView];
    [self addSubview:self.avatarBgImageView];
    [self addSubview:self.avatarLabel];
    [self addSubview:self.userNameLabel];
    [self addSubview:self.maskView];
    [self addSubview:self.centerImageView];
    
    [self.avatarBgImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(60, 60));
        make.top.mas_equalTo(7);
        make.centerX.equalTo(self);
    }];
    
    [self.maskView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.avatarBgImageView);
    }];
    
    [self.animationView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.avatarBgImageView);
        make.width.height.mas_equalTo(74);
    }];
    
    [self.avatarLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.center.equalTo(self.avatarBgImageView);
    }];
    
    [self.userNameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.bottom.equalTo(self);
    }];
    
    [self.centerImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(20, 20));
        make.center.equalTo(self.avatarBgImageView);
    }];
}

- (void)addWiggleAnimation:(UIView *)view {
    CAKeyframeAnimation *animation = [CAKeyframeAnimation animationWithKeyPath:@"transform.scale"];
    animation.values = @[@(0.81), @(1.0), @(1.0)];
    animation.keyTimes = @[@(0), @(0.27), @(1.0)];
    
    CAKeyframeAnimation *animation2 = [CAKeyframeAnimation animationWithKeyPath:@"opacity"];
    animation2.values = @[@(0), @(0.2), @(0.4), @(0.2)];
    animation2.keyTimes = @[@(0), @(0.27), @(0.27), @(1.0)];
    
    CAAnimationGroup *group = [CAAnimationGroup animation];
    group.animations = @[animation,animation2];
    group.duration = 1.1;
    group.repeatCount = MAXFLOAT;
    group.removedOnCompletion = NO;
    group.fillMode = kCAFillModeForwards;
    [view.layer addAnimation:group forKey:@"transformKey"];
}

#pragma mark - getter

- (UIView *)animationView {
    if (!_animationView) {
        _animationView = [[UIView alloc] init];
        _animationView.backgroundColor = [UIColor colorFromRGBHexString:@"#F93D89"];
        _animationView.layer.cornerRadius = 74 / 2;
        _animationView.layer.masksToBounds = YES;
        _animationView.hidden = YES;
        [self addWiggleAnimation:_animationView];
    }
    return _animationView;
}

- (UIImageView *)avatarBgImageView {
    if (!_avatarBgImageView) {
        _avatarBgImageView = [[UIImageView alloc] init];
        _avatarBgImageView.image = [UIImage imageNamed:@"voicechat_small_bg" bundleName:HomeBundleName];
        _avatarBgImageView.hidden = YES;
    }
    return _avatarBgImageView;
}

- (UILabel *)avatarLabel {
    if (!_avatarLabel) {
        _avatarLabel = [[UILabel alloc] init];
        _avatarLabel.textColor = [UIColor colorFromHexString:@"#FFFFFF"];
        _avatarLabel.font = [UIFont systemFontOfSize:24];
    }
    return _avatarLabel;
}

- (UILabel *)userNameLabel {
    if (!_userNameLabel) {
        _userNameLabel = [[UILabel alloc] init];
        _userNameLabel.textColor = [UIColor colorFromHexString:@"#F2F3F5"];
        _userNameLabel.font = [UIFont systemFontOfSize:12];
    }
    return _userNameLabel;
}

- (UIView *)maskView {
    if (!_maskView) {
        _maskView = [[UIView alloc] init];
        _maskView.backgroundColor = [UIColor colorFromRGBHexString:@"#040404" andAlpha:0.8 * 255];
        _maskView.layer.cornerRadius = 60 / 2;
        _maskView.layer.masksToBounds = YES;
        _maskView.hidden = YES;
    }
    return _maskView;
}

- (UIImageView *)centerImageView {
    if (!_centerImageView) {
        _centerImageView = [[UIImageView alloc] init];
        _centerImageView.image = [UIImage imageNamed:@"voicechat_seat_mic" bundleName:HomeBundleName];
        _centerImageView.hidden = YES;
    }
    return _centerImageView;
}

- (GCDTimer *)timer {
    if (!_timer) {
        _timer = [[GCDTimer alloc] init];
    }
    return _timer;
}

@end
