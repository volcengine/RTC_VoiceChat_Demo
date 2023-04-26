// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "VoiceChatRoomParamInfoView.h"
#import "UIView+Fillet.h"

@interface VoiceChatRoomParamInfoView ()

@property (nonatomic, strong) UILabel *messageLabel;

@end

@implementation VoiceChatRoomParamInfoView

- (instancetype)init {
    self = [super init];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        
        [self addSubview:self.messageLabel];
        [self.messageLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self);
        }];
    }
    return self;
}

#pragma mark - Publish Action


- (void)setParamInfoModel:(VoiceChatRoomParamInfoModel *)paramInfoModel {
    _paramInfoModel = paramInfoModel;
    
    if (IsEmptyStr(paramInfoModel.rtt)) {
        paramInfoModel.rtt = @"0";
    }
    NSString *noticeStr = LocalizedString(@"delay");
    self.messageLabel.text = [NSString stringWithFormat:@"%@ %@ms",noticeStr,
                              paramInfoModel.rtt];
}

#pragma mark - Private Action

- (UILabel *)messageLabel {
    if (!_messageLabel) {
        _messageLabel = [[UILabel alloc] init];
        _messageLabel.font = [UIFont systemFontOfSize:12];
        _messageLabel.textColor = [UIColor colorFromHexString:@"#00CF31"];
    }
    return _messageLabel;
}

@end
