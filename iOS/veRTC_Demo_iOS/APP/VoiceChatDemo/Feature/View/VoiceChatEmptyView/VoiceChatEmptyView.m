//
//  VoiceChatEmptyView.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/3.
//  Copyright © 2021 . All rights reserved.
//

#import "VoiceChatEmptyView.h"

@interface VoiceChatEmptyView ()

@property (nonatomic, strong) UIImageView *iconImageView;
@property (nonatomic, strong) UILabel *messageLabel;

@end

@implementation VoiceChatEmptyView

- (instancetype)initWithView:(UIView *)view message:(NSString *)message {
    self = [super init];
    if (self) {
        [view addSubview:self.iconImageView];
        [self.iconImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(266, 109));
            make.centerX.equalTo(view);
            make.centerY.equalTo(view).offset(-35);
        }];
        
        [view addSubview:self.messageLabel];
        [self.messageLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.iconImageView.mas_bottom).offset(12);
            make.centerX.equalTo(view);
        }];
        
        self.messageLabel.text = message;
        [self dismiss];
    }
    return self;
}

- (void)show {
    self.messageLabel.hidden = NO;
    self.iconImageView.hidden = NO;
}

- (void)dismiss {
    self.messageLabel.hidden = YES;
    self.iconImageView.hidden = YES;
}

#pragma mark - Getter

- (UIImageView *)iconImageView {
    if (!_iconImageView) {
        _iconImageView = [[UIImageView alloc] init];
        _iconImageView.image = [UIImage imageNamed:@"voicechar_empty" bundleName:HomeBundleName];
    }
    return _iconImageView;
}

- (UILabel *)messageLabel {
    if (!_messageLabel) {
        _messageLabel = [[UILabel alloc] init];
        _messageLabel.font = [UIFont systemFontOfSize:12];
        _messageLabel.textColor = [UIColor colorFromHexString:@"#D3C6C6"];
    }
    return _messageLabel;
}


@end
