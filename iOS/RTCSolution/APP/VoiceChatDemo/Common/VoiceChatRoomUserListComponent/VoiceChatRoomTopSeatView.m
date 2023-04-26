// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "VoiceChatRoomTopSeatView.h"

@interface VoiceChatRoomTopSeatView ()

@property (nonatomic, strong) UILabel *messageLabel;
@property (nonatomic, strong) UISwitch *switchView;
@property (nonatomic, strong) UIView *lineView;

@end


@implementation VoiceChatRoomTopSeatView

- (instancetype)init {
    self = [super init];
    if (self) {
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateSeatSwitch:) name:NotificationUpdateSeatSwitch object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(resultSeatSwitch) name:NotificationResultSeatSwitch object:nil];
        
        self.backgroundColor = [UIColor colorFromRGBHexString:@"#0E0825" andAlpha:0.95 * 255];
        
        [self addSubview:self.messageLabel];
        [self.messageLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.equalTo(self);
            make.left.mas_equalTo(16);
        }];
        
        [self addSubview:self.switchView];
        [self.switchView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.equalTo(self);
            make.left.equalTo(self.messageLabel.mas_right).offset(13);
        }];
        
        [self addSubview:self.lineView];
        [self.lineView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.equalTo(self);
            make.height.mas_equalTo(1);
            make.bottom.mas_equalTo(-1);
        }];
    }
    return self;
}

- (void)switchViewChanged:(UISwitch *)sender {
    sender.userInteractionEnabled = NO;
    if (self.clickSwitchBlock) {
        self.clickSwitchBlock(sender.on);
    }
}

- (void)updateSeatSwitch:(NSNotification *)notification {
    if ([notification.object isKindOfClass:[NSNumber class]]) {
        NSNumber *number = (NSNumber *)notification.object;
        [self.switchView setOn:number.boolValue animated:YES];
    }
}

- (void)resultSeatSwitch {
    self.switchView.userInteractionEnabled = YES;
}

#pragma mark - Getter

- (UILabel *)messageLabel {
    if (!_messageLabel) {
        _messageLabel = [[UILabel alloc] init];
        _messageLabel.text = LocalizedString(@"visitor_connection_application");
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

- (UIView *)lineView {
    if (!_lineView) {
        _lineView = [[UIView alloc] init];
        _lineView.backgroundColor = [UIColor colorFromRGBHexString:@"#2A2441"];
    }
    return _lineView;
}

@end
