//
//  ToastView.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/6/3.
//  Copyright © 2021 . All rights reserved.
//

#import "ToastView.h"
#import "Masonry.h"

@interface ToastView ()

@property (nonatomic, strong) UIView *bgView;

@end


@implementation ToastView

- (instancetype)initWithMeeage:(NSString *)message {
    self = [super init];
    if (self) {
        CGFloat minScreen = MIN([UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height);
        CGFloat scale = (minScreen / 375);
        self.bgView = [[UIView alloc] init];
        self.bgView.backgroundColor = [UIColor blackColor];
        self.bgView.alpha = 0.8;
        self.bgView.layer.cornerRadius = 4;
        self.bgView.layer.masksToBounds = YES;
        
        UILabel *titleLabel = [[UILabel alloc] init];
        titleLabel.numberOfLines = 0;
        titleLabel.textColor = [UIColor whiteColor];
        titleLabel.textAlignment = NSTextAlignmentCenter;
        titleLabel.font = [UIFont systemFontOfSize:16.0 * scale weight:UIFontWeightRegular];
        titleLabel.text = message;
        
        [self addSubview:self.bgView];
        [self.bgView addSubview:titleLabel];
        
        [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.center.equalTo(self.bgView);
            make.width.mas_lessThanOrEqualTo(minScreen - 24 * 2);
        }];
        
        [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.equalTo(titleLabel).offset(-12);
            make.right.equalTo(titleLabel).offset(12);
            make.top.equalTo(titleLabel).offset(-12);;
            make.bottom.equalTo(titleLabel).offset(12);;
        }];
        
        [self mas_updateConstraints:^(MASConstraintMaker *make) {
            make.size.equalTo(self.bgView);
        }];
    }
    return self;
}

@end
