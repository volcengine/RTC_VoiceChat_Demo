//
//  VoiceChatRoomViewController.m
//  veRTC_Demo
//
//  Created by on 2021/5/18.
//  
//

#import "VoiceChatRoomListsViewController.h"
#import "VoiceChatCreateRoomViewController.h"
#import "VoiceChatRoomViewController.h"
#import "VoiceChatRoomTableView.h"
#import "VoiceChatRTCManager.h"

@interface VoiceChatRoomListsViewController () <VoiceChatRoomTableViewDelegate>

@property (nonatomic, strong) UIButton *createButton;
@property (nonatomic, strong) VoiceChatRoomTableView *roomTableView;
@property (nonatomic, copy) NSString *currentAppid;

@end

@implementation VoiceChatRoomListsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.bgView.hidden = NO;
    self.navView.backgroundColor = [UIColor clearColor];
    
    [self.view addSubview:self.roomTableView];
    [self.roomTableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.left.right.equalTo(self.view);
        make.top.equalTo(self.navView.mas_bottom);
    }];
    
    [self.view addSubview:self.createButton];
    [self.createButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(171, 50));
        make.centerX.equalTo(self.view);
        make.bottom.equalTo(self.view).offset(- 48 - [DeviceInforTool getVirtualHomeHeight]);
    }];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    self.navTitle = @"语音聊天室";
    [self.rightButton setImage:[UIImage imageNamed:@"refresh" bundleName:HomeBundleName] forState:UIControlStateNormal];
    
    [self loadDataWithGetLists];
}

- (void)rightButtonAction:(BaseButton *)sender {
    [super rightButtonAction:sender];
    
    [self loadDataWithGetLists];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
}

#pragma mark - load data

- (void)loadDataWithGetLists {
    __weak __typeof(self) wself = self;
    
    [VoiceChatRTMManager clearUser:^(RTMACKModel * _Nonnull model) {
        [VoiceChatRTMManager getActiveLiveRoomListWithBlock:^(NSArray<VoiceChatRoomModel *> * _Nonnull roomList, RTMACKModel * _Nonnull model) {
            if (model.result) {
                wself.roomTableView.dataLists = roomList;
            } else {
                wself.roomTableView.dataLists = @[];
                [[ToastComponent shareToastComponent] showWithMessage:model.message];
            }
        }];
    }];
}

#pragma mark - VoiceChatRoomTableViewDelegate

- (void)VoiceChatRoomTableView:(VoiceChatRoomTableView *)VoiceChatRoomTableView didSelectRowAtIndexPath:(VoiceChatRoomModel *)model {
    VoiceChatRoomViewController *next = [[VoiceChatRoomViewController alloc]
                                         initWithRoomModel:model];
    [self.navigationController pushViewController:next animated:YES];
}

#pragma mark - Touch Action

- (void)createButtonAction {
    VoiceChatCreateRoomViewController *next = [[VoiceChatCreateRoomViewController alloc] init];
    [self.navigationController pushViewController:next animated:YES];
}

#pragma mark - getter

- (UIButton *)createButton {
    if (!_createButton) {
        _createButton = [[UIButton alloc] init];
        _createButton.backgroundColor = [UIColor colorFromHexString:@"#4080FF"];
        [_createButton addTarget:self action:@selector(createButtonAction) forControlEvents:UIControlEventTouchUpInside];
        _createButton.layer.cornerRadius = 25;
        _createButton.layer.masksToBounds = YES;
        
        UIImageView *iconImageView = [[UIImageView alloc] init];
        iconImageView.image = [UIImage imageNamed:@"voice_add" bundleName:HomeBundleName];
        [_createButton addSubview:iconImageView];
        [iconImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(20, 20));
            make.centerY.equalTo(_createButton);
            make.left.mas_equalTo(40);
        }];
        
        UILabel *titleLabel = [[UILabel alloc] init];
        titleLabel.text = @"创建房间";
        titleLabel.textColor = [UIColor whiteColor];
        titleLabel.font = [UIFont systemFontOfSize:16 weight:UIFontWeightRegular];
        [_createButton addSubview:titleLabel];
        [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.equalTo(_createButton);
            make.left.equalTo(iconImageView.mas_right).offset(8);
        }];
    }
    return _createButton;
}

- (VoiceChatRoomTableView *)roomTableView {
    if (!_roomTableView) {
        _roomTableView = [[VoiceChatRoomTableView alloc] init];
        _roomTableView.delegate = self;
    }
    return _roomTableView;
}

- (void)dealloc {
    [[VoiceChatRTCManager shareRtc] disconnect];
    [PublicParameterComponent clear];
}


@end
