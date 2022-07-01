//
//  UserNameViewController.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/23.
//  Copyright © 2021 . All rights reserved.
//

#import "UserNameViewController.h"
#import "MenuCreateTextFieldView.h"
#import "NetworkingManager.h"
#import "Masonry.h"
#import "Core.h"

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
    
    [self.userNameTextFieldView becomeFirstResponder];
    
    self.userNameTextFieldView.text = [LocalUserComponents userModel].name;
}

- (void)rightButtonAction:(BaseButton *)sender {
    if (![LocalUserComponents isMatchUserName:self.userNameTextFieldView.text] ||
        self.userNameTextFieldView.text.length <= 0) {
        return;
    }
    __weak __typeof(self) wself = self;
    BaseUserModel *userModel = [LocalUserComponents userModel];
    userModel.name = self.userNameTextFieldView.text;
    [NetworkingManager changeUserName:userModel.name
                           loginToken:[LocalUserComponents userModel].loginToken
                                block:^(NetworkingResponse * _Nonnull response) {
        if (response.result) {
            [LocalUserComponents updateLocalUserModel:userModel];
            [wself.navigationController popViewControllerAnimated:YES];
        } else {
            [[ToastComponents shareToastComponents] showWithMessage:response.message];
        }
    }];
}

- (void)navBackAction {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - getter

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
        [_rightButton setTitle:@"确定" forState:UIControlStateNormal];
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
        _navLabel.text = @"设置名称";
    }
    return _navLabel;
}


- (MenuCreateTextFieldView *)userNameTextFieldView {
    if (!_userNameTextFieldView) {
        _userNameTextFieldView = [[MenuCreateTextFieldView alloc] initWithModify:YES];
        _userNameTextFieldView.placeholderStr = @"请输入用户昵称";
        _userNameTextFieldView.maxLimit = 18;
        _userNameTextFieldView.minLimit = 1;
    }
    return _userNameTextFieldView;
}

@end
