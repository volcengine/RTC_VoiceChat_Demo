// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "BaseViewController.h"
#import "Masonry.h"

@interface BaseViewController () <UIGestureRecognizerDelegate>

@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, strong) UILabel *navTitleLabel;
@property (nonatomic, strong) BaseButton *leftButton;
@property (nonatomic, strong) BaseButton *rightButton;

@end

@implementation BaseViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationController.interactivePopGestureRecognizer.delegate = self;
    
    [self.view addSubview:self.bgView];
    [self.view addSubview:self.navView];
    [self.navView addSubview:self.navTitleLabel];
    [self.navView addSubview:self.leftButton];
    [self.navView addSubview:self.rightButton];
    [self addConstraints];
    [self addBgGradientLayer];
}

- (void)addConstraints {
    [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.view);
    }];
    
    [self.navView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.top.right.equalTo(self.view);
        make.height.mas_equalTo([DeviceInforTool getStatusBarHight] + 44);
    }];
    
    [self.navTitleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.navView);
        make.centerY.equalTo(self.navView).offset([DeviceInforTool getStatusBarHight]/2);
    }];
    
    [self.leftButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.width.mas_equalTo(16);
        make.left.mas_equalTo(16);
        make.centerY.equalTo(self.navTitleLabel);
    }];
    
    [self.rightButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(-16);
        make.centerY.equalTo(self.navTitleLabel);
    }];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:NO];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    self.navigationController.interactivePopGestureRecognizer.enabled = YES;
}

- (void)navBackAction:(UIButton *)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - Publish Action

- (void)rightButtonAction:(BaseButton *)sender {
    
}

- (void)setNavTitle:(NSString *)navTitle {
    _navTitle = navTitle;
    self.navTitleLabel.text = navTitle;
}

- (void)setNavRightTitle:(NSString *)navRightTitle {
    _navRightTitle = navRightTitle;
    
    if (navRightTitle && navRightTitle.length > 0) {
        self.navRightImage = [UIImage new];
    }
    [self.rightButton setTitle:navRightTitle forState:UIControlStateNormal];
}

- (void)setNavRightImage:(UIImage *)navRightImage {
    _navRightImage = navRightImage;
    
    if (navRightImage) {
        self.navRightTitle = @"";
    }
    [self.rightButton setImage:navRightImage
                      forState:UIControlStateNormal];
}

- (void)setNavLeftImage:(UIImage *)navLeftImage {
    _navLeftImage = navLeftImage;
    
    [self.leftButton setImage:navLeftImage
                     forState:UIControlStateNormal];
}

#pragma mark - Private Action

- (void)addBgGradientLayer {
    UIColor *startColor = [UIColor colorFromHexString:@"#30394A"];
    UIColor *endColor = [UIColor colorFromHexString:@"#1D2129"];
    CAGradientLayer *gradientLayer = [CAGradientLayer layer];
    gradientLayer.frame = self.view.bounds;
    gradientLayer.colors = @[(__bridge id)[startColor colorWithAlphaComponent:1.0].CGColor,
                             (__bridge id)[endColor colorWithAlphaComponent:1.0].CGColor];
    gradientLayer.startPoint = CGPointMake(.0, .0);
    gradientLayer.endPoint = CGPointMake(.0, 1.0);
    [self.bgView.layer addSublayer:gradientLayer];
}

#pragma mark - Getter

- (BaseButton *)leftButton {
    if (!_leftButton) {
        _leftButton = [[BaseButton alloc] init];
        [_leftButton setImage:[UIImage imageNamed:@"nav_left" bundleName:@"ToolKit"] forState:UIControlStateNormal];
        _leftButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
        [_leftButton addTarget:self action:@selector(navBackAction:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _leftButton;
}

- (BaseButton *)rightButton {
    if (!_rightButton) {
        _rightButton = [[BaseButton alloc] init];;
        [_rightButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _rightButton.titleLabel.font = [UIFont systemFontOfSize:15];
        [_rightButton addTarget:self action:@selector(rightButtonAction:) forControlEvents:UIControlEventTouchUpInside];
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

- (UILabel *)navTitleLabel {
    if (!_navTitleLabel) {
        _navTitleLabel = [[UILabel alloc] init];
        _navTitleLabel.font = [UIFont systemFontOfSize:17];
        _navTitleLabel.textColor = [UIColor whiteColor];
    }
    return _navTitleLabel;
}

- (UIView *)bgView {
    if (!_bgView) {
        _bgView = [[UIView alloc] init];
    }
    return _bgView;
}


@end
