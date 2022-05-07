//
//  VoiceChatMusicView.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/30.
//  Copyright © 2021 . All rights reserved.
//

#import "VoiceChatMusicView.h"
#import "VoiceChatRTCManager.h"

@interface VoiceChatMusicView ()

@property (nonatomic, strong) UILabel *messageLabel;
@property (nonatomic, strong) UISwitch *switchView;
@property (nonatomic, strong) UIView *lineView;
@property (nonatomic, strong) UILabel *musicLeftLabel;
@property (nonatomic, strong) UILabel *musicRightLabel;
@property (nonatomic, strong) UISlider *musicSlider;
@property (nonatomic, strong) UILabel *vocalLeftLabel;
@property (nonatomic, strong) UILabel *vocalRightLabel;
@property (nonatomic, strong) UISlider *vocalSlider;

@property (nonatomic, assign) BOOL musicIsOpen;


@end


@implementation VoiceChatMusicView

- (instancetype)init {
    self = [super init];
    if (self) {
        self.backgroundColor = [UIColor colorFromRGBHexString:@"#0E0825" andAlpha:0.95 * 255];
        
        [self addSubview:self.messageLabel];
        [self.messageLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(12);
            make.left.mas_equalTo(16);
        }];
        
        [self addSubview:self.switchView];
        [self.switchView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.equalTo(self.messageLabel);
            make.left.equalTo(self.messageLabel.mas_right).offset(12);
        }];
        
        [self addSubview:self.lineView];
        [self.lineView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.equalTo(self);
            make.height.mas_equalTo(1);
            make.top.mas_equalTo(48);
        }];
        
        [self addSubview:self.musicLeftLabel];
        [self.musicLeftLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.messageLabel.mas_bottom).offset(28);
            make.left.mas_equalTo(16);
        }];

        [self addSubview:self.musicRightLabel];
        [self.musicRightLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.equalTo(self.musicLeftLabel);
            make.right.mas_equalTo(-16);
        }];

        [self addSubview:self.musicSlider];
        [self.musicSlider mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.musicLeftLabel.mas_bottom).offset(8);
            make.left.mas_equalTo(16);
            make.right.mas_equalTo(-16);
        }];
        
        [self addSubview:self.vocalLeftLabel];
        [self.vocalLeftLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.musicLeftLabel.mas_bottom).offset(48);
            make.left.mas_equalTo(16);
        }];

        [self addSubview:self.vocalRightLabel];
        [self.vocalRightLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.equalTo(self.vocalLeftLabel);
            make.right.mas_equalTo(-16);
        }];

        [self addSubview:self.vocalSlider];
        [self.vocalSlider mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.vocalLeftLabel.mas_bottom).offset(8);
            make.left.mas_equalTo(16);
            make.right.mas_equalTo(-16);
        }];
        
        _musicIsOpen = NO;
        [self enableMusicVolume:NO];
    }
    return self;
}

- (void)switchViewChanged:(UISwitch *)sender {
    if (sender.on) {
        if (_musicIsOpen) {
            [[VoiceChatRTCManager shareRtc] resumeBackgroundMusic];
        } else {
            NSString *path = [[NSBundle mainBundle] pathForResource:@"voicechat_bg"
                                                             ofType:@"mp3"];
            _musicIsOpen = YES;
            [[VoiceChatRTCManager shareRtc] startBackgroundMusic:path];
        }
        [self enableMusicVolume:YES];
    } else {
        [[VoiceChatRTCManager shareRtc] pauseBackgroundMusic];
        [self enableMusicVolume:NO];
    }
}

- (void)enableMusicVolume:(BOOL)isEnable {
    CGFloat alpha = 1;
    if (!isEnable) {
        alpha = 0.34;
        self.musicSlider.userInteractionEnabled = NO;
    } else {
        self.musicSlider.userInteractionEnabled = YES;
    }
    self.musicLeftLabel.alpha = alpha;
    self.musicRightLabel.alpha = alpha;
    self.musicSlider.alpha = alpha;
}

- (void)musicSliderValueChanged:(UISlider *)musicSlider {
    [[VoiceChatRTCManager shareRtc] setMusicVolume:musicSlider.value];
    self.musicRightLabel.text = [NSString stringWithFormat:@"%ld", (long)musicSlider.value];
}

- (void)vocalSliderValueChanged:(UISlider *)musicSlider {
    [[VoiceChatRTCManager shareRtc] setRecordingVolume:musicSlider.value];
    self.vocalRightLabel.text = [NSString stringWithFormat:@"%ld", (long)musicSlider.value];
}

#pragma mark - Getter

- (UILabel *)messageLabel {
    if (!_messageLabel) {
        _messageLabel = [[UILabel alloc] init];
        _messageLabel.text = @"背景音乐";
        _messageLabel.font = [UIFont systemFontOfSize:16];
        _messageLabel.textColor = [UIColor whiteColor];
    }
    return _messageLabel;
}

- (UISwitch *)switchView {
    if (!_switchView) {
        _switchView = [[UISwitch alloc] init];
        _switchView.onTintColor = [UIColor colorFromHexString:@"#165DFF"];
        [_switchView addTarget:self action:@selector(switchViewChanged:) forControlEvents:UIControlEventValueChanged];
    }
    return _switchView;
}

- (UILabel *)musicLeftLabel {
    if (!_musicLeftLabel) {
        _musicLeftLabel = [[UILabel alloc] init];
        _musicLeftLabel.text = @"音乐音量";
        _musicLeftLabel.font = [UIFont systemFontOfSize:14];
        _musicLeftLabel.textColor = [UIColor colorFromHexString:@"#E5E6EB"];
    }
    return _musicLeftLabel;
}

- (UILabel *)musicRightLabel {
    if (!_musicRightLabel) {
        _musicRightLabel = [[UILabel alloc] init];
        _musicRightLabel.text = @"100";
        _musicRightLabel.font = [UIFont systemFontOfSize:14];
        _musicRightLabel.textColor = [UIColor colorFromHexString:@"#FFFFFF"];
    }
    return _musicRightLabel;
}

- (UISlider *)musicSlider {
    if (!_musicSlider) {
        _musicSlider = [[UISlider alloc] init];
        [_musicSlider setTintColor:[UIColor colorFromHexString:@"#4080FF"]];
        [_musicSlider addTarget:self action:@selector(musicSliderValueChanged:) forControlEvents:UIControlEventValueChanged];
        _musicSlider.minimumValue = 0;
        _musicSlider.maximumValue = 100;
        _musicSlider.value = 100;
    }
    return _musicSlider;
}

- (UILabel *)vocalLeftLabel {
    if (!_vocalLeftLabel) {
        _vocalLeftLabel = [[UILabel alloc] init];
        _vocalLeftLabel.text = @"人声音量";
        _vocalLeftLabel.font = [UIFont systemFontOfSize:14];
        _vocalLeftLabel.textColor = [UIColor colorFromHexString:@"#E5E6EB"];
    }
    return _vocalLeftLabel;
}

- (UILabel *)vocalRightLabel {
    if (!_vocalRightLabel) {
        _vocalRightLabel = [[UILabel alloc] init];
        _vocalRightLabel.text = @"100";
        _vocalRightLabel.font = [UIFont systemFontOfSize:14];
        _vocalRightLabel.textColor = [UIColor colorFromHexString:@"#FFFFFF"];
    }
    return _vocalRightLabel;
}

- (UISlider *)vocalSlider {
    if (!_vocalSlider) {
        _vocalSlider = [[UISlider alloc] init];
        [_vocalSlider setTintColor:[UIColor colorFromHexString:@"#4080FF"]];
        [_vocalSlider addTarget:self action:@selector(vocalSliderValueChanged:) forControlEvents:UIControlEventValueChanged];
        _vocalSlider.minimumValue = 0;
        _vocalSlider.maximumValue = 100;
        _vocalSlider.value = 100;
    }
    return _vocalSlider;
}

- (UIView *)lineView {
    if (!_lineView) {
        _lineView = [[UIView alloc] init];
        _lineView.backgroundColor = [UIColor colorFromRGBHexString:@"#2A2441"];
    }
    return _lineView;
}


@end
