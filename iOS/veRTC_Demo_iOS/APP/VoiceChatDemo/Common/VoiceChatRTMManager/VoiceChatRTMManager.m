//
//  VoiceChatRTMManager.m
//  SceneRTCDemo
//
//  Created by on 2021/3/16.
//

#import "VoiceChatRTMManager.h"
#import "PublicParameterComponent.h"
#import "VoiceChatRTCManager.h"
#import "JoinRTSParams.h"

@implementation VoiceChatRTMManager

#pragma mark - Get Voice data

+ (void)startLive:(NSString *)roomName
         userName:(NSString *)userName
      bgImageName:(NSString *)bgImageName
            block:(void (^)(NSString *RTCToken,
                            VoiceChatRoomModel *roomModel,
                            VoiceChatUserModel *hostUserModel,
                            RTMACKModel *model))block {
    NSDictionary *dic = @{@"room_name" : roomName ?: @"",
                          @"background_image_name" : bgImageName ?: @"",
                          @"user_name" : userName ?: @""};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcStartLive"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        NSString *RTCToken = @"";
        VoiceChatRoomModel *roomModel = nil;
        VoiceChatUserModel *hostUserModel = nil;
        if (ackModel.response &&
            [ackModel.response isKindOfClass:[NSDictionary class]]) {
            roomModel = [VoiceChatRoomModel yy_modelWithJSON:ackModel.response[@"room_info"]];
            hostUserModel = [VoiceChatUserModel yy_modelWithJSON:ackModel.response[@"user_info"]];
            RTCToken = [NSString stringWithFormat:@"%@", ackModel.response[@"rtc_token"]];
        }
        if (block) {
            block(RTCToken, roomModel, hostUserModel, ackModel);
        }
    }];
}

+ (void)getAudienceList:(NSString *)roomID
                  block:(void (^)(NSArray<VoiceChatUserModel *> *userLists,
                                  RTMACKModel *model))block {
    NSDictionary *dic = @{@"room_id" : roomID ?: @""};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcGetAudienceList"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        
        NSMutableArray<VoiceChatUserModel *> *userLists = [[NSMutableArray alloc] init];
        if (ackModel.response &&
            [ackModel.response isKindOfClass:[NSDictionary class]]) {
            NSArray *list = ackModel.response[@"audience_list"];
            for (int i = 0; i < list.count; i++) {
                VoiceChatUserModel *userModel = [VoiceChatUserModel yy_modelWithJSON:list[i]];
                [userLists addObject:userModel];
            }
        }
        if (block) {
            block([userLists copy], ackModel);
        }
    }];
}

+ (void)getApplyAudienceList:(NSString *)roomID
                       block:(void (^)(NSArray<VoiceChatUserModel *> *userLists,
                                       RTMACKModel *model))block {
    NSDictionary *dic = @{@"room_id" : roomID ?: @""};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcGetApplyAudienceList"
    with:dic block:^(RTMACKModel * _Nonnull ackModel) {
        
        NSMutableArray<VoiceChatUserModel *> *userLists = [[NSMutableArray alloc] init];
        if (ackModel.response &&
            [ackModel.response isKindOfClass:[NSDictionary class]]) {
            NSArray *list = ackModel.response[@"audience_list"];
            for (int i = 0; i < list.count; i++) {
                VoiceChatUserModel *userModel = [VoiceChatUserModel yy_modelWithJSON:list[i]];
                [userLists addObject:userModel];
            }
        }
        if (block) {
            block([userLists copy], ackModel);
        }
    }];
}

+ (void)inviteInteract:(NSString *)roomID
                   uid:(NSString *)uid
                seatID:(NSString *)seatID
                 block:(void (^)(RTMACKModel *model))block {
    NSDictionary *dic = @{@"room_id" : roomID ?: @"",
                          @"audience_user_id" : uid ?: @"",
                          @"seat_id" : @(seatID.integerValue)};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcInviteInteract"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        if (block) {
            block(ackModel);
        }
    }];
}

+ (void)agreeApply:(NSString *)roomID
               uid:(NSString *)uid
             block:(void (^)(RTMACKModel *model))block {
    NSDictionary *dic = @{@"room_id" : roomID ?: @"",
                          @"audience_user_id" : uid ?: @""};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcAgreeApply"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        if (block) {
            block(ackModel);
        }
    }];
}

+ (void)managerInteractApply:(NSString *)roomID
                        type:(NSInteger)type
                       block:(void (^)(RTMACKModel *model))block {
    NSDictionary *dic = @{@"room_id" : roomID ?: @"",
                          @"type" : @(type)};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcManageInteractApply"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        if (block) {
            block(ackModel);
        }
    }];
}

+ (void)managerSeat:(NSString *)roomID
             seatID:(NSString *)seatID
               type:(NSInteger)type
              block:(void (^)(RTMACKModel *model))block {
    NSDictionary *dic = @{@"room_id" : roomID ?: @"",
                          @"seat_id" : @(seatID.integerValue),
                          @"type" : @(type)};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcManageSeat"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        if (block) {
            block(ackModel);
        }
    }];
}

+ (void)finishLive:(NSString *)roomID {
    if (IsEmptyStr(roomID)) {
        return;
    }
    NSDictionary *dic = @{@"room_id" : roomID ?: @""};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcFinishLive"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
    }];
}


#pragma mark - Audience API

+ (void)joinLiveRoom:(NSString *)roomID
            userName:(NSString *)userName
               block:(void (^)(NSString *RTCToken,
                               VoiceChatRoomModel *roomModel,
                               VoiceChatUserModel *userModel,
                               VoiceChatUserModel *hostUserModel,
                               NSArray<VoiceChatSeatModel *> *seatList,
                               RTMACKModel *model))block {
    NSDictionary *dic = @{@"room_id" : roomID ?: @"",
                          @"user_name" : userName ?: @""};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcJoinLiveRoom"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        NSString *RTCToken = @"";
        VoiceChatRoomModel *roomModel = nil;
        VoiceChatUserModel *hostUserModel = nil;
        VoiceChatUserModel *userModel = nil;
        NSMutableArray<VoiceChatSeatModel *> *seatList = [[NSMutableArray alloc] init];;
        if (ackModel.response &&
            [ackModel.response isKindOfClass:[NSDictionary class]]) {
            roomModel = [VoiceChatRoomModel yy_modelWithJSON:ackModel.response[@"room_info"]];
            roomModel.audienceCount = [[NSString stringWithFormat:@"%@", ackModel.response[@"audience_count"]] integerValue];
            hostUserModel = [VoiceChatUserModel yy_modelWithJSON:ackModel.response[@"host_info"]];
            userModel = [VoiceChatUserModel yy_modelWithJSON:ackModel.response[@"user_info"]];
            RTCToken = [NSString stringWithFormat:@"%@", ackModel.response[@"rtc_token"]];
            NSDictionary *seatDic = ackModel.response[@"seat_list"];
            for (int i = 0; i < seatDic.allKeys.count; i++) {
                NSString *keyStr = [NSString stringWithFormat:@"%ld", (long)(i + 1)];
                VoiceChatSeatModel *seatModel = [VoiceChatSeatModel yy_modelWithJSON:seatDic[keyStr]];
                seatModel.index = keyStr.integerValue;
                [seatList addObject:seatModel];
            }
        }
        if (block) {
            block(RTCToken,
                  roomModel,
                  userModel,
                  hostUserModel,
                  [seatList copy],
                  ackModel);
        }
    }];
}

+ (void)replyInvite:(NSString *)roomID
              reply:(NSInteger)reply
              block:(void (^)(RTMACKModel *model))block {
    NSDictionary *dic = @{@"room_id" : roomID ?: @"",
                          @"reply" : @(reply)};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcReplyInvite"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        if (block) {
            block(ackModel);
        }
    }];
}

+ (void)finishInteract:(NSString *)roomID
                seatID:(NSString *)seatID
                 block:(void (^)(RTMACKModel *model))block {
    NSDictionary *dic = @{@"room_id" : roomID ?: @"",
                          @"seat_id" : @(seatID.integerValue)};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcFinishInteract"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        if (block) {
            block(ackModel);
        }
    }];
}

+ (void)applyInteract:(NSString *)roomID
               seatID:(NSString *)seatID
                block:(void (^)(BOOL isNeedApply,
                                RTMACKModel *model))block {
    NSDictionary *dic = @{@"room_id" : roomID ?: @"",
                          @"seat_id" : @(seatID.integerValue)};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcApplyInteract"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        BOOL isNeedApply = NO;
        if (ackModel.response &&
            [ackModel.response isKindOfClass:[NSDictionary class]]) {
            isNeedApply = [[NSString stringWithFormat:@"%@", ackModel.response[@"is_need_apply"]] boolValue];
        }
        if (block) {
            block(isNeedApply, ackModel);
        }
    }];
}

+ (void)leaveLiveRoom:(NSString *)roomID {
    NSDictionary *dic = @{@"room_id" : roomID ?: @""};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcLeaveLiveRoom"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {

    }];
}


#pragma mark - Publish API

+ (void)getActiveLiveRoomListWithBlock:(void (^)(NSArray<VoiceChatRoomModel *> *roomList,
                                                 RTMACKModel *model))block {
    NSDictionary *dic = [JoinRTSParams addTokenToParams:nil];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcGetActiveLiveRoomList"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        NSMutableArray<VoiceChatRoomModel *> *roomModelList = [[NSMutableArray alloc] init];
        if (ackModel.response &&
            [ackModel.response isKindOfClass:[NSDictionary class]]) {
            NSArray *list = ackModel.response[@"room_list"];
            for (int i = 0; i < list.count; i++) {
                VoiceChatRoomModel *roomModel = [VoiceChatRoomModel yy_modelWithJSON:list[i]];
                [roomModelList addObject:roomModel];
            }
        }
        if (block) {
            block([roomModelList copy], ackModel);
        }
    }];
}

+ (void)clearUser:(void (^)(RTMACKModel *model))block {
    NSDictionary *dic = [JoinRTSParams addTokenToParams:nil];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcClearUser"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        if (block) {
            block(ackModel);
        }
    }];
}

+ (void)sendMessage:(NSString *)roomID
            message:(NSString *)message
              block:(void (^)(RTMACKModel *model))block {
    NSString *encodedString = [message stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
    NSDictionary *dic = @{@"room_id" : roomID ?: @"",
                          @"message" : encodedString ?: @""};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcSendMessage"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        if (block) {
            block(ackModel);
        }
    }];
}

+ (void)updateMediaStatus:(NSString *)roomID
                      mic:(NSInteger)mic
                    block:(void (^)(RTMACKModel *model))block {
    NSDictionary *dic = @{@"room_id" : roomID ?: @"",
                          @"mic" : @(mic)};
    dic = [JoinRTSParams addTokenToParams:dic];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcUpdateMediaStatus"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        if (block) {
            block(ackModel);
        }
    }];
}

+ (void)reconnectWithBlock:(void (^)(NSString *RTCToken,
                                     VoiceChatRoomModel *roomModel,
                                     VoiceChatUserModel *userModel,
                                     VoiceChatUserModel *hostUserModel,
                                     NSArray<VoiceChatSeatModel *> *seatList,
                                     RTMACKModel *model))block {
    NSDictionary *dic = [JoinRTSParams addTokenToParams:nil];
    
    [[VoiceChatRTCManager shareRtc] emitWithAck:@"svcReconnect"
                                       with:dic
                                      block:^(RTMACKModel * _Nonnull ackModel) {
        VoiceChatRoomModel *roomModel = nil;
        VoiceChatUserModel *hostUserModel = nil;
        VoiceChatUserModel *userModel = nil;
        NSMutableArray<VoiceChatSeatModel *> *seatList = [[NSMutableArray alloc] init];
        NSString *RTCToken = @"";
        if (ackModel.response &&
            [ackModel.response isKindOfClass:[NSDictionary class]]) {
            roomModel = [VoiceChatRoomModel yy_modelWithJSON:ackModel.response[@"room_info"]];
            hostUserModel = [VoiceChatUserModel yy_modelWithJSON:ackModel.response[@"host_info"]];
            userModel = [VoiceChatUserModel yy_modelWithJSON:ackModel.response[@"user_info"]];
            NSDictionary *seatDic = ackModel.response[@"seat_list"];
            for (int i = 0; i < seatDic.allKeys.count; i++) {
                NSString *keyStr = [NSString stringWithFormat:@"%ld", (long)(i + 1)];
                VoiceChatSeatModel *seatModel = [VoiceChatSeatModel yy_modelWithJSON:seatDic[keyStr]];
                seatModel.index = keyStr.integerValue;
                [seatList addObject:seatModel];
            }
            RTCToken = [NSString stringWithFormat:@"%@", ackModel.response[@"rtc_token"]];
        }
        if (block) {
            block(RTCToken,
                  roomModel,
                  userModel,
                  hostUserModel,
                  [seatList copy],
                  ackModel);
        }
    }];
}

#pragma mark - Notification Message

+ (void)onAudienceJoinRoomWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                              NSInteger count))block {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnAudienceJoinRoom"
                                           block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        VoiceChatUserModel *model = nil;
        NSInteger count = -1;
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            model = [VoiceChatUserModel yy_modelWithJSON:noticeModel.data[@"user_info"]];
            NSString *str = [NSString stringWithFormat:@"%@", noticeModel.data[@"audience_count"]];
            count = [str integerValue];
        }
        if (block) {
            block(model, count);
        }
    }];
}

+ (void)onAudienceLeaveRoomWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                               NSInteger count))block {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnAudienceLeaveRoom"
                                           block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        VoiceChatUserModel *model = nil;
        NSInteger count = -1;
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            model = [VoiceChatUserModel yy_modelWithJSON:noticeModel.data[@"user_info"]];
            NSString *str = [NSString stringWithFormat:@"%@", noticeModel.data[@"audience_count"]];
            count = [str integerValue];
        }
        if (block) {
            block(model, count);
        }
    }];
}

+ (void)onFinishLiveWithBlock:(void (^)(NSString *rommID, NSInteger type))block {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnFinishLive"
                                           block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        NSInteger type = -1;
        NSString *rommID = @"";
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            NSString *str = [NSString stringWithFormat:@"%@", noticeModel.data[@"type"]];
            type = [str integerValue];
            rommID = [NSString stringWithFormat:@"%@", noticeModel.data[@"room_id"]];
        }
        if (block) {
            block(rommID, type);
        }
    }];
}

+ (void)onJoinInteractWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                          NSString *seatID))block {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnJoinInteract"
                                           block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        VoiceChatUserModel *model = nil;
        NSString *seatID = @"";
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            model = [VoiceChatUserModel yy_modelWithJSON:noticeModel.data[@"user_info"]];
            seatID = [NSString stringWithFormat:@"%@", noticeModel.data[@"seat_id"]];
        }
        if (block) {
            block(model, seatID);
        }
    }];
}

+ (void)onFinishInteractWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                            NSString *seatID,
                                            NSInteger type))block {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnFinishInteract"
                                           block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        VoiceChatUserModel *model = nil;
        NSString *seatID = @"";
        NSInteger type = -1;
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            model = [VoiceChatUserModel yy_modelWithJSON:noticeModel.data[@"user_info"]];
            seatID = [NSString stringWithFormat:@"%@", noticeModel.data[@"seat_id"]];
            NSString *str = [NSString stringWithFormat:@"%@", noticeModel.data[@"type"]];
            type = [str integerValue];
        }
        if (block) {
            block(model, seatID, type);
        }
    }];
}

+ (void)onSeatStatusChangeWithBlock:(void (^)(NSString *seatID,
                                              NSInteger type))block {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnSeatStatusChange"
                                           block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        NSInteger type = -1;
        NSString *seatID = @"";
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            NSString *str = [NSString stringWithFormat:@"%@", noticeModel.data[@"type"]];
            type = [str integerValue];
            seatID = [NSString stringWithFormat:@"%@", noticeModel.data[@"seat_id"]];
        }
        if (block) {
            block(seatID, type);
        }
    }];
}

+ (void)onMediaStatusChangeWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                               NSString *seatID,
                                               NSInteger mic))block {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnMediaStatusChange"
                                           block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        VoiceChatUserModel *model = nil;
        NSString *seatID = @"";
        NSInteger mic = -1;
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            model = [VoiceChatUserModel yy_modelWithJSON:noticeModel.data[@"user_info"]];
            seatID = [NSString stringWithFormat:@"%@", noticeModel.data[@"seat_id"]];
            NSString *str = [NSString stringWithFormat:@"%@", noticeModel.data[@"mic"]];
            mic = [str integerValue];
        }
        if (block) {
            block(model, seatID, mic);
        }
    }];
}

+ (void)onMessageWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                     NSString *message))block {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnMessage"
                                           block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        VoiceChatUserModel *model = nil;
        NSString *message = @"";
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            model = [VoiceChatUserModel yy_modelWithJSON:noticeModel.data[@"user_info"]];
            message = [NSString stringWithFormat:@"%@", noticeModel.data[@"message"]];
        }
        if (block) {
            block(model, message);
        }
    }];
}


#pragma mark - Single Notification Message

+ (void)onInviteInteractWithBlock:(void (^)(VoiceChatUserModel *hostUserModel,
                                            NSString *seatID))block {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnInviteInteract"
                                       block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        VoiceChatUserModel *model = nil;
        NSString *seatID = @"";
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            model = [VoiceChatUserModel yy_modelWithJSON:noticeModel.data[@"host_info"]];
            seatID = [NSString stringWithFormat:@"%@", noticeModel.data[@"seat_id"]];
        }
        if (block) {
            block(model, seatID);
        }
    }];
}

+ (void)onInviteResultWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                          NSInteger reply))block {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnInviteResult"
                                       block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        VoiceChatUserModel *model = nil;
        NSInteger reply = -1;
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            model = [VoiceChatUserModel yy_modelWithJSON:noticeModel.data[@"user_info"]];
            NSString *str = [NSString stringWithFormat:@"%@", noticeModel.data[@"reply"]];
            reply = [str integerValue];
        }
        if (block) {
            block(model, reply);
        }
    }];
}

+ (void)onApplyInteractWithBlock:(void (^)(VoiceChatUserModel *userModel,
                                           NSString *seatID))block {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnApplyInteract"
                                       block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        VoiceChatUserModel *model = nil;
        NSString *seatID = @"";
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            model = [VoiceChatUserModel yy_modelWithJSON:noticeModel.data[@"user_info"]];
            seatID = [NSString stringWithFormat:@"%@", noticeModel.data[@"seat_id"]];
        }
        if (block) {
            block(model, seatID);
        }
    }];
}

+ (void)onMediaOperateWithBlock:(void (^)(NSInteger mic))block  {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnMediaOperate"
                                           block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        NSInteger mic = -1;
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            NSString *str = [NSString stringWithFormat:@"%@", noticeModel.data[@"mic"]];
            mic = [str integerValue];
        }
        if (block) {
            block(mic);
        }
    }];
}

+ (void)onClearUserWithBlock:(void (^)(NSString *uid))block {
    [[VoiceChatRTCManager shareRtc] onSceneListener:@"svcOnClearUser"
                                           block:^(RTMNoticeModel * _Nonnull noticeModel) {
        
        NSString *uid = @"";
        if (noticeModel.data && [noticeModel.data isKindOfClass:[NSDictionary class]]) {
            uid = [NSString stringWithFormat:@"%@", noticeModel.data[@"user_id"]];
        }
        if (block) {
            block(uid);
        }
    }];
}

@end
