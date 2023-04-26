// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.core;

import static com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo.USER_STATUS_APPLYING;
import static com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo.USER_STATUS_INTERACT;
import static com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo.USER_STATUS_INVITING;
import static com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo.USER_STATUS_NORMAL;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.REPLY_TYPE_ACCEPT;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.ss.bytertc.engine.RTCVideo;
import com.volcengine.vertcdemo.common.AbsBroadcast;
import com.volcengine.vertcdemo.common.AppExecutors;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.core.net.RequestCallbackAdapter;
import com.volcengine.vertcdemo.core.net.rts.RTSBaseClient;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;
import com.volcengine.vertcdemo.core.net.rts.RTSInfo;
import com.volcengine.vertcdemo.voicechat.bean.ApplyInteractResponse;
import com.volcengine.vertcdemo.voicechat.bean.AudienceApplyEvent;
import com.volcengine.vertcdemo.voicechat.bean.AudienceChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.ChatMessageEvent;
import com.volcengine.vertcdemo.voicechat.bean.ClearUserEvent;
import com.volcengine.vertcdemo.voicechat.bean.CreateRoomResponse;
import com.volcengine.vertcdemo.voicechat.bean.FinishLiveEvent;
import com.volcengine.vertcdemo.voicechat.bean.GetActiveRoomListResponse;
import com.volcengine.vertcdemo.voicechat.bean.GetAudienceResponse;
import com.volcengine.vertcdemo.voicechat.bean.InteractChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.InteractReplyResponse;
import com.volcengine.vertcdemo.voicechat.bean.InteractResultEvent;
import com.volcengine.vertcdemo.voicechat.bean.JoinRoomResponse;
import com.volcengine.vertcdemo.voicechat.bean.MediaChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.MediaOperateEvent;
import com.volcengine.vertcdemo.voicechat.bean.ReceivedInteractEvent;
import com.volcengine.vertcdemo.voicechat.bean.SeatChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatResponse;
import com.volcengine.vertcdemo.voicechat.event.UserStatusChangedEvent;

import java.util.UUID;

public class VoiceChatRTSClient extends RTSBaseClient {

    private static final String CMD_START_LIVE = "svcStartLive";
    private static final String CMD_GET_AUDIENCE_LIST = "svcGetAudienceList";
    private static final String CMD_GET_APPLY_AUDIENCE_LIST = "svcGetApplyAudienceList";
    private static final String CMD_INVITE_INTERACT = "svcInviteInteract";
    private static final String CMD_AGREE_APPLY = "svcAgreeApply";
    private static final String CMD_MANAGE_INTERACT_APPLY = "svcManageInteractApply";
    private static final String CMD_MANAGE_SEAT = "svcManageSeat";
    private static final String CMD_FINISH_LIVE = "svcFinishLive";
    private static final String CMD_JOIN_LIVE_ROOM = "svcJoinLiveRoom";
    private static final String CMD_REPLY_INVITE = "svcReplyInvite";
    private static final String CMD_FINISH_INTERACT = "svcFinishInteract";
    private static final String CMD_APPLY_INTERACT = "svcApplyInteract";
    private static final String CMD_LEAVE_LIVE_ROOM = "svcLeaveLiveRoom";
    private static final String CMD_GET_ACTIVE_LIVE_ROOM_LIST = "svcGetActiveLiveRoomList";
    private static final String CMD_SEND_MESSAGE = "svcSendMessage";
    private static final String CMD_RECONNECT = "svcReconnect";
    private static final String CMD_CLEAR_USER = "svcClearUser";
    private static final String CMD_UPDATE_MEDIA_STATUS = "svcUpdateMediaStatus";

    private static final AbsBroadcast<AudienceChangedEvent> sAudienceJoined = new AbsBroadcast<>(
            "svcOnAudienceJoinRoom", AudienceChangedEvent.class, (data) -> {
        data.isJoin = true;
        SolutionDemoEventManager.post(data);
    });

    private static final AbsBroadcast<AudienceChangedEvent> sAudienceLeaved = new AbsBroadcast<>(
            "svcOnAudienceLeaveRoom", AudienceChangedEvent.class, (data) -> {
        data.isJoin = false;
        SolutionDemoEventManager.post(data);
    });

    private static final AbsBroadcast<FinishLiveEvent> sFinishLive = new AbsBroadcast<>(
            "svcOnFinishLive", FinishLiveEvent.class, SolutionDemoEventManager::post);

    private static final AbsBroadcast<InteractChangedEvent> sJoinInteract = new AbsBroadcast<>(
            "svcOnJoinInteract", InteractChangedEvent.class, (data) -> {
        data.isStart = true;
        SolutionDemoEventManager.post(data);

        UserStatusChangedEvent event = new UserStatusChangedEvent(data.userInfo, USER_STATUS_INTERACT);
        SolutionDemoEventManager.post(event);
    });

    private static final AbsBroadcast<InteractChangedEvent> sFinishInteract = new AbsBroadcast<>(
            "svcOnFinishInteract", InteractChangedEvent.class, (data) -> {
        data.isStart = false;
        SolutionDemoEventManager.post(data);

        UserStatusChangedEvent event = new UserStatusChangedEvent(data.userInfo, USER_STATUS_NORMAL);
        SolutionDemoEventManager.post(event);
    });

    private static final AbsBroadcast<SeatChangedEvent> sSeatChanged = new AbsBroadcast<>(
            "svcOnSeatStatusChange", SeatChangedEvent.class, SolutionDemoEventManager::post);

    private static final AbsBroadcast<MediaChangedEvent> sMediaChanged = new AbsBroadcast<>(
            "svcOnMediaStatusChange", MediaChangedEvent.class, SolutionDemoEventManager::post);

    private static final AbsBroadcast<ChatMessageEvent> sMessage = new AbsBroadcast<>(
            "svcOnMessage", ChatMessageEvent.class, SolutionDemoEventManager::post);

    private static final AbsBroadcast<ReceivedInteractEvent> sReceivedInvite = new AbsBroadcast<>(
            "svcOnInviteInteract", ReceivedInteractEvent.class, (data) -> {
        UserStatusChangedEvent event = new UserStatusChangedEvent(data.userInfo, USER_STATUS_INVITING);
        SolutionDemoEventManager.post(event);
        SolutionDemoEventManager.post(data);
    });

    private static final AbsBroadcast<AudienceApplyEvent> sReceivedApply = new AbsBroadcast<>(
            "svcOnApplyInteract", AudienceApplyEvent.class, (data) -> {
        data.hasNewApply = true;
        SolutionDemoEventManager.post(data);
        UserStatusChangedEvent event = new UserStatusChangedEvent(data.userInfo, USER_STATUS_APPLYING);
        SolutionDemoEventManager.post(event);
    });

    private static final AbsBroadcast<InteractResultEvent> sInvitedResult = new AbsBroadcast<>(
            "svcOnInviteResult", InteractResultEvent.class, (data) -> {
        SolutionDemoEventManager.post(data);
        int statue = data.reply == REPLY_TYPE_ACCEPT ? USER_STATUS_INTERACT : USER_STATUS_NORMAL;
        UserStatusChangedEvent event = new UserStatusChangedEvent(data.userInfo, statue);
        SolutionDemoEventManager.post(event);
    });

    private static final AbsBroadcast<MediaOperateEvent> sMediaOperate = new AbsBroadcast<>(
            "svcOnMediaOperate", MediaOperateEvent.class, SolutionDemoEventManager::post);

    private static final AbsBroadcast<ClearUserEvent> sClearUser = new AbsBroadcast<>(
            "svcOnClearUser", ClearUserEvent.class, SolutionDemoEventManager::post);


    public VoiceChatRTSClient(RTCVideo rtcVideo, RTSInfo rtmInfo) {
        super(rtcVideo, rtmInfo);
        initEventListener();
    }

    private JsonObject getCommonParams(String cmd) {
        JsonObject params = new JsonObject();
        params.addProperty("app_id", mRTSInfo.appId);
        params.addProperty("room_id", "");
        params.addProperty("user_id", SolutionDataManager.ins().getUserId());
        params.addProperty("event_name", cmd);
        params.addProperty("request_id", UUID.randomUUID().toString());
        params.addProperty("device_id", SolutionDataManager.ins().getDeviceId());
        return params;
    }

    private void initEventListener() {
        putEventListener(sAudienceJoined);
        putEventListener(sAudienceLeaved);
        putEventListener(sFinishLive);
        putEventListener(sJoinInteract);
        putEventListener(sFinishInteract);
        putEventListener(sSeatChanged);
        putEventListener(sMediaChanged);
        putEventListener(sMessage);
        putEventListener(sReceivedInvite);
        putEventListener(sReceivedApply);
        putEventListener(sInvitedResult);
        putEventListener(sMediaOperate);
        putEventListener(sClearUser);
    }

    private void putEventListener(AbsBroadcast<? extends RTSBizInform> absBroadcast) {
        mEventListeners.put(absBroadcast.getEvent(), absBroadcast);
    }

    public void removeAllEventListener() {
        mEventListeners.remove(sAudienceJoined.getEvent());
        mEventListeners.remove(sAudienceLeaved.getEvent());
        mEventListeners.remove(sFinishLive.getEvent());
        mEventListeners.remove(sJoinInteract.getEvent());
        mEventListeners.remove(sFinishInteract.getEvent());
        mEventListeners.remove(sSeatChanged.getEvent());
        mEventListeners.remove(sMediaChanged.getEvent());
        mEventListeners.remove(sMessage.getEvent());
        mEventListeners.remove(sReceivedInvite.getEvent());
        mEventListeners.remove(sReceivedApply.getEvent());
        mEventListeners.remove(sInvitedResult.getEvent());
        mEventListeners.remove(sMediaOperate.getEvent());
        mEventListeners.remove(sClearUser.getEvent());
    }

    private <T extends VoiceChatResponse> void sendServerMessageOnNetwork(String roomId, JsonObject content, Class<T> resultClass, IRequestCallback<T> callback) {
        String cmd = content.get("event_name").getAsString();
        if (TextUtils.isEmpty(cmd)) {
            return;
        }
        AppExecutors.networkIO().execute(() -> sendServerMessage(cmd, roomId, content, resultClass, callback));
    }


    public void requestStartLive(String userName, String roomName, String backgroundImageName,
                                 IRequestCallback<CreateRoomResponse> callback) {
        JsonObject params = getCommonParams(CMD_START_LIVE);
        params.addProperty("user_name", userName);
        params.addProperty("room_name", roomName);
        params.addProperty("background_image_name", backgroundImageName);
        sendServerMessageOnNetwork("", params, CreateRoomResponse.class, callback);
    }

    public void requestAudienceList(String roomId, IRequestCallback<GetAudienceResponse> callback) {
        JsonObject params = getCommonParams(CMD_GET_AUDIENCE_LIST);
        params.addProperty("room_id", roomId);
        sendServerMessageOnNetwork(roomId, params, GetAudienceResponse.class, callback);
    }

    public void requestApplyAudienceList(String roomId, IRequestCallback<GetAudienceResponse> callback) {
        JsonObject params = getCommonParams(CMD_GET_APPLY_AUDIENCE_LIST);
        params.addProperty("room_id", roomId);
        sendServerMessageOnNetwork(roomId, params, GetAudienceResponse.class, callback);
    }

    public void inviteInteract(String roomId, String audienceUserId, int seatId,
                               IRequestCallback<VoiceChatResponse> callback) {
        JsonObject params = getCommonParams(CMD_INVITE_INTERACT);
        params.addProperty("room_id", roomId);
        params.addProperty("audience_user_id", audienceUserId);
        params.addProperty("seat_id", seatId);
        sendServerMessageOnNetwork(roomId, params, VoiceChatResponse.class, callback);
    }

    public void agreeApply(String roomId, String audienceUserId, String audienceRoomId,
                           IRequestCallback<VoiceChatResponse> callback) {
        JsonObject params = getCommonParams(CMD_AGREE_APPLY);
        params.addProperty("room_id", roomId);
        params.addProperty("audience_user_id", audienceUserId);
        params.addProperty("audience_room_id", audienceRoomId);
        sendServerMessageOnNetwork(roomId, params, VoiceChatResponse.class, callback);
    }

    public void manageInteractApply(String roomId, @VoiceChatDataManager.AllowUserApplyType int type,
                                    IRequestCallback<VoiceChatResponse> callback) {
        JsonObject params = getCommonParams(CMD_MANAGE_INTERACT_APPLY);
        params.addProperty("room_id", roomId);
        params.addProperty("type", type);
        sendServerMessageOnNetwork(roomId, params, VoiceChatResponse.class, callback);
    }

    public void managerSeat(String roomId, int seatId, @VoiceChatDataManager.SeatOption int type,
                            IRequestCallback<VoiceChatResponse> callback) {
        JsonObject params = getCommonParams(CMD_MANAGE_SEAT);
        params.addProperty("room_id", roomId);
        params.addProperty("seat_id", seatId);
        params.addProperty("type", type);
        sendServerMessageOnNetwork(roomId, params, VoiceChatResponse.class, callback);
    }

    public void requestFinishLive(String roomId, IRequestCallback<VoiceChatResponse> callback) {
        JsonObject params = getCommonParams(CMD_FINISH_LIVE);
        params.addProperty("room_id", roomId);
        sendServerMessageOnNetwork(roomId, params, VoiceChatResponse.class, callback);
    }

    public void requestJoinRoom(String userName, String roomId,
                                IRequestCallback<JoinRoomResponse> callback) {
        JsonObject params = getCommonParams(CMD_JOIN_LIVE_ROOM);
        params.addProperty("user_name", userName);
        params.addProperty("room_id", roomId);
        sendServerMessageOnNetwork(roomId, params, JoinRoomResponse.class, callback);
    }

    public void replyInvite(String roomId, @VoiceChatDataManager.ReplyType int reply, int seatId,
                            IRequestCallback<InteractReplyResponse> callback) {
        JsonObject params = getCommonParams(CMD_REPLY_INVITE);
        params.addProperty("room_id", roomId);
        params.addProperty("reply", reply);
        params.addProperty("seat_id", seatId);
        sendServerMessageOnNetwork(roomId, params, InteractReplyResponse.class, callback);
    }

    public void finishInteract(String roomId, int seatId, IRequestCallback<VoiceChatResponse> callback) {
        JsonObject params = getCommonParams(CMD_FINISH_INTERACT);
        params.addProperty("room_id", roomId);
        params.addProperty("seat_id", seatId);
        sendServerMessageOnNetwork(roomId, params, VoiceChatResponse.class, callback);
    }

    public void applyInteract(String roomId, int seatId, IRequestCallback<ApplyInteractResponse> callback) {
        JsonObject params = getCommonParams(CMD_APPLY_INTERACT);
        params.addProperty("room_id", roomId);
        params.addProperty("seat_id", seatId);
        sendServerMessageOnNetwork(roomId, params, ApplyInteractResponse.class, callback);
    }

    public void requestLeaveRoom(String roomId, IRequestCallback<VoiceChatResponse> callback) {
        JsonObject params = getCommonParams(CMD_LEAVE_LIVE_ROOM);
        params.addProperty("room_id", roomId);
        sendServerMessageOnNetwork(roomId, params, VoiceChatResponse.class, callback);
    }

    public void getActiveRoomList(IRequestCallback<GetActiveRoomListResponse> callback) {
        JsonObject params = getCommonParams(CMD_GET_ACTIVE_LIVE_ROOM_LIST);
        sendServerMessageOnNetwork("", params, GetActiveRoomListResponse.class, callback);
    }

    public void sendMessage(String roomId, String message, IRequestCallback<VoiceChatResponse> callback) {
        JsonObject params = getCommonParams(CMD_SEND_MESSAGE);
        params.addProperty("room_id", roomId);
        params.addProperty("message", message);
        sendServerMessageOnNetwork(roomId, params, VoiceChatResponse.class, callback);
    }

    public void reconnectToServer(IRequestCallback<JoinRoomResponse> callback) {
        JsonObject params = getCommonParams(CMD_RECONNECT);
        sendServerMessageOnNetwork("", params, JoinRoomResponse.class, callback);
    }

    public void requestClearUser(Runnable next) {
        JsonObject params = getCommonParams(CMD_CLEAR_USER);
        sendServerMessageOnNetwork("", params, VoiceChatResponse.class, RequestCallbackAdapter.create(next));
    }

    public void updateMediaStatus(String roomId, String userId, @VoiceChatDataManager.MicOption int micOption,
                                  IRequestCallback<VoiceChatResponse> callback) {
        JsonObject params = getCommonParams(CMD_UPDATE_MEDIA_STATUS);
        params.addProperty("room_id", roomId);
        params.addProperty("user_id", userId);
        params.addProperty("mic", micOption);
        sendServerMessageOnNetwork(roomId, params, VoiceChatResponse.class, callback);
    }
}
