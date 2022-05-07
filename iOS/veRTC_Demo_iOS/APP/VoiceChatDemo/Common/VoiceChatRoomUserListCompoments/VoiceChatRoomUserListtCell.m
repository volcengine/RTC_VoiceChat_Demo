//
//  VoiceChatRoomUserListtCell.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/19.
//  Copyright © 2021 . All rights reserved.
//

#import "VoiceChatRoomUserListtCell.h"
#import "VoiceChatAvatarView.h"

@interface VoiceChatRoomUserListtCell ()

@property (nonatomic, strong) UILabel *nameLabel;
@property (nonatomic, strong) BaseButton *rightButton;
@property (nonatomic, strong) VoiceChatAvatarView *avatarView;

@end

@implementation VoiceChatRoomUserListtCell


- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.contentView.backgroundColor = [UIColor clearColor];
        [self createUIComponents];
    }
    return self;
}

- (void)setModel:(VoiceChatUserModel *)model {
    _model = model;
    self.nameLabel.text = model.name;
    self.avatarView.text = model.name;
    
    if (model.status == UserStatusActive) {
        [self.rightButton setTitle:@"已上麦" forState:UIControlStateNormal];
        self.rightButton.backgroundColor = [UIColor colorFromRGBHexString:@"#94C2FF"];
        self.rightButton.hidden = NO;
    } else if (model.status == UserStatusApply) {
        [self.rightButton setTitle:@"接受" forState:UIControlStateNormal];
        self.rightButton.backgroundColor = [UIColor colorFromRGBHexString:@"#1664FF"];
        self.rightButton.hidden = NO;
    } else if (model.status == UserStatusInvite) {
        [self.rightButton setTitle:@"已邀请" forState:UIControlStateNormal];
        self.rightButton.backgroundColor = [UIColor colorFromRGBHexString:@"#94C2FF"];
        self.rightButton.hidden = NO;
    } else if (model.status == UserStatusDefault) {
        [self.rightButton setTitle:@"邀请上麦" forState:UIControlStateNormal];
        self.rightButton.backgroundColor = [UIColor colorFromRGBHexString:@"#1664FF"];
        self.rightButton.hidden = NO;
    } else {
        self.rightButton.hidden = YES;
    }
}

- (void)createUIComponents {
    [self.contentView addSubview:self.avatarView];
    [self.avatarView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(40, 40));
        make.left.mas_equalTo(16);
        make.bottom.equalTo(self.contentView);
    }];
    
    [self.contentView addSubview:self.rightButton];
    [self.rightButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(88);
        make.height.mas_equalTo(28);
        make.right.mas_equalTo(-16);
        make.centerY.equalTo(self.avatarView);
    }];
    
    [self.contentView addSubview:self.nameLabel];
    [self.nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.avatarView.mas_right).mas_offset(9);
        make.centerY.equalTo(self.avatarView);
        make.right.mas_lessThanOrEqualTo(self.rightButton.mas_left).offset(-9);
    }];
}

- (void)rightButtonAction:(BaseButton *)sender {
    if ([self.delegate respondsToSelector:@selector(VoiceChatRoomUserListtCell:clickButton:)]) {
        [self.delegate VoiceChatRoomUserListtCell:self clickButton:self.model];
    }
}

#pragma mark - getter

- (BaseButton *)rightButton {
    if (!_rightButton) {
        _rightButton = [[BaseButton alloc] init];
        _rightButton.layer.cornerRadius = 14;
        _rightButton.layer.masksToBounds = YES;
        _rightButton.titleLabel.font = [UIFont systemFontOfSize:14];
        [_rightButton addTarget:self action:@selector(rightButtonAction:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _rightButton;
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
        _nameLabel.font = [UIFont systemFontOfSize:16];
    }
    return _nameLabel;
}

@end
