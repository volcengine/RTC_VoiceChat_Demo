//
//  MenuLoginViewController.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/6/6.
//  Copyright © 2021 . All rights reserved.
//

#import "MenuLoginViewController.h"
#import "MenuCreateTextFieldView.h"
#import "PhonePrivacyView.h"
#import "LoginControlComponent.h"
#import "BuildConfig.h"

@interface MenuLoginViewController ()

@property (nonatomic, strong) UIImageView *maskImageView;
@property (nonatomic, strong) UIImageView *iconImageView;
@property (nonatomic, strong) MenuCreateTextFieldView *userNameTextFieldView;
@property (nonatomic, strong) UIButton *loginButton;
@property (nonatomic, strong) PhonePrivacyView *privacyView;
@property (nonatomic, strong) UILabel *verLabel;

@end

@implementation MenuLoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyBoardDidShow:) name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyBoardDidHide:) name:UIKeyboardWillHideNotification object:nil];
    
    [self addMakeConstraints];
    
    __weak __typeof(self) wself = self;
    self.userNameTextFieldView.textFieldChangeBlock = ^(NSString * _Nonnull text) {
        [wself updateLoginButtonStatus];
    };
    self.privacyView.changeAgree = ^(BOOL isAgree) {
        [wself updateLoginButtonStatus];
    };
    [self updateLoginButtonStatus];
    
    NSString *sdkVer = [BaseRTCManager getSdkVersion];
    NSString *appVer = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleShortVersionString"];
    self.verLabel.text = [NSString stringWithFormat:@"App版本 v%@ / SDK版本 v%@", appVer, sdkVer];
}

#pragma mark - Private Action

- (void)keyBoardDidShow:(NSNotification *)notifiction {
    CGRect keyboardRect = [[notifiction.userInfo objectForKey:UIKeyboardFrameEndUserInfoKey] CGRectValue];
    [UIView animateWithDuration:0.25 animations:^{
        [self.loginButton mas_updateConstraints:^(MASConstraintMaker *make) {
            make.bottom.equalTo(self.view).offset(-keyboardRect.size.height - 80/2);
        }];
    }];
    self.iconImageView.hidden = SCREEN_WIDTH <= 320 ? YES : NO;
    [self.view layoutIfNeeded];
}

- (void)keyBoardDidHide:(NSNotification *)notifiction {
    [UIView animateWithDuration:0.25 animations:^{
        [self.loginButton mas_updateConstraints:^(MASConstraintMaker *make) {
            make.bottom.equalTo(self.view).offset(-(SCREEN_HEIGHT * 0.3) - [DeviceInforTool getVirtualHomeHeight]);
        }];
    }];
    self.iconImageView.hidden = NO;
    [self.view layoutIfNeeded];
}

- (void)loginButtonAction:(UIButton *)sender {
    if (IsEmptyStr(HeadUrl)) {
        NSString *errorMessage = @"请在 BuildConfig.h 配置 url 信息";
        [[ToastComponent shareToastComponent] showWithMessage:errorMessage];
        return;
    }
    __weak __typeof(self) wself = self;
    [[ToastComponent shareToastComponent] showLoading];
    [LoginControlComponent passwordFreeLogin:self.userNameTextFieldView.text
                                        block:^(BOOL result, NSString * _Nullable errorStr) {
        [[ToastComponent shareToastComponent] dismiss];
        if (result) {
            [wself dismissViewControllerAnimated:YES completion:^{
                
            }];
        } else {
            [[ToastComponent shareToastComponent] showWithMessage:errorStr];
        }
    }];
}

- (void)maskAction {
    [self.userNameTextFieldView resignFirstResponder];
}

#pragma mark - Private Action

- (void)updateLoginButtonStatus {
    BOOL isDisable = YES;
    BOOL isIllega = NO;
    isDisable = IsEmptyStr(self.userNameTextFieldView.text);
    isIllega = self.userNameTextFieldView.isIllega;
    BOOL isAgree = self.privacyView.isAgree;
    if (isDisable || isIllega || !isAgree) {
        self.loginButton.userInteractionEnabled = NO;
        self.loginButton.backgroundColor = [UIColor colorFromRGBHexString:@"#4080FF" andAlpha:0.3 * 255];
        [self.loginButton setTitleColor:[UIColor colorFromRGBHexString:@"#ffffff" andAlpha:0.3 * 255] forState:UIControlStateNormal];
    } else {
        self.loginButton.userInteractionEnabled = YES;
        self.loginButton.backgroundColor = [UIColor colorFromHexString:@"#0E42D2"];
        [self.loginButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    }
}

- (void)addMakeConstraints {
    [self.view addSubview:self.maskImageView];
    [self.maskImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.view);
    }];
    
    [self.view addSubview:self.iconImageView];
    [self.iconImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view);
        make.top.mas_equalTo(100 + [DeviceInforTool getStatusBarHight]);
    }];
    
    [self.view addSubview:self.loginButton];
    [self.loginButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(30);
        make.right.mas_equalTo(-30);
        make.height.mas_equalTo(50);
        make.bottom.equalTo(self.view).offset(-(SCREEN_HEIGHT * 0.3) - [DeviceInforTool getVirtualHomeHeight]);
    }];
    
    [self.view addSubview:self.userNameTextFieldView];
    [self.userNameTextFieldView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(30);
        make.right.mas_equalTo(-30);
        make.height.mas_equalTo(32);
        make.bottom.equalTo(self.loginButton.mas_top).offset(-78);
    }];
    
    [self.view addSubview:self.privacyView];
    [self.privacyView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(304, 22));
        make.left.equalTo(self.userNameTextFieldView);
        make.centerY.equalTo(self.userNameTextFieldView.mas_bottom).offset(32);
    }];
    
    [self.view addSubview:self.verLabel];
    [self.verLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view);
        make.bottom.equalTo(self.view).offset(-([DeviceInforTool getVirtualHomeHeight] + 20));
    }];
}

#pragma mark - getter

- (MenuCreateTextFieldView *)userNameTextFieldView {
    if (!_userNameTextFieldView) {
        _userNameTextFieldView = [[MenuCreateTextFieldView alloc] initWithModify:NO];
        _userNameTextFieldView.placeholderStr = @"请输入用户昵称";
        _userNameTextFieldView.maxLimit = 18;
    }
    return _userNameTextFieldView;
}

- (UIImageView *)maskImageView {
    if (!_maskImageView) {
        _maskImageView = [[UIImageView alloc] init];
        _maskImageView.image = [UIImage imageNamed:@"menu_mask" bundleName:@"Login"];
        _maskImageView.contentMode = UIViewContentModeScaleAspectFill;
        _maskImageView.clipsToBounds = YES;
        
        UIView *view = [[UIView alloc] init];
        view.userInteractionEnabled = NO;
        view.backgroundColor = [UIColor colorFromRGBHexString:@"#141C2C" andAlpha:0.7 * 255];
        [_maskImageView addSubview:view];
        [view mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(_maskImageView);
        }];
        
        _maskImageView.userInteractionEnabled = YES;
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(maskAction)];
        [_maskImageView addGestureRecognizer:tap];
    }
    return _maskImageView;
}

- (UIImageView *)iconImageView {
    if (!_iconImageView) {
        _iconImageView = [[UIImageView alloc] init];
        _iconImageView.image = [UIImage imageNamed:@"logo_icon" bundleName:@"Login"];
        _iconImageView.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _iconImageView;
}

- (UIButton *)loginButton {
    if (!_loginButton) {
        _loginButton = [[UIButton alloc] init];
        _loginButton.layer.masksToBounds = YES;
        _loginButton.layer.cornerRadius = 50/2;
        [_loginButton setTitle:@"登录" forState:UIControlStateNormal];
        _loginButton.titleLabel.font = [UIFont systemFontOfSize:16 weight:UIFontWeightRegular];
        [_loginButton addTarget:self action:@selector(loginButtonAction:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _loginButton;
}

- (PhonePrivacyView *)privacyView {
    if (!_privacyView) {
        _privacyView = [[PhonePrivacyView alloc] init];
    }
    return _privacyView;
}

- (UILabel *)verLabel {
    if (!_verLabel) {
        _verLabel = [[UILabel alloc] init];
        _verLabel.textColor = [UIColor colorFromHexString:@"#86909C"];
        _verLabel.font = [UIFont systemFontOfSize:12 weight:UIFontWeightRegular];
    }
    return _verLabel;
}

- (void)dealloc {

}

@end
