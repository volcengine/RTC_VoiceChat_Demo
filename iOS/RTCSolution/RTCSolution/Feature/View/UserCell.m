// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "UserCell.h"
#import "LocalizatorBundle.h"
#import "Masonry.h"
#import "ToolKit.h"

@interface UserCell ()

@property (nonatomic, strong) UIView *lineView;
@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UILabel *desTitleLabel;
@property (nonatomic, strong) UIImageView *moreImageView;

@end

@implementation UserCell


- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.contentView.backgroundColor = [UIColor clearColor];
        [self createUIComponent];
    }
    return self;
}

- (void)setModel:(MenuCellModel *)model {
    _model = model;
    self.titleLabel.text = model.title;
    if ([model.title isEqualToString:LocalizedStringFromBundle(@"user_name", @"")]) {
        self.desTitleLabel.text = [LocalUserComponent userModel].name;
    } else {
        self.desTitleLabel.text = model.desTitle;
    }
    
    CGFloat desRight = 0;
    if (model.isMore) {
        desRight = -40;
        self.moreImageView.hidden = NO;
    } else {
        desRight = -16;
        self.moreImageView.hidden = YES;
    }

    [self.desTitleLabel mas_updateConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(desRight);
    }];
}

#pragma mark - Private Action

- (void)createUIComponent {
    [self.contentView addSubview:self.titleLabel];
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.contentView).offset(16);
        make.centerY.equalTo(self.contentView);
    }];
    
    [self.contentView addSubview:self.desTitleLabel];
    [self.desTitleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(-16);
        make.centerY.equalTo(self.titleLabel);
        make.width.mas_lessThanOrEqualTo(150);
    }];
    
    [self.contentView addSubview:self.moreImageView];
    [self.moreImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(16, 16));
        make.right.mas_equalTo(-16);
        make.centerY.equalTo(self.titleLabel);
    }];
    
    [self.contentView addSubview:self.lineView];
    [self.lineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(16.f);
        make.height.mas_equalTo(1);
        make.bottom.equalTo(self.contentView);
        make.right.equalTo(self.contentView);
    }];
}

#pragma mark - Private Action



#pragma mark - Getter

- (UILabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [[UILabel alloc] init];
        _titleLabel.textColor = [UIColor whiteColor];
        _titleLabel.font = [UIFont systemFontOfSize:16 weight:UIFontWeightRegular];
        _titleLabel.numberOfLines = 2;
    }
    return _titleLabel;
}

- (UILabel *)desTitleLabel {
    if (!_desTitleLabel) {
        _desTitleLabel = [[UILabel alloc] init];
        _desTitleLabel.textColor = [UIColor colorFromHexString:@"#86909C"];
        _desTitleLabel.font = [UIFont systemFontOfSize:16 weight:UIFontWeightRegular];
    }
    return _desTitleLabel;
}

- (UIView *)lineView {
    if (!_lineView) {
        _lineView = [[UIView alloc] init];
        _lineView.backgroundColor = [UIColor colorFromRGBHexString:@"#FFFFFF" andAlpha:0.1 * 255];
    }
    return _lineView;
}

- (UIImageView *)moreImageView {
    if (!_moreImageView) {
        _moreImageView = [[UIImageView alloc] init];
        _moreImageView.image = [UIImage imageNamed:@"menu_list_more"];
    }
    return _moreImageView;
}

@end
