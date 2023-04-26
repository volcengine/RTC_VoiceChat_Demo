// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <UIKit/UIKit.h>
#import "VoiceChatRTSManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatRoomViewController : UIViewController

/**
 * @brief 使用房间数据模型初始化，观众使用
 * @param roomModel 房间数据模型
 */
- (instancetype)initWithRoomModel:(VoiceChatRoomModel *)roomModel;

/**
 * @brief 使用数据模型和token初始化，主持人使用
 * @param roomModel 房间数据模型
 * @param rtcToken RTC Token
 * @param hostUserModel 主持人用户模型
 */
- (instancetype)initWithRoomModel:(VoiceChatRoomModel *)roomModel
                         rtcToken:(NSString *)rtcToken
                    hostUserModel:(VoiceChatUserModel *)hostUserModel;

#pragma mark - RTS Listener

/**
 * @brief 收到用户加入的消息
 * @param userModel 用户模型
 * @param count 当前房间用户数量
 */
- (void)receivedJoinUser:(VoiceChatUserModel *)userModel
                   count:(NSInteger)count;

/**
 * @brief 收到用户离开的消息
 * @param userModel 用户模型
 * @param count 当前房间用户数量
 */
- (void)receivedLeaveUser:(VoiceChatUserModel *)userModel
                    count:(NSInteger)count;

/**
 * @brief 收到直播结束的消息
 * @param type 直播结束类型。2：因为超时关闭，3：因为违规关闭。
 * @param roomID 房间ID
 */
- (void)receivedFinishLive:(NSInteger)type roomID:(NSString *)roomID;

/**
 * @brief 收到有用户上麦消息
 * @param userModel 用户模型
 * @param seatID 麦位ID
 */
- (void)receivedJoinInteractWithUser:(VoiceChatUserModel *)userModel
                              seatID:(NSString *)seatID;

/**
 * @brief 收到有用户下麦消息
 * @param userModel 用户模型
 * @param seatID 麦位ID
 * @param type 下麦类型。1 ：被踢下麦，2：主动下麦。
 */
- (void)receivedLeaveInteractWithUser:(VoiceChatUserModel *)userModel
                               seatID:(NSString *)seatID
                                 type:(NSInteger)type;

/**
 * @brief 收到麦位状态变化的消息
 * @param seatID 麦位ID
 * @param type 麦位状态。 0：麦位被锁, 1：正常麦位
 */
- (void)receivedSeatStatusChange:(NSString *)seatID
                            type:(NSInteger)type;

/**
 * @brief 收到媒体状态变化的消息
 * @param seatID 麦位ID
 * @param mic 麦克风开关状态。1：开启麦克风，0：关闭麦克风
 */
- (void)receivedMediaStatusChangeWithUser:(VoiceChatUserModel *)userModel
                                   seatID:(NSString *)seatID
                                      mic:(NSInteger)mic;

/**
 * @brief 收到远端用户消息
 * @param userModel 用户模型
 * @param message 消息内容
 */
- (void)receivedMessageWithUser:(VoiceChatUserModel *)userModel
                            message:(NSString *)message;

/**
 * @brief 收到邀请连麦的消息
 * @param hostUserModel 主播用户模型
 * @param seatID 麦位ID
 */
- (void)receivedInviteInteractWithUser:(VoiceChatUserModel *)hostUserModel
                                seatID:(NSString *)seatID;

/**
 * @brief 收到申请上麦消息
 * @param userModel 用户模型
 * @param seatID 麦位ID
 */
- (void)receivedApplyInteractWithUser:(VoiceChatUserModel *)userModel
                               seatID:(NSString *)seatID;

/**
 * @brief 收到邀请结果消息
 * @param hostUserModel 主播用户模型
 * @param reply 邀请结果类型。2：拒绝邀请，1：同意邀请。
 */
- (void)receivedInviteResultWithUser:(VoiceChatUserModel *)hostUserModel
                               reply:(NSInteger)reply;

/**
 * @brief 收到操作麦克风的消息
 * @param mic 麦克风开关状态。1：开启麦克风，0：关闭麦克风
 */
- (void)receivedMediaOperatWithUid:(NSInteger)mic;

/**
 * @brief 收到清除用户的消息
 * @param uid 用户ID
 */
- (void)receivedClearUserWithUid:(NSString *)uid;


@end

NS_ASSUME_NONNULL_END
