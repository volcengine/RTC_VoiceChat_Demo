// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "VoiceChatRoomBottomView.h"
#import "UIView+Fillet.h"

@interface VoiceChatRoomBottomView ()

@property (nonatomic, strong) UIView *contentView;
@property (nonatomic, strong) VoiceChatRoomItemButton *inputButton;
@property (nonatomic, strong) NSMutableArray *buttonLists;
@property (nonatomic, strong) VoiceChatUserModel *loginUserModel;

@end

@implementation VoiceChatRoomBottomView

- (instancetype)init {
    self = [super init];
    if (self) {
        self.clipsToBounds = NO;
        self.backgroundColor = [UIColor clearColor];
        
        [self addSubview:self.contentView];
        [self.contentView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.top.bottom.equalTo(self);
            make.width.mas_equalTo(0);
        }];
        
        [self addSubview:self.inputButton];
        [self.inputButton mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(36);
            make.left.top.equalTo(self);
            make.right.equalTo(self.contentView.mas_left).offset(-18);
        }];
        
        [self addSubviewAndConstraints];
    }
    return self;
}

- (void)inputButtonAction {
    if ([self.delegate respondsToSelector:@selector(voiceChatRoomBottomView:itemButton:didSelectStatus:)]) {
        [self.delegate voiceChatRoomBottomView:self itemButton:self.inputButton didSelectStatus:VoiceChatRoomBottomStatusInput];
    }
}

- (void)buttonAction:(VoiceChatRoomItemButton *)sender {
    if ([self.delegate respondsToSelector:@selector(voiceChatRoomBottomView:itemButton:didSelectStatus:)]) {
        [self.delegate voiceChatRoomBottomView:self itemButton:sender didSelectStatus:sender.tagNum];
    }
    
    if (sender.tagNum == VoiceChatRoomBottomStatusLocalMic) {
        BOOL isEnableMic = YES;
        if (sender.status == ButtonStatusActive) {
            sender.status = ButtonStatusNone;
            isEnableMic = YES;
        } else {
            sender.status = ButtonStatusActive;
            isEnableMic = NO;
        }
        [self loadDataWithMediaStatus:isEnableMic];
    }
}

- (void)loadDataWithMediaStatus:(BOOL)isEnable {
    [VoiceChatRTSManager updateMediaStatus:_loginUserModel.roomID
                                              mic:isEnable ? 1 : 0
                                            block:^(RTSACKModel * _Nonnull model) {
        if (!model.result) {
            [[ToastComponent shareToastComponent] showWithMessage:LocalizedString(@"operation_failed_message")];
        }
    }];
}

- (void)addSubviewAndConstraints {
    NSInteger groupNum = 4;
    for (int i = 0; i < groupNum; i++) {
        VoiceChatRoomItemButton *button = [[VoiceChatRoomItemButton alloc] init];
        [button addTarget:self action:@selector(buttonAction:) forControlEvents:UIControlEventTouchUpInside];
        [self.buttonLists addObject:button];
        [self.contentView addSubview:button];
    }
}

#pragma mark - Publish Action

- (void)updateBottomLists:(VoiceChatUserModel *)userModel {
    _loginUserModel = userModel;
    CGFloat itemWidth = 36;
    
    NSArray *status = [self getBottomListsWithModel:userModel];
    NSNumber *number = status.firstObject;
    if (number.integerValue == VoiceChatRoomBottomStatusInput) {
        self.inputButton.hidden = NO;
        NSMutableArray *mutableStatus = [status mutableCopy];
        [mutableStatus removeObjectAtIndex:0];
        status = [mutableStatus copy];
    } else {
        self.inputButton.hidden = YES;
    }
    
    NSMutableArray *lists = [[NSMutableArray alloc] init];
    for (int i = 0; i < self.buttonLists.count; i++) {
        VoiceChatRoomItemButton *button = self.buttonLists[i];
        if (i < status.count) {
            NSNumber *number = status[i];
            VoiceChatRoomBottomStatus bottomStatus = number.integerValue;
            
            button.tagNum = bottomStatus;
            NSString *imageName = [self getImageWithStatus:bottomStatus];
            UIImage *image = [UIImage imageNamed:imageName bundleName:HomeBundleName];
            [button bingImage:image status:ButtonStatusNone];
            [button bingImage:[self getSelectImageWithStatus:bottomStatus] status:ButtonStatusActive];
            button.hidden = NO;
            button.status = ButtonStatusNone;
            [lists addObject:button];
        } else {
            button.hidden = YES;
        }
    }
    
    if (lists.count > 1) {
        [lists mas_remakeConstraints:^(MASConstraintMaker *make) {
                
        }];
        [lists mas_distributeViewsAlongAxis:MASAxisTypeHorizontal withFixedItemLength:itemWidth leadSpacing:0 tailSpacing:0];
        [lists mas_updateConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.contentView);
            make.height.mas_equalTo(36);
        }];
    } else {
        VoiceChatRoomItemButton *button = lists.firstObject;
        [button mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.contentView);
            make.height.mas_equalTo(36);
            make.right.equalTo(self.contentView);
            make.width.mas_equalTo(itemWidth);
        }];
    }
    
    CGFloat counentWidth = (itemWidth * status.count) + ((status.count - 1) * 12);
    [self.contentView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(counentWidth);
    }];
}

- (void)updateButtonStatus:(VoiceChatRoomBottomStatus)status isSelect:(BOOL)isSelect {
    VoiceChatRoomItemButton *selectButton = nil;
    for (VoiceChatRoomItemButton *button in self.buttonLists) {
        if (button.tagNum == status) {
            selectButton = button;
            break;
        }
    }
    if (selectButton) {
        selectButton.status = isSelect ? ButtonStatusActive : ButtonStatusNone;
    }
}

- (void)updateButtonStatus:(VoiceChatRoomBottomStatus)status isRed:(BOOL)isRed {
    VoiceChatRoomItemButton *selectButton = nil;
    for (VoiceChatRoomItemButton *button in self.buttonLists) {
        if (button.tagNum == status) {
            selectButton = button;
            break;
        }
    }
    if (selectButton) {
        selectButton.isRed = isRed;
    }
}

#pragma mark - Private Action

- (NSArray *)getBottomListsWithModel:(VoiceChatUserModel *)userModel {
    NSArray *bottomLists = nil;
    if (UserRoleHost == userModel.userRole) {
        bottomLists = @[@(VoiceChatRoomBottomStatusInput),
                        @(VoiceChatRoomBottomStatusPhone),
                        @(VoiceChatRoomBottomStatusMusic),
                        @(VoiceChatRoomBottomStatusLocalMic),
                        @(VoiceChatRoomBottomStatusEnd)];
    } else {
        if (UserStatusActive == userModel.status) {
            bottomLists = @[@(VoiceChatRoomBottomStatusInput),
                            @(VoiceChatRoomBottomStatusLocalMic),
                            @(VoiceChatRoomBottomStatusEnd)];
        } else {
            bottomLists = @[@(VoiceChatRoomBottomStatusInput),
                            @(VoiceChatRoomBottomStatusEnd)];
        }
    }
    return bottomLists;
}

- (NSString *)getImageWithStatus:(VoiceChatRoomBottomStatus)status {
    NSString *name = @"";
    switch (status) {
        case VoiceChatRoomBottomStatusPhone:
            name = @"voicechat_bottom_phone";
            break;
        case VoiceChatRoomBottomStatusMusic:
            name = @"voicechat_music";
            break;
        case VoiceChatRoomBottomStatusLocalMic:
            name = @"voicechat_bottom_mic";
            break;
        case VoiceChatRoomBottomStatusEnd:
            name = @"voicechat_bottom_end";
            break;
        default:
            break;
    }
    return name;
}

- (UIImage *)getSelectImageWithStatus:(VoiceChatRoomBottomStatus)status {
    NSString *name = @"";
    switch (status) {
        case VoiceChatRoomBottomStatusPhone:
            name = @"voicechat_bottom_phone";
            break;
        case VoiceChatRoomBottomStatusMusic:
            name = @"voicechat_music";
            break;
        case VoiceChatRoomBottomStatusLocalMic:
            name = @"voicechat_localmic_s";
            break;
        case VoiceChatRoomBottomStatusEnd:
            name = @"voicechat_bottom_end";
            break;
        default:
            break;
    }
    return [UIImage imageNamed:name bundleName:HomeBundleName];
}

#pragma mark - Getter

- (UIView *)contentView {
    if (!_contentView) {
        _contentView = [[UIView alloc] init];
        _contentView.backgroundColor = [UIColor clearColor];
    }
    return _contentView;
}

- (NSMutableArray *)buttonLists {
    if (!_buttonLists) {
        _buttonLists = [[NSMutableArray alloc] init];
    }
    return _buttonLists;
}

- (VoiceChatRoomItemButton *)inputButton {
    if (!_inputButton) {
        _inputButton = [[VoiceChatRoomItemButton alloc] init];
        [_inputButton setBackgroundImage:[UIImage imageNamed:@"voicechat_input" bundleName:HomeBundleName] forState:UIControlStateNormal];
        [_inputButton addTarget:self action:@selector(inputButtonAction) forControlEvents:UIControlEventTouchUpInside];
        _inputButton.hidden = YES;
        
        UILabel *label = [[UILabel alloc] init];
        [label setBackgroundColor:[UIColor clearColor]];
        [label setTextColor:[UIColor whiteColor]];
        label.font = [UIFont systemFontOfSize:14 weight:UIFontWeightRegular];
        label.text = LocalizedString(@"say_something");
        label.textColor = [UIColor colorFromRGBHexString:@"#FFFFFF" andAlpha:0.7 * 255];
        label.adjustsFontSizeToFitWidth = YES;
        [_inputButton addSubview:label];
        [label mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.equalTo(_inputButton).offset(12);
            make.width.centerY.equalTo(_inputButton);
        }];
    }
    return _inputButton;
}

@end
