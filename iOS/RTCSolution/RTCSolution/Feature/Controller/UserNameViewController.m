// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "UserNameViewController.h"
#import "MenuCreateTextFieldView.h"
#import "NetworkingManager.h"
#import "LocalizatorBundle.h"
#import "Masonry.h"
#import "ToolKit.h"

@interface UserNameViewController ()

@property (nonatomic, strong) UIView *navView;
@property (nonatomic, strong) UILabel *navLabel;
@property (nonatomic, strong) BaseButton *rightButton;
@property (nonatomic, strong) BaseButton *leftButton;

@property (nonatomic, strong) MenuCreateTextFieldView *userNameTextFieldView;

@end

@implementation UserNameViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor colorFromHexString:@"#272E3B"];
    
    [self.view addSubview:self.navView];
    [self.navView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.top.right.equalTo(self.view);
        make.height.mas_equalTo([DeviceInforTool getStatusBarHight] + 44);
    }];
    
    [self.navView addSubview:self.navLabel];
    [self.navLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.navView);
        make.centerY.equalTo(self.navView).offset([DeviceInforTool getStatusBarHight]/2);
    }];
    
    [self.navView addSubview:self.leftButton];
    [self.leftButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.width.mas_equalTo(16);
        make.left.mas_equalTo(16);
        make.centerY.equalTo(self.navLabel);
    }];
    
    [self.navView addSubview:self.rightButton];
    [self.rightButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(-16);
        make.centerY.equalTo(self.navLabel);
    }];
    
    [self.view addSubview:self.userNameTextFieldView];
    [self.userNameTextFieldView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(16);
        make.right.mas_equalTo(-16);
        make.height.mas_equalTo(32);
        make.top.equalTo(self.navView.mas_bottom).offset(30);
    }];
    
    self.userNameTextFieldView.text = [LocalUserComponent userModel].name;
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    [self.userNameTextFieldView becomeFirstResponder];
}

- (void)rightButtonAction:(BaseButton *)sender {
    if (![LocalUserComponent isMatchUserName:self.userNameTextFieldView.text] ||
        self.userNameTextFieldView.text.length <= 0) {
        return;
    }
    __weak __typeof(self) wself = self;
    BaseUserModel *userModel = [LocalUserComponent userModel];
    userModel.name = self.userNameTextFieldView.text;
    [[ToastComponent shareToastComponent] showLoading];
    [NetworkingManager changeUserName:userModel.name
                           loginToken:[LocalUserComponent userModel].loginToken
                                block:^(NetworkingResponse * _Nonnull response) {
        [[ToastComponent shareToastComponent] dismiss];
        if (response.result) {
            [LocalUserComponent updateLocalUserModel:userModel];
            [wself.navigationController popViewControllerAnimated:YES];
        } else {
            [[ToastComponent shareToastComponent] showWithMessage:response.message];
        }
    }];
}

- (void)navBackAction {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - Getter

- (BaseButton *)leftButton {
    if (!_leftButton) {
        _leftButton = [[BaseButton alloc] init];
        [_leftButton setImage:[UIImage imageNamed:@"nav_left"] forState:UIControlStateNormal];
        _leftButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
        [_leftButton addTarget:self action:@selector(navBackAction) forControlEvents:UIControlEventTouchUpInside];
    }
    return _leftButton;
}

- (BaseButton *)rightButton {
    if (!_rightButton) {
        _rightButton = [[BaseButton alloc] init];;
        [_rightButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _rightButton.titleLabel.font = [UIFont systemFontOfSize:15];
        [_rightButton addTarget:self action:@selector(rightButtonAction:) forControlEvents:UIControlEventTouchUpInside];
        [_rightButton setTitle:LocalizedStringFromBundle(@"ok", @"") forState:UIControlStateNormal];
    }
    return _rightButton;
}

- (UIView *)navView {
    if (!_navView) {
        _navView = [[UIView alloc] init];
        _navView.backgroundColor = [UIColor colorFromHexString:@"#272E3B"];
    }
    return _navView;
}

- (UILabel *)navLabel {
    if (!_navLabel) {
        _navLabel = [[UILabel alloc] init];
        _navLabel.font = [UIFont systemFontOfSize:17];
        _navLabel.textColor = [UIColor whiteColor];
        _navLabel.text = LocalizedStringFromBundle(@"change_user_name", @"");
    }
    return _navLabel;
}


- (MenuCreateTextFieldView *)userNameTextFieldView {
    if (!_userNameTextFieldView) {
        _userNameTextFieldView = [[MenuCreateTextFieldView alloc] initWithModify:YES];
        _userNameTextFieldView.placeholderStr = LocalizedStringFromBundle(@"please_enter_user_nickname", @"")  ?: @"";
        _userNameTextFieldView.maxLimit = 18;
        _userNameTextFieldView.minLimit = 1;
    }
    return _userNameTextFieldView;
}

@end
