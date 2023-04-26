// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import <Foundation/Foundation.h>
#import "VoiceChatUserModel.h"
#import "VoiceChatRoomModel.h"
#import "VoiceChatSeatModel.h"
#import "VoiceChatRoomParamInfoModel.h"
#import "RTSACKModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatRTSManager : NSObject

#pragma mark - Host API

/// The host creates a live room
/// @param roomName Room Name
/// @param userName User Name
/// @param bgImageName Bg Image Name
/// @param block Callback
+ (void)startLive:(NSString *)roomName
         userName:(NSString *)userName
      bgImageName:(NSString *)bgImageName
            block:(void (^)(NSString *RTCToken,
                            VoiceChatRoomModel *roomModel,
                            VoiceChatUserModel *hostUserModel,
                            RTSACKModel *model))block;


/// Get the list of audience in the room
/// @param roomID Room ID
/// @param block Callback
+ (void)getAudienceList:(NSString *)roomID
                  block:(void (^)(NSArray<VoiceChatUserModel *> *userLists,
                                  RTSACKModel *model))block;


/// Get the list of audiences applied for in the room
/// @param roomID Room ID
/// @param block Callback
+ (void)getApplyAudienceList:(NSString *)roomID
                       block:(void (^)(NSArray<VoiceChatUserModel *> *userLists,
                                       RTSACKModel *model))block;


/// The anchor invites the audience to come on stage
/// @param roomID Room ID
/// @param uid User ID
/// @param seatID Seat ID
/// @param block Callback
+ (void)inviteInteract:(NSString *)roomID
                   uid:(NSString *)uid
                seatID:(NSString *)seatID
                 block:(void (^)(RTSACKModel *model))block;


/// The anchor agrees to the audience's application
/// @param roomID Room ID
/// @param uid User ID
/// @param block Callback
+ (void)agreeApply:(NSString *)roomID
               uid:(NSString *)uid
             block:(void (^)(RTSACKModel *model))block;



/// Whether the host switch is turned on to apply
/// @param roomID Room ID
/// @param type 1 open, other close
/// @param block Callback
+ (void)managerInteractApply:(NSString *)roomID
                        type:(NSInteger)type
                       block:(void (^)(RTSACKModel *model))block;


/// Host management Shangmai guests
/// @param roomID Room ID
/// @param seatID Seat ID
/// @param type management
/// @param block Callback
+ (void)managerSeat:(NSString *)roomID
             seatID:(NSString *)seatID
               type:(NSInteger)type
              block:(void (^)(RTSACKModel *model))block;



/// The host ends the live broadcast
/// @param roomID Room ID
+ (void)finishLive:(NSString *)roomID;


#pragma mark - Audience API


/// The audience joins the room
/// @param roomID Room ID
/// @param userName User Name
/// @param block Callback
+ (void)joinLiveRoom:(NSString *)roomID
            userName:(NSString *)userName
               block:(void (^)(NSString *RTCToken,
                               VoiceChatRoomModel *roomModel,
                               VoiceChatUserModel *userModel,
                               VoiceChatUserModel *hostUserModel,
                               NSArray<VoiceChatSeatModel *> *seatList,
                               RTSACKModel *model))block;




/// Reply to the host’s invitation
/// @param roomID Room ID
/// @param reply 1 accept, 2 Refuse
/// @param block Callback
+ (void)replyInvite:(NSString *)roomID
              reply:(NSInteger)reply
              block:(void (^)(RTSACKModel *model))block;


/// Distinguished guests
/// @param roomID Room ID
/// @param seatID Seat ID
/// @param block Callback
+ (void)finishInteract:(NSString *)roomID
                seatID:(NSString *)seatID
                block:(void (^)(RTSACKModel *model))block;


/// Audience application on stage
/// @param roomID Room ID
/// @param seatID Seat ID [1-8]
/// @param block Callback
+ (void)applyInteract:(NSString *)roomID
               seatID:(NSString *)seatID
                block:(void (^)(BOOL isNeedApply,
                                RTSACKModel *model))block;


/// The audience leaves the room
/// @param roomID Room ID
+ (void)leaveLiveRoom:(NSString *)roomID;


#pragma mark - Publish API


/// Received the audience
/// @param block Callback
+ (void)getActiveLiveRoomListWithBlock:(void (^)(NSArray<VoiceChatRoomModel *> *roomList,
                                                 RTSACKModel *model))block;


/// Mutual kick notification
/// @param block Callback
+ (void)clearUser:(void (^)(RTSACKModel *model))block;


/// Send IM message
/// @param roomID Room ID
/// @param message Message
/// @param block Callback
+ (void)sendMessage:(NSString *)roomID
            message:(NSString *)message
              block:(void (^)(RTSACKModel *model))block;


/// Update microphone status
/// @param roomID Room ID
/// @param mic 0 close ,1 open
/// @param block Callback
+ (void)updateMediaStatus:(NSString *)roomID
                      mic:(NSInteger)mic
                    block:(void (^)(RTSACKModel *model))block;



/// reconnect
/// @param block Callback
+ (void)reconnectWithBlock:(void (^)(NSString *RTCToken,
                                     VoiceChatRoomModel *roomModel,
                                     VoiceChatUserModel *userModel,
                                     VoiceChatUserModel *hostUserModel,
                                     NSArray<VoiceChatSeatModel *> *seatList,
                                     RTSACKModel *model))block;



#pragma mark - Notification Message


/// The audience joins the room
/// @param block Callback
+ (void)onAudienceJoinRoomWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                              NSInteger count))block;


/// The audience leaves the room
/// @param block Callback
+ (void)onAudienceLeaveRoomWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                               NSInteger count))block;


/// Received the end of the live broadcast room
/// @param block Callback
+ (void)onFinishLiveWithBlock:(void (^)(NSString *rommID, NSInteger type))block;


/// Successful audience
/// @param block Callback
+ (void)onJoinInteractWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                          NSString *seatID))block;


/// Distinguished guests
/// @param block Callback
+ (void)onFinishInteractWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                            NSString *seatID,
                                            NSInteger type))block;


/// Seat status changes
/// @param block Callback
+ (void)onSeatStatusChangeWithBlock:(void (^)(NSString *seatID,
                                              NSInteger type))block;


/// Microphone status changes
/// @param block Callback
+ (void)onMediaStatusChangeWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                               NSString *seatID,
                                               NSInteger mic))block;


/// IM message received
/// @param block Callback
+ (void)onMessageWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                     NSString *message))block;


#pragma mark - Single Notification Message


/// Received an invitation
/// @param block Callback
+ (void)onInviteInteractWithBlock:(void (^)(VoiceChatUserModel *hostUserModel,
                                            NSString *seatID))block;

/// Application received
/// @param block Callback
+ (void)onApplyInteractWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                           NSString *seatID))block;


/// Receipt of invitation result
/// @param block Callback
+ (void)onInviteResultWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                          NSInteger reply))block;

/// Receive guest/host microphone changes
/// @param block Callback
+ (void)onMediaOperateWithBlock:(void (^)(NSInteger mic))block;


/// Received mutual kick notification
/// @param block Callback
+ (void)onClearUserWithBlock:(void (^)(NSString *uid))block;

@end

NS_ASSUME_NONNULL_END
