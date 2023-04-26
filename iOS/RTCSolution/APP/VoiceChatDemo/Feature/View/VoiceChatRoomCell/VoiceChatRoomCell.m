// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "VoiceChatRoomCell.h"
#import "VoiceChatAvatarView.h"

@interface VoiceChatRoomCell ()

@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, strong) UILabel *roomNameLabel;
@property (nonatomic, strong) VoiceChatAvatarView *avatarView;
@property (nonatomic, strong) UILabel *nameLabel;
@property (nonatomic, strong) UIImageView *iconImageView;
@property (nonatomic, strong) UIImageView *peopleNumImageView;
@property (nonatomic, strong) UILabel *peopleNumLabel;

@end

@implementation VoiceChatRoomCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.contentView.backgroundColor = [UIColor clearColor];
        [self createUIComponent];
    }
    return self;
}

- (void)setModel:(VoiceChatRoomModel *)model {
    _model = model;
    [self setLineSpace:12 withText:model.roomName inLabel:self.roomNameLabel];
    self.nameLabel.text = model.hostName;
    self.avatarView.text = model.hostName;
    self.peopleNumLabel.text = [NSString stringWithFormat:@"%ld", (long)(model.audienceCount + 1)];
}

#pragma mark - Private Action

- (void)createUIComponent {
    [self.contentView addSubview:self.bgView];
    [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.contentView).offset(16);
        make.left.equalTo(self.contentView).offset(20);
        make.right.equalTo(self.contentView).offset(-20);
    }];
    
    [self.bgView addSubview:self.roomNameLabel];
    [self.roomNameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.top.equalTo(self.bgView).offset(16);
        make.right.mas_lessThanOrEqualTo(self.bgView.mas_right).offset(-15);
    }];
    
    [self.bgView addSubview:self.avatarView];
    [self.avatarView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.height.mas_equalTo(40);
        make.left.mas_equalTo(16);
        make.top.equalTo(self.roomNameLabel.mas_bottom).offset(16);
        make.bottom.equalTo(self.bgView.mas_bottom).offset(-22);
    }];
    
    [self.bgView addSubview:self.nameLabel];
    [self.nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.equalTo(self.avatarView);
        make.left.equalTo(self.avatarView.mas_right).offset(9);
        make.right.mas_lessThanOrEqualTo(self.bgView.mas_right).offset(-15);
    }];
    
    [self.bgView addSubview:self.iconImageView];
    [self.iconImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(56, 20));
        make.right.equalTo(self.bgView).offset(-16);
        make.centerY.equalTo(self.roomNameLabel);
    }];
    
    [self.bgView addSubview:self.peopleNumLabel];
    [self.peopleNumLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self.bgView).offset(-16);
        make.centerY.equalTo(self.nameLabel);
    }];
    
    [self.bgView addSubview:self.peopleNumImageView];
    [self.peopleNumImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(12, 12));
        make.right.equalTo(self.peopleNumLabel.mas_left).offset(-4);
        make.centerY.equalTo(self.peopleNumLabel);
    }];
    
    [self.bgView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.bottom.equalTo(self.contentView);
    }];
}

#pragma mark - Private Action

- (void)setLineSpace:(CGFloat)lineSpace withText:(NSString *)text inLabel:(UILabel *)label{
    if (!text || !label) {
        return;
    }
    
    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    paragraphStyle.lineSpacing = lineSpace;
    paragraphStyle.lineBreakMode = label.lineBreakMode;
    paragraphStyle.alignment = label.textAlignment;
    
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:text];
    [attributedString addAttribute:NSParagraphStyleAttributeName value:paragraphStyle range:NSMakeRange(0, [text length])];
    label.attributedText = attributedString;
}

#pragma mark - Getter

- (UIImageView *)iconImageView {
    if (!_iconImageView) {
        _iconImageView = [[UIImageView alloc] init];
        _iconImageView.image = [UIImage imageNamed:@"voicechat_live" bundleName:HomeBundleName];
    }
    return _iconImageView;
}

- (UIImageView *)peopleNumImageView {
    if (!_peopleNumImageView) {
        _peopleNumImageView = [[UIImageView alloc] init];
        _peopleNumImageView.image = [UIImage imageNamed:@"voicechat_people" bundleName:HomeBundleName];
    }
    return _peopleNumImageView;
}

- (UILabel *)peopleNumLabel {
    if (!_peopleNumLabel) {
        _peopleNumLabel = [[UILabel alloc] init];
        _peopleNumLabel.textColor = [UIColor whiteColor];
        _peopleNumLabel.font = [UIFont systemFontOfSize:12];
        _peopleNumLabel.numberOfLines = 1;
    }
    return _peopleNumLabel;
}

- (UILabel *)roomNameLabel {
    if (!_roomNameLabel) {
        _roomNameLabel = [[UILabel alloc] init];
        _roomNameLabel.textColor = [UIColor colorFromHexString:@"#FFFFFF"];
        _roomNameLabel.font = [UIFont systemFontOfSize:17];
        _roomNameLabel.numberOfLines = 2;
    }
    return _roomNameLabel;
}

- (VoiceChatAvatarView *)avatarView {
    if (!_avatarView) {
        _avatarView = [[VoiceChatAvatarView alloc] init];
        _avatarView.layer.cornerRadius = 20;
        _avatarView.layer.masksToBounds = YES;
        _avatarView.fontSize = 20;
    }
    return _avatarView;
}

- (UILabel *)nameLabel {
    if (!_nameLabel) {
        _nameLabel = [[UILabel alloc] init];
        _nameLabel.textColor = [UIColor colorFromHexString:@"#E5E6EB"];
        _nameLabel.font = [UIFont systemFontOfSize:16 weight:UIFontWeightRegular];
    }
    return _nameLabel;
}

- (UIView *)bgView {
    if (!_bgView) {
        _bgView = [[UIView alloc] init];
        _bgView.backgroundColor = [UIColor colorFromHexString:@"#394254"];
        _bgView.layer.cornerRadius = 20;
        _bgView.layer.masksToBounds = YES;
    }
    return _bgView;
}
@end
