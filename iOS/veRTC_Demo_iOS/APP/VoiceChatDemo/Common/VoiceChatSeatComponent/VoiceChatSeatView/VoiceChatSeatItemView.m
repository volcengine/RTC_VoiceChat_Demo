//
//  VoiceChatSeatItemView.m
//  veRTC_Demo
//
//  Created by on 2021/11/29.
//  
//

#import "VoiceChatSeatItemView.h"

typedef NS_ENUM(NSInteger, VoiceChatSeatItemStatue) {
    VoiceChatSeatItemStatueNull = 0,
    VoiceChatSeatItemStatueLock,
    VoiceChatSeatItemStatueUser,
    VoiceChatSeatItemStatueUserAndSpeak,
    VoiceChatSeatItemStatueMuteMic,
};

@interface VoiceChatSeatItemView ()

@property (nonatomic, strong) UIView *animationView;
@property (nonatomic, strong) UILabel *avatarLabel;
@property (nonatomic, strong) UIImageView *avatarBgImageView;
@property (nonatomic, strong) UILabel *userNameLabel;
@property (nonatomic, strong) UIImageView *centerImageView;
@property (nonatomic, strong) UIView *maskView;

@end

@implementation VoiceChatSeatItemView

- (instancetype)init {
    self = [super init];
    if (self) {
        [self addSubview:self.animationView];
        [self addSubview:self.avatarBgImageView];
        [self addSubview:self.avatarLabel];
        [self addSubview:self.userNameLabel];
        [self addSubview:self.maskView];
        [self addSubview:self.centerImageView];
        
        [self.avatarBgImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(52, 52));
            make.top.mas_equalTo(5);
            make.centerX.equalTo(self);
        }];
        
        [self.maskView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self.avatarBgImageView);
        }];
        
        [self.animationView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerX.equalTo(self.avatarBgImageView);
            make.width.height.mas_equalTo(62);
        }];
        
        [self.avatarLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.center.equalTo(self.avatarBgImageView);
        }];
        
        [self.userNameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerX.bottom.equalTo(self);
            make.width.lessThanOrEqualTo(self.mas_width);
        }];
        
        [self.centerImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(20, 20));
            make.center.equalTo(self.avatarBgImageView);
        }];
        
        self.userInteractionEnabled = YES;
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction)];
        [self addGestureRecognizer:tap];
    }
    return self;
}

- (void)setSeatModel:(VoiceChatSeatModel *)seatModel {
    _seatModel = seatModel;
    if (seatModel) {
        if (seatModel.status == 1) {
            //unlock
            if (NOEmptyStr(seatModel.userModel.uid)) {
                if (seatModel.userModel.mic == UserMicOn) {
                    if (seatModel.userModel.isSpeak) {
                        [self updateUI:VoiceChatSeatItemStatueUserAndSpeak seatModel:seatModel];
                    } else {
                        [self updateUI:VoiceChatSeatItemStatueUser seatModel:seatModel];
                    }
                } else {
                    [self updateUI:VoiceChatSeatItemStatueMuteMic seatModel:seatModel];
                }
            } else {
                [self updateUI:VoiceChatSeatItemStatueNull seatModel:seatModel];
            }
        } else {
            //lock
            [self updateUI:VoiceChatSeatItemStatueLock seatModel:seatModel];
        }
    } else {
        [self updateUI:VoiceChatSeatItemStatueNull seatModel:seatModel];
    }
}

- (void)updateUI:(VoiceChatSeatItemStatue)statue
       seatModel:(VoiceChatSeatModel *)seatModel {
    self.animationView.hidden = YES;
    self.maskView.hidden = YES;
    
    if (statue == VoiceChatSeatItemStatueNull) {
        self.avatarBgImageView.image = nil;
        self.avatarBgImageView.backgroundColor = [UIColor colorFromRGBHexString:@"#FFFFFF" andAlpha:0.3 * 255];
        self.centerImageView.image = [UIImage imageNamed:@"voicechat_seat_null" bundleName:HomeBundleName];
        self.centerImageView.hidden = NO;
        self.avatarLabel.text = @"";
        self.userNameLabel.text = [NSString stringWithFormat:@"%ld号麦位", (long)(seatModel.index)];
    } else if (statue == VoiceChatSeatItemStatueLock) {
        self.avatarBgImageView.image = nil;
        self.avatarBgImageView.backgroundColor = [UIColor colorFromRGBHexString:@"#FFFFFF" andAlpha:0.3 * 255];
        self.centerImageView.image = [UIImage imageNamed:@"voicechat_seat_lock" bundleName:HomeBundleName];
        self.centerImageView.hidden = NO;
        self.avatarLabel.text = @"";
        self.userNameLabel.text = [NSString stringWithFormat:@"%ld号麦位", (long)(seatModel.index)];
    } else if (statue == VoiceChatSeatItemStatueUser) {
        self.avatarBgImageView.backgroundColor = [UIColor clearColor];
        self.avatarBgImageView.image = [UIImage imageNamed:@"voicechat_small_bg" bundleName:HomeBundleName];
        self.avatarLabel.text = [seatModel.userModel.name substringToIndex:1];
        self.centerImageView.hidden = YES;
        self.userNameLabel.text = seatModel.userModel.name;
    } else if (statue == VoiceChatSeatItemStatueUserAndSpeak) {
        self.avatarBgImageView.backgroundColor = [UIColor clearColor];
        self.avatarBgImageView.image = [UIImage imageNamed:@"voicechat_border_small" bundleName:HomeBundleName];
        self.avatarLabel.text = [seatModel.userModel.name substringToIndex:1];
        self.centerImageView.hidden = YES;
        self.userNameLabel.text = seatModel.userModel.name;
        
        self.animationView.hidden = NO;
    } else if (statue == VoiceChatSeatItemStatueMuteMic) {
        self.avatarBgImageView.backgroundColor = [UIColor clearColor];
        self.avatarBgImageView.image = [UIImage imageNamed:@"voicechat_small_bg" bundleName:HomeBundleName];
        self.centerImageView.hidden = YES;
        self.avatarLabel.text = [seatModel.userModel.name substringToIndex:1];
        self.userNameLabel.text = seatModel.userModel.name;
        self.centerImageView.image = [UIImage imageNamed:@"voicechat_seat_mic" bundleName:HomeBundleName];
        self.centerImageView.hidden = NO;
        self.maskView.hidden = NO;
    } else {
        //error
    }
}

- (void)tapAction {
    if (self.clickBlock) {
        self.clickBlock(self.seatModel);
    }
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
        _animationView.layer.cornerRadius = 62 / 2;
        _animationView.layer.masksToBounds = YES;
        _animationView.hidden = YES;
        [self addWiggleAnimation:_animationView];
    }
    return _animationView;
}

- (UIImageView *)avatarBgImageView {
    if (!_avatarBgImageView) {
        _avatarBgImageView = [[UIImageView alloc] init];
        _avatarBgImageView.layer.cornerRadius = 52 / 2;
        _avatarBgImageView.layer.masksToBounds = YES;
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
        _maskView.layer.cornerRadius = 52 / 2;
        _maskView.layer.masksToBounds = YES;
        _maskView.hidden = YES;
    }
    return _maskView;
}

- (UIImageView *)centerImageView {
    if (!_centerImageView) {
        _centerImageView = [[UIImageView alloc] init];
    }
    return _centerImageView;
}

@end
