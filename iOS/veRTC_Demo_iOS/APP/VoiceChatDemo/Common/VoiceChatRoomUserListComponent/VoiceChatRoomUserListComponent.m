//
//  VoiceChatRoomUserListComponent.m
//  veRTC_Demo
//
//  Created by on 2021/5/19.
//  
//

#import "VoiceChatRoomUserListComponent.h"
#import "VoiceChatRoomTopSelectView.h"
#import "VoiceChatRoomTopSeatView.h"

@interface VoiceChatRoomUserListComponent () <VoiceChatRoomTopSelectViewDelegate, VoiceChatRoomRaiseHandListsViewDelegate, VoiceChatRoomAudienceListsViewDelegate>

@property (nonatomic, strong) VoiceChatRoomTopSeatView *topSeatView;
@property (nonatomic, strong) VoiceChatRoomTopSelectView *topSelectView;
@property (nonatomic, strong) VoiceChatRoomRaiseHandListsView *applyListsView;
@property (nonatomic, strong) VoiceChatRoomAudienceListsView *onlineListsView;
@property (nonatomic, strong) UIButton *maskButton;

@property (nonatomic, copy) void (^dismissBlock)(void);
@property (nonatomic, strong) VoiceChatRoomModel *roomModel;
@property (nonatomic, copy) NSString *seatID;
@property (nonatomic, assign) BOOL isRed;

@end


@implementation VoiceChatRoomUserListComponent

- (instancetype)init {
    self = [super init];
    if (self) {
        _isRed = NO;
    }
    return self;
}

#pragma mark - Publish Action

- (void)showRoomModel:(VoiceChatRoomModel *)roomModel
               seatID:(NSString *)seatID
         dismissBlock:(void (^)(void))dismissBlock {
    _roomModel = roomModel;
    _seatID = seatID;
    self.dismissBlock = dismissBlock;
    UIViewController *rootVC = [DeviceInforTool topViewController];
    
    [rootVC.view addSubview:self.maskButton];
    [self.maskButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.left.height.equalTo(rootVC.view);
        make.top.equalTo(rootVC.view).offset(SCREEN_HEIGHT);
    }];
    
    [self.maskButton addSubview:self.applyListsView];
    [self.applyListsView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(0);
        make.right.mas_equalTo(0);
        CGFloat hetight = ((300.0 / 667.0) * SCREEN_HEIGHT);
        make.height.mas_offset(hetight + [DeviceInforTool getVirtualHomeHeight]);
        make.bottom.mas_offset(0);
    }];
    
    [self.maskButton addSubview:self.onlineListsView];
    [self.onlineListsView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.applyListsView);
    }];
    
    [self.maskButton addSubview:self.topSelectView];
    [self.topSelectView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self.maskButton);
        make.bottom.equalTo(self.applyListsView.mas_top);
        make.height.mas_equalTo(52);
    }];
    
    [self.maskButton addSubview:self.topSeatView];
    [self.topSeatView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self.maskButton);
        make.bottom.equalTo(self.topSelectView.mas_top);
        make.height.mas_equalTo(48);
    }];
    
    // Start animation
    [rootVC.view layoutIfNeeded];
    [self.maskButton.superview setNeedsUpdateConstraints];
    [UIView animateWithDuration:0.25
                     animations:^{
        [self.maskButton mas_updateConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(rootVC.view).offset(0);
        }];
        [self.maskButton.superview layoutIfNeeded];
    }];
    
    if (_isRed) {
        [self loadDataWithApplyLists];
        [self.topSelectView updateSelectItem:NO];
        self.onlineListsView.hidden = YES;
        self.applyListsView.hidden = NO;
    } else {
        [self loadDataWithOnlineLists];
        [self.topSelectView updateSelectItem:YES];
        self.onlineListsView.hidden = NO;
        self.applyListsView.hidden = YES;
    }
    
    __weak __typeof(self) wself = self;
    self.topSeatView.clickSwitchBlock = ^(BOOL isOn) {
        [wself loadDataWithSwitch:isOn];
    };
}

- (void)update {
    if (self.onlineListsView.superview && !self.onlineListsView.hidden) {
        [self loadDataWithOnlineLists];
    } else if (self.applyListsView.superview && !self.applyListsView.hidden) {
        [self loadDataWithApplyLists];
    } else {
        
    }
}

- (void)updateWithRed:(BOOL)isRed {
    _isRed = isRed;
    [self.topSelectView updateWithRed:isRed];
}

#pragma mark - Load Data

- (void)loadDataWithOnlineLists {
    __weak __typeof(self) wself = self;
    [VoiceChatRTMManager getAudienceList:_roomModel.roomID
                                          block:^(NSArray<VoiceChatUserModel *> * _Nonnull userLists, RTMACKModel * _Nonnull model) {
        if (model.result) {
            wself.onlineListsView.dataLists = userLists;
        }
    }];
}

- (void)loadDataWithApplyLists {
    __weak __typeof(self) wself = self;
    [VoiceChatRTMManager getApplyAudienceList:_roomModel.roomID
                                               block:^(NSArray<VoiceChatUserModel *> * _Nonnull userLists, RTMACKModel * _Nonnull model) {
        if (model.result) {
            wself.applyListsView.dataLists = userLists;
        }
    }];
}

#pragma mark - VoiceChatRoomTopSelectViewDelegate

- (void)VoiceChatRoomTopSelectView:(VoiceChatRoomTopSelectView *)VoiceChatRoomTopSelectView clickCancelAction:(id)model {
    [self dismissUserListView];
}

- (void)VoiceChatRoomTopSelectView:(VoiceChatRoomTopSelectView *)VoiceChatRoomTopSelectView clickSwitchItem:(BOOL)isAudience {
    if (isAudience) {
        self.onlineListsView.hidden = YES;
        self.applyListsView.hidden = NO;
        [self loadDataWithApplyLists];
    } else {
        self.onlineListsView.hidden = NO;
        self.applyListsView.hidden = YES;
        [self loadDataWithOnlineLists];
    }
}

#pragma mark - VoiceChatRoomonlineListsViewDelegate

- (void)voiceChatRoomAudienceListsView:(VoiceChatRoomRaiseHandListsView *)voiceChatRoomAudienceListsView clickButton:(VoiceChatUserModel *)model {
    [self clickTableViewWithModel:model dataLists:voiceChatRoomAudienceListsView.dataLists];
}

#pragma mark - VoiceChatRoomapplyListsViewDelegate

- (void)voiceChatRoomRaiseHandListsView:(VoiceChatRoomRaiseHandListsView *)voiceChatRoomRaiseHandListsView clickButton:(VoiceChatUserModel *)model {
    [self clickTableViewWithModel:model dataLists:voiceChatRoomRaiseHandListsView.dataLists];
}

#pragma mark - Private Action

- (void)loadDataWithSwitch:(BOOL)isOn {
    NSInteger type = isOn ? 1 : 2;
    [VoiceChatRTMManager managerInteractApply:self.roomModel.roomID
                                                type:type
                                               block:^(RTMACKModel * _Nonnull model) {
        if (!model.result) {
            [[ToastComponent shareToastComponent] showWithMessage:@"操作失败，请重试"];
            [[NSNotificationCenter defaultCenter] postNotificationName:NotificationUpdateSeatSwitch object:@(!isOn)];
        }
        [[NSNotificationCenter defaultCenter] postNotificationName:NotificationResultSeatSwitch object:nil];
    }];
}

- (void)clickTableViewWithModel:(VoiceChatUserModel *)userModel dataLists:(NSArray<VoiceChatUserModel *> *)dataLists {
    if (userModel.status == UserStatusDefault) {
        __weak __typeof(self) wself = self;
        [[ToastComponent shareToastComponent] showLoading];
        [VoiceChatRTMManager inviteInteract:userModel.roomID
                                        uid:userModel.uid
                                     seatID:_seatID
                                      block:^(RTMACKModel * _Nonnull model) {
            [[ToastComponent shareToastComponent] dismiss];
            if (!model.result) {
                [[ToastComponent shareToastComponent] showWithMessage:model.message];
            } else {
                [wself update];
                [[ToastComponent shareToastComponent] showWithMessage:@"已向观众发出邀请，等待对方应答"];
            }
        }];
    } else if (userModel.status == UserStatusApply) {
        __weak __typeof(self)wself = self;
        [[ToastComponent shareToastComponent] showLoading];
        [VoiceChatRTMManager agreeApply:userModel.roomID
                                           uid:userModel.uid
                                         block:^(RTMACKModel * _Nonnull model) {
            [[ToastComponent shareToastComponent] dismiss];
            if (model.result) {
                userModel.status = UserStatusApply;
                [wself updateDataLists:dataLists model:userModel];
            } else {
                [[ToastComponent shareToastComponent] showWithMessage:model.message];
            }
        }];
    } else {
        
    }
}

- (void)updateDataLists:(NSArray<VoiceChatUserModel *> *)dataLists
                  model:(VoiceChatUserModel *)model {
    for (int i = 0; i < dataLists.count; i++) {
        VoiceChatUserModel *currentModel = dataLists[i];
        if ([currentModel.uid isEqualToString:model.uid]) {
            NSMutableArray *mutableLists = [[NSMutableArray alloc] initWithArray:dataLists];
            [mutableLists replaceObjectAtIndex:i withObject:model];
            break;
        }
    }
}

- (void)removeDataLists:(NSArray<VoiceChatUserModel *> *)dataLists model:(VoiceChatUserModel *)model {
    VoiceChatUserModel *deleteModel = nil;
    for (int i = 0; i < dataLists.count; i++) {
        VoiceChatUserModel *currentModel = dataLists[i];
        if ([currentModel.uid isEqualToString:model.uid]) {
            deleteModel = currentModel;
            break;
        }
    }

    if (deleteModel) {
        NSMutableArray *mutableLists = [[NSMutableArray alloc] initWithArray:dataLists];
        [mutableLists removeObject:deleteModel];
        dataLists = [mutableLists copy];
    }
}

- (void)maskButtonAction {
    [self dismissUserListView];
}

- (void)dismissUserListView {
    _seatID = @"-1";
    [self.maskButton removeAllSubviews];
    [self.maskButton removeFromSuperview];
    self.maskButton = nil;
    if (self.dismissBlock) {
        self.dismissBlock();
    }
}

#pragma mark - Getter

- (VoiceChatRoomRaiseHandListsView *)applyListsView {
    if (!_applyListsView) {
        _applyListsView = [[VoiceChatRoomRaiseHandListsView alloc] init];
        _applyListsView.delegate = self;
        _applyListsView.backgroundColor = [UIColor colorFromRGBHexString:@"#0E0825" andAlpha:0.95 * 255];
    }
    return _applyListsView;
}

- (VoiceChatRoomAudienceListsView *)onlineListsView {
    if (!_onlineListsView) {
        _onlineListsView = [[VoiceChatRoomAudienceListsView alloc] init];
        _onlineListsView.delegate = self;
        _onlineListsView.backgroundColor = [UIColor colorFromRGBHexString:@"#0E0825" andAlpha:0.95 * 255];
    }
    return _onlineListsView;
}

- (UIButton *)maskButton {
    if (!_maskButton) {
        _maskButton = [[UIButton alloc] init];
        [_maskButton addTarget:self action:@selector(maskButtonAction) forControlEvents:UIControlEventTouchUpInside];
        [_maskButton setBackgroundColor:[UIColor clearColor]];
    }
    return _maskButton;
}

- (VoiceChatRoomTopSelectView *)topSelectView {
    if (!_topSelectView) {
        _topSelectView = [[VoiceChatRoomTopSelectView alloc] init];
        _topSelectView.delegate = self;
    }
    return _topSelectView;
}

- (VoiceChatRoomTopSeatView *)topSeatView {
    if (!_topSeatView) {
        _topSeatView = [[VoiceChatRoomTopSeatView alloc] init];
    }
    return _topSeatView;
}

- (void)dealloc {
    NSLog(@"dealloc %@",NSStringFromClass([self class]));
}

@end
