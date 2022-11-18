#import "VoiceChatRTCManager.h"
#import "AlertActionManager.h"
#import "SystemAuthority.h"

@interface VoiceChatRTCManager () <ByteRTCVideoDelegate>

@property (nonatomic, strong) VoiceChatRoomParamInfoModel *paramInfoModel;
@property (nonatomic, assign) int audioMixingID;
@property (nonatomic, assign) BOOL isEnableAudioCapture;

@end

@implementation VoiceChatRTCManager

+ (VoiceChatRTCManager *_Nullable)shareRtc {
    static VoiceChatRTCManager *rtcManager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        rtcManager = [[VoiceChatRTCManager alloc] init];
    });
    return rtcManager;
}

#pragma mark - Publish Action

- (instancetype)init {
    self = [super init];
    if (self) {
        _audioMixingID = 3001;
    }
    return self;
}

- (void)joinRTCRoomWithToken:(NSString *)token
                      roomID:(NSString *)roomID
                         uid:(NSString *)uid {
    self.rtcRoom = [self.rtcEngineKit createRTCRoom:roomID];
    self.rtcRoom.delegate = self;
    self.isEnableAudioCapture = NO;
    
    //关闭 本地音频/视频采集
    //Turn on/off local audio capture
    [self.rtcEngineKit stopAudioCapture];
    [self.rtcEngineKit stopVideoCapture];

    //设置音频路由模式，YES 扬声器/NO 听筒
    //Set the audio routing mode, YES speaker/NO earpiece
    [self.rtcEngineKit setDefaultAudioRoute:ByteRTCAudioRouteSpeakerphone];

    //开启/关闭发言者音量键控
    //Turn on/off speaker volume keying
    ByteRTCAudioPropertiesConfig *audioPropertiesConfig = [[ByteRTCAudioPropertiesConfig alloc] init];
    audioPropertiesConfig.interval = 300;
    [self.rtcEngineKit enableAudioPropertiesReport:audioPropertiesConfig];
    
    //设置本地渲染和编码镜像(本地和远端相同)
    //Set up the local rendering and encoding mirror (the same local and remote)
    [self.rtcEngineKit setLocalVideoMirrorType:ByteRTCMirrorTypeRenderAndEncoder];
    
    //设置用户为隐身状态
    //Set user to incognito state
    [self.rtcRoom setUserVisibility:NO];

    //加入房间，开始连麦,需要申请AppId和Token
    //Join the room, start connecting the microphone, you need to apply for AppId and Token
    ByteRTCUserInfo *userInfo = [[ByteRTCUserInfo alloc] init];
    userInfo.userId = uid;
    
    ByteRTCRoomConfig *config = [[ByteRTCRoomConfig alloc] init];
    config.profile = ByteRTCRoomProfileInteractivePodcast;
    config.isAutoPublish = YES;
    config.isAutoSubscribeAudio = YES;
    
    [self.rtcRoom joinRoomByToken:token userInfo:userInfo roomConfig:config];
}

#pragma mark - rtc method

- (void)makeCoHost:(BOOL)isCoHost {
    //开启/关闭 本地音频采集
    //Turn on/off local audio capture
    if (isCoHost) {
        [SystemAuthority authorizationStatusWithType:AuthorizationTypeAudio
                                               block:^(BOOL isAuthorize) {
            if (isAuthorize) {
                [self.rtcEngineKit startAudioCapture];
                [self.rtcRoom setUserVisibility:YES];
                [self muteLocalAudioStream:NO];
                self.isEnableAudioCapture = YES;
            }
        }];
    } else {
        [self.rtcEngineKit stopAudioCapture];
        [self.rtcRoom setUserVisibility:NO];
        self.isEnableAudioCapture = NO;
    }
}

- (void)muteLocalAudioStream:(BOOL)isMute {
    //开启/关闭 本地音频推流
    //Turn on/off local audio stream
    if (isMute) {
        [self.rtcRoom unpublishStream:ByteRTCMediaStreamTypeAudio];
    } else {
        [self.rtcRoom publishStream:ByteRTCMediaStreamTypeAudio];
    }
}

- (void)leaveRTCRoom {
    //离开频道
    //Leave the channel
    [self makeCoHost:NO];
    [self muteLocalAudioStream:YES];
    [self.rtcRoom leaveRoom];
    [self.rtcRoom destroy];
    self.rtcRoom = nil;
}

#pragma mark - Background Music Method

- (void)startBackgroundMusic:(NSString *)filePath {
    ByteRTCAudioMixingManager *audioMixingManager = [self.rtcEngineKit getAudioMixingManager];
    
    ByteRTCAudioMixingConfig *config = [[ByteRTCAudioMixingConfig alloc] init];
    config.type = ByteRTCAudioMixingTypePlayoutAndPublish;
    config.playCount = -1;
    [audioMixingManager startAudioMixing:_audioMixingID filePath:filePath config:config];
}

- (void)stopBackgroundMusic {
    ByteRTCAudioMixingManager *audioMixingManager = [self.rtcEngineKit getAudioMixingManager];
    [audioMixingManager stopAudioMixing:_audioMixingID];
}

- (void)pauseBackgroundMusic {
    ByteRTCAudioMixingManager *audioMixingManager = [self.rtcEngineKit getAudioMixingManager];
    
    [audioMixingManager pauseAudioMixing:_audioMixingID];
}

- (void)resumeBackgroundMusic {
    ByteRTCAudioMixingManager *audioMixingManager = [self.rtcEngineKit getAudioMixingManager];
    
    [audioMixingManager resumeAudioMixing:_audioMixingID];
}

- (void)setRecordingVolume:(NSInteger)volume {
    // 设置麦克风采集音量
    // Set the volume of the mixed music
    [self.rtcEngineKit setCaptureVolume:ByteRTCStreamIndexMain volume:(int)volume];
}

- (void)setMusicVolume:(NSInteger)volume {
    // 设置混音音乐音量
    // Set the volume of the mixed music
    ByteRTCAudioMixingManager *audioMixingManager = [self.rtcEngineKit getAudioMixingManager];
    
    [audioMixingManager setAudioMixingVolume:_audioMixingID volume:(int)volume type:ByteRTCAudioMixingTypePlayoutAndPublish];
}

#pragma mark - ByteRTCVideoDelegate

- (void)rtcRoom:(ByteRTCRoom *)rtcRoom onNetworkQuality:(ByteRTCNetworkQualityStats *)localQuality remoteQualities:(NSArray<ByteRTCNetworkQualityStats *> *)remoteQualities {
    if (self.isEnableAudioCapture) {
        // 开启音频采集的用户，数据传输往返时延。
        self.paramInfoModel.rtt = [NSString stringWithFormat:@"%.0ld",(long)localQuality.rtt];
    } else {
        // 关闭音频采集的用户，数据传输往返时延。
        self.paramInfoModel.rtt = [NSString stringWithFormat:@"%.0ld",(long)remoteQualities.firstObject.rtt];
    }
    // 下行网络质量评分
    self.paramInfoModel.rxQuality = localQuality.rxQuality;
    // 上行网络质量评分
    self.paramInfoModel.txQuality = localQuality.txQuality;
    [self updateRoomParamInfoModel];
}

- (void)rtcEngine:(ByteRTCVideo *)engine onRemoteAudioPropertiesReport:(NSArray<ByteRTCRemoteAudioPropertiesInfo *> *)audioPropertiesInfos totalRemoteVolume:(NSInteger)totalRemoteVolume {
    NSMutableDictionary *dic = [[NSMutableDictionary alloc] init];
    for (int i = 0; i < audioPropertiesInfos.count; i++) {
        ByteRTCRemoteAudioPropertiesInfo *model = audioPropertiesInfos[i];
        [dic setValue:@(model.audioPropertiesInfo.linearVolume) forKey:model.streamKey.userId];
    }
    if ([self.delegate respondsToSelector:@selector(voiceChatRTCManager:reportAllAudioVolume:)]) {
        [self.delegate voiceChatRTCManager:self reportAllAudioVolume:dic];
    }
}

#pragma mark - Private Action

- (void)updateRoomParamInfoModel {
    dispatch_queue_async_safe(dispatch_get_main_queue(), ^{
        if ([self.delegate respondsToSelector:@selector(voiceChatRTCManager:changeParamInfo:)]) {
            [self.delegate voiceChatRTCManager:self changeParamInfo:self.paramInfoModel];
        }
    });
}


#pragma mark - getter

- (VoiceChatRoomParamInfoModel *)paramInfoModel {
    if (!_paramInfoModel) {
        _paramInfoModel = [[VoiceChatRoomParamInfoModel alloc] init];
    }
    return _paramInfoModel;
}

@end
