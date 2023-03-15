#import "BaseRTCManager.h"
#import "VoiceChatRTCManager.h"
#import <VolcEngineRTC/objc/ByteRTCVideo.h>
#import "VoiceChatRoomParamInfoModel.h"

NS_ASSUME_NONNULL_BEGIN
@class VoiceChatRTCManager;
@protocol VoiceChatRTCManagerDelegate <NSObject>

- (void)voiceChatRTCManager:(VoiceChatRTCManager *)voiceChatRTCManager changeParamInfo:(VoiceChatRoomParamInfoModel *)model;

- (void)voiceChatRTCManager:(VoiceChatRTCManager *_Nonnull)voiceChatRTCManager reportLocalAudioVolume:(NSInteger)volume;

- (void)voiceChatRTCManager:(VoiceChatRTCManager *_Nonnull)voiceChatRTCManager reportAllAudioVolume:(NSDictionary<NSString *, NSNumber *> *_Nonnull)volumeInfo;

@end

@interface VoiceChatRTCManager : BaseRTCManager

@property (nonatomic, weak) id<VoiceChatRTCManagerDelegate> delegate;

/*
 * RTC Manager Singletons
 */
+ (VoiceChatRTCManager *_Nullable)shareRtc;

#pragma mark - Base Method

/**
 * Join room
 * @param token token
 * @param roomID roomID
 * @param uid uid
 */
- (void)joinRTCRoomWithToken:(NSString *)token roomID:(NSString *)roomID uid:(NSString *)uid;

/*
 * Switch local audio capture
 * @param enable ture:Turn on audio capture false：Turn off audio capture
 */
- (void)makeCoHost:(BOOL)isCoHost;

/*
 * Switch local audio capture
 * @param mute ture:Turn on audio capture false：Turn off audio capture
 */
- (void)muteLocalAudioStream:(BOOL)isMute;

/*
 * Leave the room
 */
- (void)leaveRTCRoom;

#pragma mark - Background Music Method

/*
 * Turn on background music
 */
- (void)startBackgroundMusic:(NSString *)filePath;

/*
 * Turn off background music
 */
- (void)stopBackgroundMusic;

/*
 * Pause background music
 */
- (void)pauseBackgroundMusic;

/*
 * Restore background music
 */
- (void)resumeBackgroundMusic;

/*
 * Modify the collection volume
 */
- (void)setRecordingVolume:(NSInteger)volume;

/*
 * Modify the background music volume
 */
- (void)setMusicVolume:(NSInteger)volume;

@end

NS_ASSUME_NONNULL_END
