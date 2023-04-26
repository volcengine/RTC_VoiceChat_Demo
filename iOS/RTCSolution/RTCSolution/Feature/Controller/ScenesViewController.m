// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "ScenesViewController.h"
#import "FeedbackManagerProtocol.h"
#import "ScenesItemButton.h"
#import "LocalizatorBundle.h"
#import "Masonry.h"
#import "ToolKit.h"

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
        make.height.mas_equalTo(30);
    }];
    
    // ScrollView
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
    
    // Add buttons
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
    // 打开对应场景首页
    button.enabled = NO;
    BaseHomeDemo *scenes = (BaseHomeDemo *)button.model.scenes;
    scenes.scenesName = button.model.scenesName;
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

#pragma mark - Getter

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
        _iconImageView.backgroundColor = [UIColor clearColor];
        _iconImageView.image = [UIImage imageNamed:@"logo_icon"];
        _iconImageView.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _iconImageView;
}

- (NSMutableArray *)dataArray {
    if (_dataArray == nil) {
        _dataArray = [NSMutableArray array];
        
        NSObject *chorusDemo = [[NSClassFromString(@"ChorusDemo") alloc] init];
        if (chorusDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = LocalizedStringFromBundle(@"chorus_ktv", @"");
            model.iconName = @"menu_chorus";
            model.scenes = chorusDemo;
            model.scenesName = @"owc";
            [_dataArray addObject:model];
        }
        
        NSObject *liveShareDemo = [[NSClassFromString(@"LiveShareDemo") alloc] init];
        if (liveShareDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = LocalizedStringFromBundle(@"watch_live_together", @"");
            model.iconName = @"menu_live_share";
            model.scenes = liveShareDemo;
            model.scenesName = @"twv";
            [_dataArray addObject:model];
        }
        
        NSObject *feedShareDemo = [[NSClassFromString(@"FeedShareDemo") alloc] init];
        if (feedShareDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = LocalizedStringFromBundle(@"watch_video_together", @"");
            model.iconName = @"menu_feedshare";
            model.scenes = feedShareDemo;
            model.scenesName = @"tw";
            [_dataArray addObject:model];
        }

        NSObject *videoCallDemo = [[NSClassFromString(@"VideoCallDemo") alloc] init];
        if (videoCallDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = LocalizedStringFromBundle(@"audio_video_calls", @"");
            model.iconName = @"menu_videocall";
            model.scenes = videoCallDemo;
            model.scenesName = @"videocall";
            [_dataArray addObject:model];
        }
        
        NSObject *gameRoomDemo = [[NSClassFromString(@"GameRoomDemo") alloc] init];
        if (gameRoomDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = LocalizedStringFromBundle(@"game_room_scenes", @"");
            model.iconName = @"menu_game";
            model.scenes = gameRoomDemo;
            model.scenesName = @"gr";
            [_dataArray addObject:model];
        }

        NSObject *videoChatDemo = [[NSClassFromString(@"VideoChatDemo") alloc] init];
        if (videoChatDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = LocalizedStringFromBundle(@"video_chat_scenes", @"");
            model.iconName = @"menu_videochat";
            model.scenes = videoChatDemo;
            model.scenesName = @"videochat";
            [_dataArray addObject:model];
        }
        
        NSObject *ktvDemo = [[NSClassFromString(@"KTVDemo") alloc] init];
        if (ktvDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = LocalizedStringFromBundle(@"ktv", @"");
            model.iconName = @"menu_ktv";
            model.scenes = ktvDemo;
            model.scenesName = @"ktv";
            [_dataArray addObject:model];
        }
        
        NSObject *liveDemo = [[NSClassFromString(@"LiveDemo") alloc] init];
        if (liveDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = LocalizedStringFromBundle(@"interactive_live_scenes", @"");
            model.iconName = @"menu_live";
            model.bgName = @"menu_live_icon_bg";
            model.scenes = liveDemo;
            model.scenesName = @"live";
            [_dataArray addObject:model];
        }
        
        NSObject *voiceChatDemo = [[NSClassFromString(@"VoiceChatDemo") alloc] init];
        if (voiceChatDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = LocalizedStringFromBundle(@"voice_chat_scenes", @"");
            model.iconName = @"menu_voicechat";
            model.scenes = voiceChatDemo;
            model.scenesName = @"svc";
            [_dataArray addObject:model];
        }
        
        NSObject *voiceDemo = [[NSClassFromString(@"VoiceDemo") alloc] init];
        if (voiceDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = LocalizedStringFromBundle(@"voice_salon", @"");
            model.iconName = @"menu_voice";
            model.bgName = @"menu_voice_icon_bg";
            model.scenes = voiceDemo;
            model.scenesName = @"cs";
            [_dataArray addObject:model];
        }
        
        NSObject *meetingDemo = [[NSClassFromString(@"MeetingDemo") alloc] init];
        if (meetingDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = LocalizedStringFromBundle(@"meeting", @"");
            model.iconName = @"menu_metting";
            model.bgName = @"menu_meeting_icon_bg";
            model.scenes = meetingDemo;
            model.scenesName = @"meeting";
            [_dataArray addObject:model];
        }
        
        NSObject *eduDemo = [[NSClassFromString(@"EduDemo") alloc] init];
        if (eduDemo) {
            SceneButtonModel *model = [[SceneButtonModel alloc] init];
            model.title = LocalizedStringFromBundle(@"online_edu", @"");
            model.iconName = @"menu_edu";
            model.bgName = @"menu_edu_icon_bg";
            model.scenes = eduDemo;
            model.scenesName = @"edu";
            [_dataArray addObject:model];
        }
        
    }
    return _dataArray;
}

- (FeedbackManagerProtocol *)feedbackManager {
    if (!_feedbackManager) {
        NSMutableDictionary *dic = [[NSMutableDictionary alloc] init];
        for (int i = 0; i < self.dataArray.count; i++) {
            SceneButtonModel *sceneButtonModel = self.dataArray[i];
            if (NOEmptyStr(sceneButtonModel.scenesName) &&
                NOEmptyStr(sceneButtonModel.title)) {
                [dic setValue:sceneButtonModel.title
                       forKey:sceneButtonModel.scenesName];
            }
        }
        _feedbackManager = [[FeedbackManagerProtocol alloc] initWithSuperView:self.view scenesDic:[dic copy]];
    }
    return _feedbackManager;
}

@end
