//
//  ScenesViewController.m
//  veRTC_Demo
//
//  Created by on 2021/5/21.
//
//

#import "ScenesViewController.h"
#import "FeedbackManagerProtocol.h"
#import "ScenesItemButton.h"
#import "Masonry.h"
#import "Core.h"

@interface ScenesViewController ()

@property (nonatomic, strong) UIImageView *iconImageView;
@property (nonatomic, strong) UIScrollView *scrollView;
@property (nonatomic, strong) NSMutableArray *dataArray;
@property (nonatomic, strong) FeedbackManagerProtocol *feedbackManager;

@end

@implementation ScenesViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self addBgGradientLayer];
    
    [self.view addSubview:self.iconImageView];
    [self.iconImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view);
        make.top.mas_equalTo(45 + [DeviceInforTool getStatusBarHight]);
    }];
    
    // scrollView
    [self.view addSubview:self.scrollView];
    [self.scrollView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(20);
        make.right.mas_equalTo(-20);
        make.top.mas_equalTo(125 + [DeviceInforTool getStatusBarHight]);
        make.bottom.mas_equalTo(-100);
    }];
    
    UIView *contenView = [[UIView alloc] init];
    [self.scrollView addSubview:contenView];
    [contenView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.scrollView);
    }];
    
    // add buttons
    for (int i = 0; i < self.dataArray.count; i++) {
        ScenesItemButton *button = [[ScenesItemButton alloc] init];
        [contenView addSubview:button];
        
        SceneButtonModel *model = self.dataArray[i];
        button.model = model;
        
        [button addTarget:self action:@selector(sceneButtonAction:) forControlEvents:UIControlEventTouchUpInside];
        
        [button mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.left.equalTo(contenView);
            make.top.mas_equalTo(i*(120 + 20));
            make.height.mas_equalTo(120);
        }];
    }
    
    CGFloat scrollviewHeight = self.dataArray.count * (120 + 20);
    [contenView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.height.mas_equalTo(scrollviewHeight);
        make.width.equalTo(self.scrollView).offset(0);
    }];
    
    [self feedbackManager];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}

#pragma mark - Touch Action

- (void)sceneButtonAction:(ScenesItemButton *)button {
    button.enabled = NO;
    BaseHomeDemo *scenes = (BaseHomeDemo *)button.model.scenes;
    // Open the corresponding scene home page
    [[ToastComponent shareToastComponent] showLoading];
    [scenes pushDemoViewControllerBlock:^(BOOL result) {
        button.enabled = YES;
        [[ToastComponent shareToastComponent] dismiss];
    }];
}

#pragma mark - Private Action

- (void)addBgGradientLayer {
    UIColor *startColor = [UIColor colorFromHexString:@"#30394A"];
    UIColor *endColor = [UIColor colorFromHexString:@"#1D2129"];
    CAGradientLayer *gradientLayer = [CAGradientLayer layer];
    gradientLayer.frame = self.view.bounds;
    gradientLayer.colors = @[(__bridge id)[startColor colorWithAlphaComponent:1.0].CGColor,
                             (__bridge id)[endColor colorWithAlphaComponent:1.0].CGColor];
    gradientLayer.startPoint = CGPointMake(.0, .0);
    gradientLayer.endPoint = CGPointMake(.0, 1.0);
    [self.view.layer addSublayer:gradientLayer];
}

#pragma mark - getter

- (UIScrollView *)scrollView {
    if (_scrollView == nil) {
        _scrollView = [[UIScrollView alloc] init];
        _scrollView.canCancelContentTouches = NO;
        _scrollView.showsVerticalScrollIndicator = NO;
        _scrollView.showsHorizontalScrollIndicator = NO;
    }
    return _scrollView;
}

- (UIImageView *)iconImageView {
    if (!_iconImageView) {
        _iconImageView = [[UIImageView alloc] init];
        _iconImageView.image = [UIImage imageNamed:@"logo_icon"];
    }
    return _iconImageView;
}

- (NSMutableArray *)dataArray {
    if (_dataArray == nil) {
        _dataArray = [NSMutableArray array];
        
        NSObject *chorusDemo = [[NSClassFromString(@"ChorusDemo") alloc] init];
        if (chorusDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = @"双人合唱";
            model.iconName = @"menu_chorus";
            model.scenes = chorusDemo;
            [_dataArray addObject:model];
        }
        
        NSObject *liveShareDemo = [[NSClassFromString(@"LiveShareDemo") alloc] init];
        if (liveShareDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = @"一起看直播";
            model.iconName = @"menu_live_share";
            model.scenes = liveShareDemo;
            [_dataArray addObject:model];
        }
        
        NSObject *feedShareDemo = [[NSClassFromString(@"FeedShareDemo") alloc] init];
        if (feedShareDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = @"一起看抖音";
            model.iconName = @"menu_feedshare";
            model.scenes = feedShareDemo;
            [_dataArray addObject:model];
        }

        NSObject *videoCallDemo = [[NSClassFromString(@"VideoCallDemo") alloc] init];
        if (videoCallDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = @"音视频通话";
            model.iconName = @"menu_videocall";
            model.scenes = videoCallDemo;
            [_dataArray addObject:model];
        }
        
        NSObject *gameRoomDemo = [[NSClassFromString(@"GameRoomDemo") alloc] init];
        if (gameRoomDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = @"游戏房";
            model.iconName = @"menu_game";
            model.scenes = gameRoomDemo;
            [_dataArray addObject:model];
        }

        NSObject *videoChatDemo = [[NSClassFromString(@"VideoChatDemo") alloc] init];
        if (videoChatDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = @"视频互动";
            model.iconName = @"menu_videochat";
            model.scenes = videoChatDemo;
            [_dataArray addObject:model];
        }
        
        NSObject *ktvDemo = [[NSClassFromString(@"KTVDemo") alloc] init];
        if (ktvDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = @"在线KTV";
            model.iconName = @"menu_ktv";
            model.scenes = ktvDemo;
            [_dataArray addObject:model];
        }
        
        NSObject *liveDemo = [[NSClassFromString(@"LiveDemo") alloc] init];
        if (liveDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = @"互动直播";
            model.iconName = @"menu_live";
            model.bgName = @"menu_live_icon_bg";
            model.scenes = liveDemo;
            [_dataArray addObject:model];
        }
        
        NSObject *voiceChatDemo = [[NSClassFromString(@"VoiceChatDemo") alloc] init];
        if (voiceChatDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = @"语音聊天室";
            model.iconName = @"menu_voicechat";
            model.scenes = voiceChatDemo;
            [_dataArray addObject:model];
        }
        
        NSObject *voiceDemo = [[NSClassFromString(@"VoiceDemo") alloc] init];
        if (voiceDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = @"语音沙龙";
            model.iconName = @"menu_voice";
            model.bgName = @"menu_voice_icon_bg";
            model.scenes = voiceDemo;
            [_dataArray addObject:model];
        }
        
        NSObject *meetingDemo = [[NSClassFromString(@"MeetingDemo") alloc] init];
        if (meetingDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = @"视频会议";
            model.iconName = @"menu_metting";
            model.bgName = @"menu_meeting_icon_bg";
            model.scenes = meetingDemo;
            [_dataArray addObject:model];
        }
        
        NSObject *eduDemo = [[NSClassFromString(@"EduDemo") alloc] init];
        if (eduDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = @"在线课堂";
            model.iconName = @"menu_edu";
            model.bgName = @"menu_edu_icon_bg";
            model.scenes = eduDemo;
            [_dataArray addObject:model];
        }
        
    }
    return _dataArray;
}

- (FeedbackManagerProtocol *)feedbackManager {
    if (!_feedbackManager) {
        _feedbackManager = [[FeedbackManagerProtocol alloc] initWithSuperView:self.view];
    }
    return _feedbackManager;
}

@end
