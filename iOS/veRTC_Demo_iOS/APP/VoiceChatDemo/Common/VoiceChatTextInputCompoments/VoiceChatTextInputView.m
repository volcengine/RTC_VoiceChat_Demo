//
//  VoiceChatTextInputView.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/11/30.
//  Copyright © 2021 . All rights reserved.
//

#import "VoiceChatTextInputView.h"

@interface VoiceChatTextInputView ()

@property (nonatomic, strong) UIView *maskView;
@property (nonatomic, strong) UIImageView *borderImageView;
@property (nonatomic, strong) UITextField *textField;
@property (nonatomic, strong) BaseButton *senderButton;

@end

@implementation VoiceChatTextInputView

- (instancetype)initWithMessage:(NSString *)message {
    self = [super init];
    if (self) {
        [self addSubview:self.maskView];
        [self.maskView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self);
        }];
        
        [self addSubview:self.senderButton];
        [self.senderButton mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(60, 28));
            make.right.mas_equalTo(-16);
            make.centerY.equalTo(self);
        }];
        
        [self addSubview:self.borderImageView];
        [self.borderImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(8);
            make.right.equalTo(self.senderButton.mas_left).offset(-12);
            make.centerY.equalTo(self);
        }];
        
        [self addSubview:self.textField];
        [self.textField mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.equalTo(self.borderImageView).offset(12);
            make.right.equalTo(self.borderImageView).offset(-12);
            make.height.top.equalTo(self.borderImageView);
        }];
        self.textField.text = message;
    }
    return self;
}

#pragma mark - Publish Action

- (void)show {
    [self.textField becomeFirstResponder];
}

- (void)dismiss:(void (^)(NSString *text))block {
    if (block) {
        block(self.textField.text);
    }
    [self.textField resignFirstResponder];
}

- (void)senderButtonAction {
    if (IsEmptyStr(self.textField.text)) {
        return;
    }
    if (self.clickSenderBlock) {
        self.clickSenderBlock(self.textField.text);
    }
    [self dismiss:^(NSString * _Nonnull text) {
        
    }];
}

#pragma mark - Getter

- (UIView *)maskView {
    if (!_maskView) {
        _maskView = [[UIView alloc] init];
        _maskView.backgroundColor = [UIColor colorFromRGBHexString:@"#0E0825" andAlpha:0.95 * 255];
    }
    return _maskView;
}

- (UIImageView *)borderImageView {
    if (!_borderImageView) {
        _borderImageView = [[UIImageView alloc] init];
        _borderImageView.image = [UIImage imageNamed:@"voicechat_textinput_border" bundleName:HomeBundleName];
    }
    return _borderImageView;
}

- (UITextField *)textField {
    if (!_textField) {
        _textField = [[UITextField alloc] init];
        [_textField setBackgroundColor:[UIColor clearColor]];
        [_textField setTextColor:[UIColor whiteColor]];
        _textField.font = [UIFont systemFontOfSize:14 weight:UIFontWeightRegular];
        NSAttributedString *attrString = [[NSAttributedString alloc]
                                          initWithString:@"说点什么"
                                          attributes:
                                        @{NSForegroundColorAttributeName :
                                        [UIColor colorFromRGBHexString:@"#FFFFFF" andAlpha:0.7 * 255]}];
        _textField.attributedPlaceholder = attrString;
    }
    return _textField;
}

- (BaseButton *)senderButton {
    if (!_senderButton) {
        _senderButton = [[BaseButton alloc] init];
        _senderButton.backgroundColor = [UIColor colorFromHexString:@"#1664FF"];
        [_senderButton setTitle:@"发送" forState:UIControlStateNormal];
        [_senderButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _senderButton.titleLabel.font = [UIFont systemFontOfSize:14];
        [_senderButton addTarget:self action:@selector(senderButtonAction) forControlEvents:UIControlEventTouchUpInside];
        _senderButton.layer.cornerRadius = 14;
        _senderButton.layer.masksToBounds = YES;
    }
    return _senderButton;
}

@end
