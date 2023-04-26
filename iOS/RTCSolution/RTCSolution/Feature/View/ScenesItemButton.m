// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "ScenesItemButton.h"
#import "Masonry.h"
#import "ToolKit.h"

@interface ScenesItemButton ()

@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, strong) UIView *bgSelectView;

@property (nonatomic, strong) UIImageView *iconImageView;
@property (nonatomic, strong) UIImageView *bgImageView;
@property (nonatomic, strong) UILabel *label;
@property (nonatomic, assign) BOOL hasAddItemLayer;

@end

@implementation ScenesItemButton

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        
        self.layer.cornerRadius = 20;
        self.layer.masksToBounds = YES;
        self.backgroundColor = [UIColor clearColor];
        
        [self addTarget:self action:@selector(touchUp:) forControlEvents:UIControlEventTouchUpInside];
        [self addTarget:self action:@selector(touchUp:) forControlEvents:UIControlEventTouchUpOutside];
        [self addTarget:self action:@selector(touchDown:) forControlEvents:UIControlEventTouchDown];
        [self addTarget:self action:@selector(touchUp:) forControlEvents:UIControlEventTouchCancel];
        
        [self addSubview:self.bgView];
        [self addSubview:self.bgSelectView];
        
        [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self);
        }];
        [self.bgSelectView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self);
        }];

        [self addSubview:self.bgImageView];
        [self addSubview:self.iconImageView];
        [self addSubview:self.label];
                
        [self.iconImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(90, 90));
            make.centerY.equalTo(self);
            make.centerX.equalTo(self).multipliedBy(0.5);
        }];
        
        [self.bgImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(300 / 2, 236 / 2));
            make.center.equalTo(self.iconImageView);
        }];
       
        [self.label mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.equalTo(self).offset(-5);
            make.left.equalTo(self.mas_right).multipliedBy(0.5);
            make.centerY.equalTo(self);
        }];
    }
    return self;
}

- (void)layoutSubviews{
    [super layoutSubviews];
    
    if (_hasAddItemLayer == NO) {
        [self addItemLayer];
        _hasAddItemLayer = YES;
    }
}

#pragma mark - setter

- (void)setModel:(SceneButtonModel *)model{
    _model = model;
    
    self.iconImageView.image = [UIImage imageNamed:model.iconName];
    self.bgImageView.image = [UIImage imageNamed:model.bgName];
    self.label.text = model.title;
}

#pragma mark - Publish Action

- (void)addItemLayer {
    [self addItemBgGradientLayer];
    [self addItemSelectBgGradientLayer];
}

#pragma mark - Private Action

- (void)touchUp:(UIButton *)sender {
    self.bgView.hidden = NO;
    self.bgSelectView.hidden = YES;
}

- (void)touchDown:(UIButton *)sender {
    self.bgView.hidden = YES;
    self.bgSelectView.hidden = NO;
}

- (void)addItemBgGradientLayer {
    UIColor *startColor = [UIColor colorFromHexString:@"#39455B"];
    UIColor *endColor = [UIColor colorFromRGBHexString:@"#39455B"];
    CAGradientLayer *gradientLayer = [CAGradientLayer layer];
    gradientLayer.frame = self.bgView.bounds;
    gradientLayer.colors = @[(__bridge id)[startColor colorWithAlphaComponent:1.0].CGColor,
                             (__bridge id)[endColor colorWithAlphaComponent:0.5].CGColor];
    gradientLayer.startPoint = CGPointMake(0.5, .0);
    gradientLayer.endPoint = CGPointMake(0.5, 1.0);
    [self.bgView.layer addSublayer:gradientLayer];
}

- (void)addItemSelectBgGradientLayer {
    UIColor *startColor = [UIColor colorFromRGBHexString:@"#39455B"];
    UIColor *endColor = [UIColor colorFromHexString:@"#39455B"];
    CAGradientLayer *gradientLayer = [CAGradientLayer layer];
    gradientLayer.frame = self.bgSelectView.bounds;
    gradientLayer.colors = @[(__bridge id)[startColor colorWithAlphaComponent:0.5].CGColor,
                             (__bridge id)[endColor colorWithAlphaComponent:1.0].CGColor];
    gradientLayer.startPoint = CGPointMake(0.5, .0);
    gradientLayer.endPoint = CGPointMake(0.5, 1.0);
    [self.bgSelectView.layer addSublayer:gradientLayer];
}

#pragma mark - Getter

- (UIImageView *)iconImageView {
    if (_iconImageView == nil) {
        _iconImageView = [[UIImageView alloc] init];
        _iconImageView.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _iconImageView;;
}

- (UIImageView *)bgImageView {
    if (_bgImageView == nil) {
        _bgImageView = [[UIImageView alloc] init];
        
    }
    return _bgImageView;
}

- (UILabel *)label {
    if (_label == nil) {
        _label = [[UILabel alloc] init];
        _label.textColor = [UIColor whiteColor];
        _label.numberOfLines = 3;
        _label.font = [UIFont systemFontOfSize:24 weight:UIFontWeightRegular];
    }
    return _label;
}

- (UIView *)bgView {
    if (!_bgView) {
        _bgView = [[UIView alloc] init];
        _bgView.userInteractionEnabled = NO;
    }
    return _bgView;
}

- (UIView *)bgSelectView {
    if (!_bgSelectView) {
        _bgSelectView = [[UIView alloc] init];
        _bgSelectView.userInteractionEnabled = NO;
        _bgSelectView.hidden = YES;
    }
    return _bgSelectView;
}

@end
