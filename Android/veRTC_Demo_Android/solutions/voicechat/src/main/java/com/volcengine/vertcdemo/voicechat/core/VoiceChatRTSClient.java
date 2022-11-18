package com.volcengine.vertcdemo.voicechat.core;

import static com.volcengine.vertcdemo.voicechat.bean.VCUserInfo.USER_STATUS_APPLYING;
import static com.volcengine.vertcdemo.voicechat.bean.VCUserInfo.USER_STATUS_INTERACT;
import static com.volcengine.vertcdemo.voicechat.bean.VCUserInfo.USER_STATUS_INVITING;
import static com.volcengine.vertcdemo.voicechat.bean.VCUserInfo.USER_STATUS_NORMAL;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.REPLY_TYPE_ACCEPT;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.ss.bytertc.engine.RTCVideo;
import com.ss.video.rtc.demo.basic_module.utils.AppExecutors;
import com.volcengine.vertcdemo.common.AbsBroadcast;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.core.net.rts.RTSBaseClient;
import com.volcengine.vertcdemo.core.net.rts.RTSBizInform;
import com.volcengine.vertcdemo.core.net.rts.RTSInfo;
import com.volcengine.vertcdemo.voicechat.bean.AudienceApplyBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.AudienceChangedBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.ClearUserBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.CreateRoomResponse;
import com.volcengine.vertcdemo.voicechat.bean.FinishLiveBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.GetActiveRoomListResponse;
import com.volcengine.vertcdemo.voicechat.bean.GetAudienceResponse;
import com.volcengine.vertcdemo.voicechat.bean.InteractChangedBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.InteractResultBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.JoinRoomResponse;
import com.volcengine.vertcdemo.voicechat.bean.MediaChangedBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.MediaOperateBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.MessageBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.ReceivedInteractBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.ReplyMicOnResponse;
import com.volcengine.vertcdemo.voicechat.bean.ReplyResponse;
import com.volcengine.vertcdemo.voicechat.bean.SeatChangedBroadcast;
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

    private static final AbsBroadcast<AudienceChangedBroadcast> sAudienceJoined = new AbsBroadcast<>(
            "svcOnAudienceJoinRoom", AudienceChangedBroadcast.class, (data) -> {
        data.isJoin = true;
        SolutionDemoEventManager.post(data);
    });

    private static final AbsBroadcast<AudienceChangedBroadcast> sAudienceLeaved = new AbsBroadcast<>(
            "svcOnAudienceLeaveRoom", AudienceChangedBroadcast.class, (data) -> {
        data.isJoin = false;
        SolutionDemoEventManager.post(data);
    });

    private static final AbsBroadcast<FinishLiveBroadcast> sFinishLive = new AbsBroadcast<>(
            "svcOnFinishLive", FinishLiveBroadcast.class, SolutionDemoEventManager::post);

    private static final AbsBroadcast<InteractChangedBroadcast> sJoinInteract = new AbsBroadcast<>(
            "svcOnJoinInteract", InteractChangedBroadcast.class, (data) -> {
        data.isStart = true;
        SolutionDemoEventManager.post(data);

        UserStatusChangedEvent event = new UserStatusChangedEvent(data.userInfo, USER_STATUS_INTERACT);
        SolutionDemoEventManager.post(event);
    });

    private static final AbsBroadcast<InteractChangedBroadcast> sFinishInteract = new AbsBroadcast<>(
            "svcOnFinishInteract", InteractChangedBroadcast.class, (data) -> {
        data.isStart = false;
        SolutionDemoEventManager.post(data);

        UserStatusChangedEvent event = new UserStatusChangedEvent(data.userInfo, USER_STATUS_NORMAL);
        SolutionDemoEventManager.post(event);
    });

    private static final AbsBroadcast<SeatChangedBroadcast> sSeatChanged = new AbsBroadcast<>(
            "svcOnSeatStatusChange", SeatChangedBroadcast.class, SolutionDemoEventManager::post);

    private static final AbsBroadcast<MediaChangedBroadcast> sMediaChanged = new AbsBroadcast<>(
            "svcOnMediaStatusChange", MediaChangedBroadcast.class, SolutionDemoEventManager::post);

    private static final AbsBroadcast<MessageBroadcast> sMessage = new AbsBroadcast<>(
            "svcOnMessage", MessageBroadcast.class, SolutionDemoEventManager::post);

    private static final AbsBroadcast<ReceivedInteractBroadcast> sReceivedInvite = new AbsBroadcast<>(
            "svcOnInviteInteract", ReceivedInteractBroadcast.class, (data) -> {
        UserStatusChangedEvent event = new UserStatusChangedEvent(data.userInfo, USER_STATUS_INVITING);
        SolutionDemoEventManager.post(event);
        SolutionDemoEventManager.post(data);
    });

    private static final AbsBroadcast<AudienceApplyBroadcast> sReceivedApply = new AbsBroadcast<>(
            "svcOnApplyInteract", AudienceApplyBroadcast.class, (data) -> {
        data.hasNewApply = true;
        SolutionDemoEventManager.post(data);
        UserStatusChangedEvent event = new UserStatusChangedEvent(data.userInfo, USER_STATUS_APPLYING);
        SolutionDemoEventManager.post(event);
    });

    private static final AbsBroadcast<InteractResultBroadcast> sInvitedResult = new AbsBroadcast<>(
            "svcOnInviteResult", InteractResultBroadcast.class, (data) -> {
        SolutionDemoEventManager.post(data);
        int statue = data.reply == REPLY_TYPE_ACCEPT ? USER_STATUS_INTERACT : USER_STATUS_NORMAL;
        UserStatusChangedEvent event = new UserStatusChangedEvent(data.userInfo, statue);
        SolutionDemoEventManager.post(event);
    });

    private static final AbsBroadcast<MediaOperateBroadcast> sMediaOperate = new AbsBroadcast<>(
            "svcOnMediaOperate", MediaOperateBroadcast.class, SolutionDemoEventManager::post);

    private static final AbsBroadcast<ClearUserBroadcast> sClearUser = new AbsBroadcast<>(
            "svcOnClearUser", ClearUserBroadcast.class, SolutionDemoEventManager::post);


    public VoiceChatRTSClient(RTCVideo rtcVideo, RTSInfo rtmInfo) {
        super(rtcVideo, rtmInfo);
        initEventListener();
    }

    private JsonObject getCommonParams(String cmd) {
        JsonObject params = new JsonObject();
        params.addProperty("app_id", mRtmInfo.appId);
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
                            IRequestCallback<ReplyResponse> callback) {
        JsonObject params = getCommonParams(CMD_REPLY_INVITE);
        params.addProperty("room_id", roomId);
        params.addProperty("reply", reply);
        params.addProperty("seat_id", seatId);
        sendServerMessageOnNetwork(roomId, params, ReplyResponse.class, callback);
    }

    public void finishInteract(String roomId, int seatId, IRequestCallback<VoiceChatResponse> callback) {
        JsonObject params = getCommonParams(CMD_FINISH_INTERACT);
        params.addProperty("room_id", roomId);
        params.addProperty("seat_id", seatId);
        sendServerMessageOnNetwork(roomId, params, VoiceChatResponse.class, callback);
    }

    public void applyInteract(String roomId, int seatId, IRequestCallback<ReplyMicOnResponse> callback) {
        JsonObject params = getCommonParams(CMD_APPLY_INTERACT);
        params.addProperty("room_id", roomId);
        params.addProperty("seat_id", seatId);
        sendServerMessageOnNetwork(roomId, params, ReplyMicOnResponse.class, callback);
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

    public void requestClearUser(IRequestCallback<VoiceChatResponse> callback) {
        JsonObject params = getCommonParams(CMD_CLEAR_USER);
        sendServerMessageOnNetwork("", params, VoiceChatResponse.class, callback);
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
