//
//  VoiceChatSeatComponent.m
//  veRTC_Demo
//
//  Created by on 2021/12/1.
//  
//

#import "VoiceChatSeatComponent.h"
#import "VoiceChatSeatView.h"

@interface VoiceChatSeatComponent () <VoiceChatSheetViewDelegate>

@property (nonatomic, weak) VoiceChatSeatView *seatView;
@property (nonatomic, weak) VoiceChatSheetView *sheetView;
@property (nonatomic, weak) UIView *superView;
@property (nonatomic, strong) VoiceChatUserModel *loginUserModel;

@end

@implementation VoiceChatSeatComponent

- (instancetype)initWithSuperView:(UIView *)superView {
    self = [super init];
    if (self) {
        _superView = superView;
    }
    return self;
}

#pragma mark - Publish Action

- (void)showSeatView:(NSArray<VoiceChatSeatModel *> *)seatList
      loginUserModel:(VoiceChatUserModel *)loginUserModel {
    _loginUserModel = loginUserModel;
    
    if (!_seatView) {
        VoiceChatSeatView *seatView = [[VoiceChatSeatView alloc] init];
        [_superView addSubview:seatView];
        [seatView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(44);
            make.right.mas_equalTo(-44);
            make.height.mas_equalTo(180);
            make.top.mas_equalTo(175 + [DeviceInforTool getStatusBarHight]);
        }];
        _seatView = seatView;
    }
    _seatView.seatList = seatList;
    
    __weak __typeof(self) wself = self;
    _seatView.clickBlock = ^(VoiceChatSeatModel * _Nonnull seatModel) {
        VoiceChatSheetView *sheetView = [[VoiceChatSheetView alloc] init];
        sheetView.delegate = wself;
        [wself.superView addSubview:sheetView];
        [sheetView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(wself.superView);
        }];
        [sheetView showWithSeatModel:seatModel
                      loginUserModel:wself.loginUserModel];
        wself.sheetView = sheetView;
    };
}

- (void)addSeatModel:(VoiceChatSeatModel *)seatModel {
    [_seatView addSeatModel:seatModel];
    [self updateSeatModel:seatModel];
}

- (void)removeUserModel:(VoiceChatUserModel *)userModel {
    [_seatView removeUserModel:userModel];
    if ([userModel.uid isEqualToString:_loginUserModel.uid]) {
        _loginUserModel = userModel;
    }
    NSString *sheetUid = self.sheetView.seatModel.userModel.uid;
    if (self.sheetView &&
        [userModel.uid isEqualToString:sheetUid]) {
        // update the new one to open the sheet user
        [self.sheetView dismiss];
    }
}

- (void)updateSeatModel:(VoiceChatSeatModel *)seatModel {
    [_seatView updateSeatModel:seatModel];
    if ([seatModel.userModel.uid isEqualToString:_loginUserModel.uid]) {
        _loginUserModel = seatModel.userModel;
    }
    NSString *sheetUid = self.sheetView.seatModel.userModel.uid;
    if (self.sheetView &&
        [seatModel.userModel.uid isEqualToString:sheetUid]) {
        // update the new one to open the sheet user
        [self.sheetView dismiss];
    }
}

- (void)updateSeatVolume:(NSDictionary *)volumeDic {
    [_seatView updateSeatVolume:volumeDic];
}

#pragma mark - VoiceChatSheetViewDelegate

- (void)voiceChatSheetView:(VoiceChatSheetView *)voiceChatSheetView
               clickButton:(VoiceChatSheetStatus)sheetState {
    if (sheetState == VoiceChatSheetStatusInvite) {
        if ([self.delegate respondsToSelector:@selector
             (voiceChatSeatComponent:clickButton:sheetStatus:)]) {
            [self.delegate voiceChatSeatComponent:self
                                       clickButton:voiceChatSheetView.seatModel
                                       sheetStatus:sheetState];
        }
        [voiceChatSheetView dismiss];
    } else if (sheetState == VoiceChatSheetStatusKick) {
        [self loadDataManager:5 sheetView:voiceChatSheetView];
    } else if (sheetState == VoiceChatSheetStatusOpenMic) {
        [self loadDataManager:4 sheetView:voiceChatSheetView];
    } else if (sheetState == VoiceChatSheetStatusCloseMic) {
        [self loadDataManager:3 sheetView:voiceChatSheetView];
    } else if (sheetState == VoiceChatSheetStatusLock) {
        [self showAlertWithLockSeat:voiceChatSheetView];
    } else if (sheetState == VoiceChatSheetStatusUnlock) {
        [self loadDataManager:2 sheetView:voiceChatSheetView];
    } else if (sheetState == VoiceChatSheetStatusApply) {
        [self loadDataApply:voiceChatSheetView];
    } else if (sheetState == VoiceChatSheetStatusLeave) {
        [self loadDataLeave:voiceChatSheetView];
    } else {
        //error
    }
}

#pragma mark - Private Action

- (void)loadDataManager:(NSInteger)type
              sheetView:(VoiceChatSheetView *)voiceChatSheetView {
    NSString *seatID = [NSString stringWithFormat:@"%ld", (long)voiceChatSheetView.seatModel.index];
    [[ToastComponent shareToastComponent] showLoading];
    [VoiceChatRTMManager managerSeat:voiceChatSheetView.loginUserModel.roomID
                                     seatID:seatID
                                       type:type
                                      block:^(RTMACKModel * _Nonnull model) {
        [[ToastComponent shareToastComponent] dismiss];
        if (!model.result) {
            [[ToastComponent shareToastComponent] showWithMessage:@"操作失败，请重试"];
        } else {
            [voiceChatSheetView dismiss];
        }
    }];
}

- (void)loadDataApply:(VoiceChatSheetView *)voiceChatSheetView {
    NSString *seatID = [NSString stringWithFormat:@"%ld", (long)voiceChatSheetView.seatModel.index];
    [[ToastComponent shareToastComponent] showLoading];
    [VoiceChatRTMManager applyInteract:voiceChatSheetView.loginUserModel.roomID
                                       seatID:seatID
                                        block:^(BOOL isNeedApply,
                                                RTMACKModel * _Nonnull model) {
        [[ToastComponent shareToastComponent] dismiss];
        if (!model.result) {
            [[ToastComponent shareToastComponent] showWithMessage:model.message];
        } else {
            if (isNeedApply) {
                voiceChatSheetView.loginUserModel.status = UserStatusApply;
                [[ToastComponent shareToastComponent] showWithMessage:@"已向主播发送申请"];
            }
            [voiceChatSheetView dismiss];
        }
    }];
}

- (void)loadDataLeave:(VoiceChatSheetView *)voiceChatSheetView {
    NSString *seatID = [NSString stringWithFormat:@"%ld", (long)voiceChatSheetView.seatModel.index];
    [[ToastComponent shareToastComponent] showLoading];
    [VoiceChatRTMManager finishInteract:voiceChatSheetView.loginUserModel.roomID
                                        seatID:seatID
                                         block:^(RTMACKModel * _Nonnull model) {
        [[ToastComponent shareToastComponent] dismiss];
        if (!model.result) {
            [[ToastComponent shareToastComponent] showWithMessage:@"操作失败，请重试"];
        } else {
            [voiceChatSheetView dismiss];
        }
    }];
}

- (void)showAlertWithLockSeat:(VoiceChatSheetView *)voiceChatSheetView {
    AlertActionModel *alertModel = [[AlertActionModel alloc] init];
    alertModel.title = @"确定";
    AlertActionModel *cancelModel = [[AlertActionModel alloc] init];
    cancelModel.title = @"取消";
    [[AlertActionManager shareAlertActionManager] showWithMessage:@"确定封锁麦位？封锁麦位后，观众无法在此麦位上麦； 且此麦位上嘉宾将被下麦" actions:@[ cancelModel, alertModel ]];
    __weak __typeof(self) wself = self;
    alertModel.alertModelClickBlock = ^(UIAlertAction *_Nonnull action) {
        if ([action.title isEqualToString:@"确定"]) {
            [wself loadDataManager:1 sheetView:voiceChatSheetView];
        }
    };
}



@end
