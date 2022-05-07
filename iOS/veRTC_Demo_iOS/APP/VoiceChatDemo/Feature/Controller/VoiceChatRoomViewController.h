//
//  VoiceChatRoomViewController.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/5/18.
//  Copyright © 2021 . All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VoiceChatRTMManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatRoomViewController : UIViewController

- (instancetype)initWithRoomModel:(VoiceChatRoomModel *)roomModel;

- (instancetype)initWithRoomModel:(VoiceChatRoomModel *)roomModel
                         rtcToken:(NSString *)rtcToken
                    hostUserModel:(VoiceChatUserModel *)hostUserModel;

- (void)receivedJoinUser:(VoiceChatUserModel *)userModel
                   count:(NSInteger)count;

- (void)receivedLeaveUser:(VoiceChatUserModel *)userModel
                    count:(NSInteger)count;

- (void)receivedFinishLive:(NSInteger)type roomID:(NSString *)roomID;

- (void)receivedJoinInteractWithUser:(VoiceChatUserModel *)userModel
                              seatID:(NSString *)seatID;

- (void)receivedLeaveInteractWithUser:(VoiceChatUserModel *)userModel
                               seatID:(NSString *)seatID
                                 type:(NSInteger)type;

- (void)receivedSeatStatusChange:(NSString *)seatID
                            type:(NSInteger)type;

- (void)receivedMediaStatusChangeWithUser:(VoiceChatUserModel *)userModel
                                   seatID:(NSString *)seatID
                                      mic:(NSInteger)mic;

- (void)receivedMessageWithUser:(VoiceChatUserModel *)userModel
                            message:(NSString *)message;

- (void)receivedInviteInteractWithUser:(VoiceChatUserModel *)hostUserModel
                                seatID:(NSString *)seatID;

- (void)receivedApplyInteractWithUser:(VoiceChatUserModel *)userModel
                               seatID:(NSString *)seatID;


- (void)receivedInviteResultWithUser:(VoiceChatUserModel *)hostUserModel
                               reply:(NSInteger)reply;

- (void)receivedMediaOperatWithUid:(NSInteger)mic;

- (void)receivedClearUserWithUid:(NSString *)uid;


@end

NS_ASSUME_NONNULL_END
