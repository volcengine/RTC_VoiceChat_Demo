//
//  VoiceChatPeopleNumView.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/10/12.
//  Copyright © 2021 . All rights reserved.
//

#import "VoiceChatPeopleNumView.h"

@interface VoiceChatPeopleNumView ()

@property (nonatomic, strong) UIImageView *iconImageView;
@property (nonatomic, strong) UILabel *titleLabel;

@end

@implementation VoiceChatPeopleNumView

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self addSubview:self.iconImageView];
        [self.iconImageView mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(24, 24));
          make.left.mas_equalTo(8);
          make.centerY.equalTo(self);
        }];

        [self addSubview:self.titleLabel];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
          make.centerY.equalTo(self);
          make.left.equalTo(self.iconImageView.mas_right).offset(8);
        }];

        [self mas_updateConstraints:^(MASConstraintMaker *make) {
          make.right.equalTo(self.titleLabel.mas_right).offset(8);
        }];
    }
    return self;
}

- (void)updateTitleLabel:(NSInteger)num {
    num++;
    NSString *str = [NSString stringWithFormat:@"%ld", (long)num];
    if (num >= 10000) {
        CGFloat value = floorf(num / 1000.0);
        str = [NSString stringWithFormat:@"%.1fW", value / 10];
    } else if (num >= 1000) {
        CGFloat value = floorf(num / 100.0);
        str = [NSString stringWithFormat:@"%.1fk", value / 10];
    }
    self.titleLabel.text = str;
}

#pragma mark - getter

- (UILabel *)titleLabel {
    if (_titleLabel == nil) {
        _titleLabel = [[UILabel alloc] init];
        _titleLabel.textColor = [UIColor whiteColor];
        _titleLabel.textAlignment = NSTextAlignmentCenter;
        _titleLabel.font = [UIFont systemFontOfSize:12];
    }
    return _titleLabel;
}

- (UIImageView *)iconImageView {
    if (_iconImageView == nil) {
        _iconImageView = [[UIImageView alloc] init];
        _iconImageView.image = [UIImage imageNamed:@"people_num" bundleName:HomeBundleName];
    }
    return _iconImageView;
}

@end
