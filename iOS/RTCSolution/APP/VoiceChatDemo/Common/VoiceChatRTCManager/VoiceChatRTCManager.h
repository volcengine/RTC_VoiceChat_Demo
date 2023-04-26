// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "BaseRTCManager.h"
#import "VoiceChatRTCManager.h"
#import "VoiceChatRoomParamInfoModel.h"

NS_ASSUME_NONNULL_BEGIN
@class VoiceChatRTCManager;
@protocol VoiceChatRTCManagerDelegate <NSObject>

/**
 * @brief 房间状态改变时的回调。 通过此回调，您会收到与房间相关的警告、错误和事件的通知。 例如，用户加入房间，用户被移出房间等。
 * @param manager GameRTCManager 模型
 * @param joinModel RTCJoinModel模型房间信息、加入成功失败等信息。
 */
- (void)voiceChatRTCManager:(VoiceChatRTCManager *)manager
    onRoomStateChanged:(RTCJoinModel *)joinModel;

/**
 * @brief 音频质量状态回调
 * @param voiceChatRTCManager GameRTCManager 对象
 * @param model 质量状态模型对象
 */
- (void)voiceChatRTCManager:(VoiceChatRTCManager *)voiceChatRTCManager changeParamInfo:(VoiceChatRoomParamInfoModel *)model;

/**
 * @brief 音量信息变化的回调
 * @param voiceChatRTCManager GameRTCManager 对象
 * @param volumeInfo 语音音量数据，key 为 user id， value 为音量分贝大小范围[0,255]。
 */
- (void)voiceChatRTCManager:(VoiceChatRTCManager *_Nonnull)voiceChatRTCManager reportAllAudioVolume:(NSDictionary<NSString *, NSNumber *> *_Nonnull)volumeInfo;

@end

@interface VoiceChatRTCManager : BaseRTCManager

@property (nonatomic, weak) id<VoiceChatRTCManagerDelegate> delegate;

+ (VoiceChatRTCManager *_Nullable)shareRtc;

#pragma mark - Base Method

/**
 * @brief 加入RTC房间
 * @param token RTC Token
 * @param roomID RTC 房间ID
 * @param uid RTC 用户ID
 */
- (void)joinRTCRoomWithToken:(NSString *)token
                      roomID:(NSString *)roomID
                         uid:(NSString *)uid;

/**
 * @brief 离开 RTC 房间
 */
- (void)leaveRTCRoom;

/**
 * @brief 成为上麦嘉宾
 * @param isGuest ture:角色切换为为上麦嘉宾； false：角色切换为为下麦观众
 */
- (void)makeGuest:(BOOL)isGuest;

/**
 * @brief 控制本地音频流的发送状态：发送/不发送
 * @param isPublish true：发送, false：不发送
 */
- (void)publishAudioStream:(BOOL)isPublish;


#pragma mark - Background Music Method

/**
 * @brief 开启背景音乐
 * @param filePath 音乐沙盒路径
 */
- (void)startBackgroundMusic:(NSString *)filePath;

/**
 * @brief 关闭背景音乐
 */
- (void)stopBackgroundMusic;

/**
 * @brief 暂停背景音乐
 */
- (void)pauseBackgroundMusic;

/**
 * @brief 恢复背景音乐播放
 */
- (void)resumeBackgroundMusic;

/**
 * @brief 设置人声音量
 * @param volume 音量
 */
- (void)setRecordingVolume:(NSInteger)volume;

/**
 * @brief 设置背景音量
 * @param volume 音量
 */
- (void)setMusicVolume:(NSInteger)volume;

@end

NS_ASSUME_NONNULL_END
