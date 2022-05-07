//
//  VoiceChatRoomTopSelectView.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/24.
//  Copyright © 2021 . All rights reserved.
//

#import "VoiceChatRoomTopSelectView.h"

@interface VoiceChatRoomTopSelectView ()

@property (nonatomic, strong) BaseButton *onlineButton;
@property (nonatomic, strong) BaseButton *appleButton;
@property (nonatomic, strong) UIView *selectLineView;
@property (nonatomic, strong) UIImageView *redImageView;

@end

@implementation VoiceChatRoomTopSelectView

- (instancetype)init {
    self = [super init];
    if (self) {
        self.backgroundColor = [UIColor colorFromRGBHexString:@"#0E0825" andAlpha:0.95 * 255];
        
        [self addSubview:self.onlineButton];
        [self addSubview:self.appleButton];
        [self addSubview:self.selectLineView];
        
        [self addConstraints];
        [self onlineButtonAction];
    }
    return self;
}

- (void)addConstraints {
    [self.onlineButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.bottom.height.equalTo(self);
        make.width.mas_equalTo(SCREEN_WIDTH / 2);
    }];
    
    [self.appleButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.bottom.height.equalTo(self);
        make.width.mas_equalTo(SCREEN_WIDTH / 2);
    }];
    
    [self.selectLineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(64);
        make.height.mas_equalTo(2);
        make.bottom.mas_equalTo(-2);
        make.centerX.equalTo(self.onlineButton);
    }];
    
    [self addSubview:self.redImageView];
    [self.redImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(10, 10));
        make.top.mas_equalTo(13);
        make.left.equalTo(self.appleButton.mas_centerX).offset(25);
    }];
}

- (void)setTitleStr:(NSString *)titleStr {
    _titleStr = titleStr;
    if (NOEmptyStr(titleStr)) {
        self.onlineButton.hidden = YES;
        self.appleButton.hidden = YES;
        self.selectLineView.hidden = YES;
    } else {
        self.onlineButton.hidden = NO;
        self.appleButton.hidden = NO;
        self.selectLineView.hidden = NO;
    }
}

- (void)onlineButtonAction {
    [self.onlineButton setTitleColor:[UIColor colorFromHexString:@"#4080FF"] forState:UIControlStateNormal];
    [self.appleButton setTitleColor:[UIColor colorFromHexString:@"#E5E6EB"] forState:UIControlStateNormal];
    
    [self.selectLineView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(64);
        make.height.mas_equalTo(2);
        make.bottom.mas_equalTo(-10);
        make.centerX.equalTo(self.onlineButton);
    }];
    
    if ([self.delegate respondsToSelector:@selector(VoiceChatRoomTopSelectView:clickSwitchItem:)]) {
        [self.delegate VoiceChatRoomTopSelectView:self clickSwitchItem:NO];
    }
}

- (void)appleButtonAction {
    [self.onlineButton setTitleColor:[UIColor colorFromHexString:@"#E5E6EB"] forState:UIControlStateNormal];
    [self.appleButton setTitleColor:[UIColor colorFromHexString:@"#4080FF"] forState:UIControlStateNormal];
    
    [self.selectLineView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(64);
        make.height.mas_equalTo(2);
        make.bottom.mas_equalTo(-10);
        make.centerX.equalTo(self.appleButton);
    }];
    
    if ([self.delegate respondsToSelector:@selector(VoiceChatRoomTopSelectView:clickSwitchItem:)]) {
        [self.delegate VoiceChatRoomTopSelectView:self clickSwitchItem:YES];
    }
}

- (void)cancelButtonAction {
    if ([self.delegate respondsToSelector:@selector(VoiceChatRoomTopSelectView:clickCancelAction:)]) {
        [self.delegate VoiceChatRoomTopSelectView:self clickCancelAction:@""];
    }
}

- (void)updateWithRed:(BOOL)isRed {
    self.redImageView.hidden = !isRed;
}

- (void)updateSelectItem:(BOOL)isLeft {
    if (isLeft) {
        [self.onlineButton setTitleColor:[UIColor colorFromHexString:@"#4080FF"] forState:UIControlStateNormal];
        [self.appleButton setTitleColor:[UIColor colorFromHexString:@"#E5E6EB"] forState:UIControlStateNormal];
        [self.selectLineView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.width.mas_equalTo(64);
            make.height.mas_equalTo(2);
            make.bottom.mas_equalTo(-10);
            make.centerX.equalTo(self.onlineButton);
        }];
    } else {
        [self.onlineButton setTitleColor:[UIColor colorFromHexString:@"#E5E6EB"] forState:UIControlStateNormal];
        [self.appleButton setTitleColor:[UIColor colorFromHexString:@"#4080FF"] forState:UIControlStateNormal];
        [self.selectLineView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.width.mas_equalTo(64);
            make.height.mas_equalTo(2);
            make.bottom.mas_equalTo(-10);
            make.centerX.equalTo(self.appleButton);
        }];
    }
}

#pragma mark - getter

- (BaseButton *)onlineButton {
    if (!_onlineButton) {
        _onlineButton = [[BaseButton alloc] init];
        _onlineButton.backgroundColor = [UIColor clearColor];
        [_onlineButton setTitle:@"在线观众" forState:UIControlStateNormal];
        [_onlineButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _onlineButton.titleLabel.font = [UIFont systemFontOfSize:16];
        [_onlineButton addTarget:self action:@selector(onlineButtonAction) forControlEvents:UIControlEventTouchUpInside];
    }
    return _onlineButton;
}

- (BaseButton *)appleButton {
    if (!_appleButton) {
        _appleButton = [[BaseButton alloc] init];
        _appleButton.backgroundColor = [UIColor clearColor];
        [_appleButton setTitle:@"申请消息" forState:UIControlStateNormal];
        [_appleButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _appleButton.titleLabel.font = [UIFont systemFontOfSize:16];
        [_appleButton addTarget:self action:@selector(appleButtonAction) forControlEvents:UIControlEventTouchUpInside];
    }
    return _appleButton;
}

- (UIView *)selectLineView {
    if (!_selectLineView) {
        _selectLineView = [[UIView alloc] init];
        _selectLineView.layer.cornerRadius = 1;
        _selectLineView.layer.masksToBounds = YES;
        _selectLineView.backgroundColor = [UIColor colorFromRGBHexString:@"#1664FF"];
    }
    return _selectLineView;
}

- (UIImageView *)redImageView {
    if (!_redImageView) {
        _redImageView = [[UIImageView alloc] init];
        _redImageView.image = [UIImage imageNamed:@"voicechat_bottom_red" bundleName:HomeBundleName];
        _redImageView.hidden = YES;
    }
    return _redImageView;
}

@end
