#import "VoiceChatRTCManager.h"
#import "AlertActionManager.h"
#import "SystemAuthority.h"

@interface VoiceChatRTCManager () <ByteRTCEngineDelegate>

@property (nonatomic, strong) VoiceChatRoomParamInfoModel *paramInfoModel;
@property (nonatomic, assign) int audioMixingID;
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
    //关闭 本地音频/视频采集
    //Turn on/off local audio capture
    [self.rtcEngineKit stopAudioCapture];
    [self.rtcEngineKit stopVideoCapture];

    //设置音频路由模式，YES 扬声器/NO 听筒
    //Set the audio routing mode, YES speaker/NO earpiece
    [self.rtcEngineKit setAudioPlaybackDevice:ByteRTCAudioPlaybackDeviceSpeakerphone];
    
    //开启/关闭发言者音量键控
    //Turn on/off speaker volume keying
    [self.rtcEngineKit setAudioVolumeIndicationInterval:300];
    
    //设置用户为隐身状态
    //Set user to incognito state
    [self.rtcEngineKit setUserVisibility:NO];

    //加入房间，开始连麦,需要申请AppId和Token
    //Join the room, start connecting the microphone, you need to apply for AppId and Token
    ByteRTCUserInfo *userInfo = [[ByteRTCUserInfo alloc] init];
    userInfo.userId = uid;
    
    ByteRTCRoomConfig *config = [[ByteRTCRoomConfig alloc] init];
    config.profile = ByteRTCRoomProfileLiveBroadcasting;
    config.isAutoPublish = YES;
    config.isAutoSubscribeAudio = YES;
    
    [self.rtcEngineKit joinRoomByKey:token
                        roomId:roomID
                      userInfo:userInfo
                 rtcRoomConfig:config];
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
                [self muteLocalAudioStream:NO];
                [self.rtcEngineKit setUserVisibility:YES];
            }
        }];
    } else {
        [self.rtcEngineKit stopAudioCapture];
        [self.rtcEngineKit setUserVisibility:NO];
    }
}

- (void)muteLocalAudioStream:(BOOL)isMute {
    //开启/关闭 本地音频推流
    //Turn on/off local audio stream
    if (isMute) {
        [self.rtcEngineKit muteLocalAudio:ByteRTCMuteStateOn];
    } else {
        [self.rtcEngineKit muteLocalAudio:ByteRTCMuteStateOff];
    }
}

- (void)leaveRTCRoom {
    //离开频道
    //Leave the channel
    [self makeCoHost:NO];
    [self muteLocalAudioStream:YES];
    [self.rtcEngineKit leaveRoom];
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

#pragma mark - ByteRTCEngineDelegate

- (void)rtcEngine:(ByteRTCEngineKit *_Nonnull)engine onLocalStreamStats:(const ByteRTCLocalStreamStats * _Nonnull)stats {
    if (stats.audio_stats.audioLossRate > 0) {
        self.paramInfoModel.sendLossRate = [NSString stringWithFormat:@"%.0f",stats.audio_stats.audioLossRate];
    }
    if (stats.audio_stats.rtt > 0) {
        self.paramInfoModel.rtt = [NSString stringWithFormat:@"%.0ld",(long)stats.audio_stats.rtt];
    }
    [self updateRoomParamInfoModel];
}

- (void)rtcEngine:(ByteRTCEngineKit * _Nonnull)engine onRemoteStreamStats:(const ByteRTCRemoteStreamStats * _Nonnull)stats {
    if (stats.audio_stats.audioLossRate > 0) {
        self.paramInfoModel.receivedLossRate = [NSString stringWithFormat:@"%.0f",stats.audio_stats.audioLossRate];
    }
    [self updateRoomParamInfoModel];
}

- (void)rtcEngine:(ByteRTCEngineKit * _Nonnull)engine onAudioVolumeIndication:(NSArray<ByteRTCAudioVolumeInfo *> * _Nonnull)speakers totalRemoteVolume:(NSInteger)totalRemoteVolume {
    NSMutableDictionary *dic = [[NSMutableDictionary alloc] init];
    for (int i = 0; i < speakers.count; i++) {
        ByteRTCAudioVolumeInfo *model = speakers[i];
        [dic setValue:@(model.linearVolume) forKey:model.uid];
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
