// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.feature.roommain;

import static com.volcengine.vertcdemo.voicechat.bean.InteractChangedEvent.FINISH_INTERACT_TYPE_HOST;
import static com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo.MIC_STATUS_ON;
import static com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo.USER_STATUS_INTERACT;
import static com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo.USER_STATUS_NORMAL;
import static com.volcengine.vertcdemo.voicechat.feature.roommain.AudienceManagerDialog.SEAT_ID_BY_SERVER;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.volcengine.vertcdemo.common.GsonUtils;
import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.common.InputTextDialogFragment;
import com.volcengine.vertcdemo.common.SolutionBaseActivity;
import com.volcengine.vertcdemo.common.SolutionCommonDialog;
import com.volcengine.vertcdemo.common.SolutionToast;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.AppTokenExpiredEvent;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.ErrorTool;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.core.eventbus.SDKReconnectToRoomEvent;
import com.volcengine.vertcdemo.utils.Utils;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.AudienceApplyEvent;
import com.volcengine.vertcdemo.voicechat.bean.AudienceChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.ChatMessageEvent;
import com.volcengine.vertcdemo.voicechat.bean.ClearUserEvent;
import com.volcengine.vertcdemo.voicechat.bean.FinishLiveEvent;
import com.volcengine.vertcdemo.voicechat.bean.InteractChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.InteractReplyResponse;
import com.volcengine.vertcdemo.voicechat.bean.InteractResultEvent;
import com.volcengine.vertcdemo.voicechat.bean.JoinRoomResponse;
import com.volcengine.vertcdemo.voicechat.bean.MediaChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.MediaOperateEvent;
import com.volcengine.vertcdemo.voicechat.bean.ReceivedInteractEvent;
import com.volcengine.vertcdemo.voicechat.bean.SeatChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatResponse;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatRoomInfo;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatSeatInfo;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatRTCManager;
import com.volcengine.vertcdemo.voicechat.databinding.ActivityVoiceChatMainBinding;
import com.volcengine.vertcdemo.voicechat.event.SDKAudioPropertiesEvent;
import com.volcengine.vertcdemo.voicechat.event.SDKAudioStatsEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * 语音聊天室房间主页
 */
public class VoiceChatRoomMainActivity extends SolutionBaseActivity {

    private static final String TAG = "VoiceChatRoomMain";

    private static final String REFER_KEY = "refer";
    private static final String REFER_FROM_CREATE = "create";
    private static final String REFER_FROM_LIST = "list";
    private static final String REFER_EXTRA_ROOM = "extra_room";
    private static final String REFER_EXTRA_USER = "extra_user";
    private static final String REFER_EXTRA_CREATE_JSON = "extra_create_json";

    private ActivityVoiceChatMainBinding mViewBinding;

    private ChatAdapter mChatAdapter;

    private VoiceChatRoomInfo mRoomInfo;
    private VoiceChatUserInfo mHostUserInfo;
    private VoiceChatUserInfo mSelfUserInfo;

    private boolean isMicOn = true;
    // 是不是多端登录被服务端踢出
    // Is the multi-terminal login kicked out by the server
    private boolean isLeaveByKickOut = false;

    private final IRequestCallback<JoinRoomResponse> mJoinCallback = new IRequestCallback<JoinRoomResponse>() {
        @Override
        public void onSuccess(JoinRoomResponse data) {
            data.isFromCreate = true;
            initViewWithData(data);
        }

        @Override
        public void onError(int errorCode, String message) {
            onArgsError(ErrorTool.getErrorMessageByErrorCode(errorCode, message));
        }
    };

    private final IRequestCallback<VoiceChatResponse> mSendMessageCallback = new IRequestCallback<VoiceChatResponse>() {
        @Override
        public void onSuccess(VoiceChatResponse data) {

        }

        @Override
        public void onError(int errorCode, String message) {

        }
    };

    private final IRequestCallback<VoiceChatResponse> mFinishCallback = new IRequestCallback<VoiceChatResponse>() {
        @Override
        public void onSuccess(VoiceChatResponse data) {

        }

        @Override
        public void onError(int errorCode, String message) {

        }
    };

    private final IRequestCallback<VoiceChatResponse> mLeaveCallback = new IRequestCallback<VoiceChatResponse>() {
        @Override
        public void onSuccess(VoiceChatResponse data) {

        }

        @Override
        public void onError(int errorCode, String message) {

        }
    };

    private final IRequestCallback<JoinRoomResponse> mReconnectCallback = new IRequestCallback<JoinRoomResponse>() {
        @Override
        public void onSuccess(JoinRoomResponse data) {
            data.isFromCreate = false;
            initViewWithData(data);
        }

        @Override
        public void onError(int errorCode, String message) {
            SolutionToast.show(ErrorTool.getErrorMessageByErrorCode(errorCode, message));
            finish();
        }
    };

    private final BottomOptionLayout.IBottomOptions mIBottomOptions = new BottomOptionLayout.IBottomOptions() {
        @Override
        public void onInputClick() {
            openInput();
        }

        @Override
        public void onInteractClick() {
            AudienceManagerDialog dialog = new AudienceManagerDialog(VoiceChatRoomMainActivity.this);
            dialog.setData(mRoomInfo.roomId, VoiceChatDataManager.ins().getAllowUserApply(),
                    VoiceChatDataManager.ins().hasNewApply(), SEAT_ID_BY_SERVER);
            dialog.show();
        }

        @Override
        public void onBGMClick() {
            BGMSettingDialog dialog = new BGMSettingDialog(VoiceChatRoomMainActivity.this);
            dialog.setData(VoiceChatDataManager.ins().getBGMOpening(),
                    VoiceChatDataManager.ins().getBGMVolume(),
                    VoiceChatDataManager.ins().getUserVolume());
            dialog.show();
        }

        @Override
        public void onMicClick() {
            switchMicStatus();
        }

        @Override
        public void onCloseClick() {
            attemptLeave();
        }
    };

    private final IAction<VoiceChatSeatInfo> mOnSeatClick = seatInfo -> {
        if (InputTextDialogFragment.isInputFragmentShowing(getSupportFragmentManager())) {
            closeInput();
            return;
        }

        if (seatInfo != null && !mSelfUserInfo.isHost() && seatInfo.isLocked()) {
            return;
        }
        VoiceChatUserInfo userInfo = seatInfo == null ? null : seatInfo.userInfo;
        String userId = userInfo == null ? null : userInfo.userId;
        String selfUserId = SolutionDataManager.ins().getUserId();
        boolean isSelf = TextUtils.equals(selfUserId, userId);
        boolean isSelfHost = TextUtils.equals(selfUserId, mHostUserInfo.userId);
        if (!isSelfHost && !isSelf) {
            if (userInfo != null) {
                return;
            }
            if (mSelfUserInfo.userStatus == USER_STATUS_INTERACT) {
                SolutionToast.show(R.string.you_are_on_the_mic);
                return;
            } else if (mSelfUserInfo.userStatus == USER_STATUS_NORMAL) {
                if (VoiceChatDataManager.ins().getSelfApply()) {
                    SolutionToast.show(R.string.application_has_been_sent_to_the_host);
                    return;
                }
            }
        }
        SeatOptionDialog dialog = new SeatOptionDialog(VoiceChatRoomMainActivity.this);
        dialog.setData(mRoomInfo.roomId, seatInfo, mSelfUserInfo.userRole, mSelfUserInfo.userStatus);
        dialog.show();
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityVoiceChatMainBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        mViewBinding.voiceChatMainBg.setOnClickListener((v) -> closeInput());

        Drawable drawable = getResources().getDrawable(R.drawable.ic_audience);
        drawable.setBounds(0, 0,
                (int) Utils.dp2Px(22), (int) Utils.dp2Px(20));
        mViewBinding.voiceChatMainAudienceNum.setCompoundDrawables(drawable, null, null, null);

        mViewBinding.voiceChatMainSeatGroup.setSeatClick(mOnSeatClick);

        mViewBinding.voiceChatMainBottomOption.setOptionCallback(mIBottomOptions);

        mChatAdapter = new ChatAdapter();

        mViewBinding.voiceChatMainChatRv.setLayoutManager(new LinearLayoutManager(
                VoiceChatRoomMainActivity.this, RecyclerView.VERTICAL, false));
        mViewBinding.voiceChatMainChatRv.setAdapter(mChatAdapter);
        mViewBinding.voiceChatMainChatRv.setOnClickListener((v) -> closeInput());

        closeInput();

        if (!checkArgs()) {
            onArgsError(getString(R.string.joining_room_failed));
            return;
        }

        SolutionDemoEventManager.register(this);
    }

    private boolean checkArgs() {
        Intent intent = getIntent();
        if (intent == null) {
            return false;
        }
        String refer = intent.getStringExtra(REFER_KEY);
        String roomJson = intent.getStringExtra(REFER_EXTRA_ROOM);
        String selfJson = intent.getStringExtra(REFER_EXTRA_USER);
        VoiceChatRoomInfo roomInfo = GsonUtils.gson().fromJson(roomJson, VoiceChatRoomInfo.class);
        VoiceChatUserInfo selfInfo = GsonUtils.gson().fromJson(selfJson, VoiceChatUserInfo.class);
        if (TextUtils.equals(refer, REFER_FROM_LIST)) {
            VoiceChatRTCManager.ins().getRTSClient().requestJoinRoom(selfInfo.userName, roomInfo.roomId, mJoinCallback);
            return true;
        } else if (TextUtils.equals(refer, REFER_FROM_CREATE)) {
            String createJson = intent.getStringExtra(REFER_EXTRA_CREATE_JSON);
            if (TextUtils.isEmpty(createJson)) {
                return false;
            }
            JoinRoomResponse createResponse = GsonUtils.gson().fromJson(createJson, JoinRoomResponse.class);
            initViewWithData(createResponse);
            return true;
        } else {
            return false;
        }
    }

    private void initViewWithData(JoinRoomResponse data) {
        mViewBinding.voiceChatMainTitle.setText(data.roomInfo.roomName);
        mViewBinding.voiceChatMainAudienceNum.setText(String.valueOf(data.audienceCount + 1));
        mRoomInfo = data.roomInfo;
        mHostUserInfo = data.hostInfo;
        mSelfUserInfo = data.userInfo;
        mViewBinding.voiceChatMainSeatGroup.bindHostInfo(data.hostInfo);
        mViewBinding.voiceChatMainSeatGroup.bindSeatInfo(data.seatMap);
        mViewBinding.voiceChatMainBottomOption.updateUIByRoleAndStatus(data.userInfo.userRole, data.userInfo.userStatus);

        setMainBackground(data.roomInfo.extraInfo);

        if (data.isFromCreate) {
            VoiceChatRTCManager.ins().joinRoom(data.roomInfo.roomId, data.rtcToken, data.userInfo.userId);
        }
        boolean isSelfHost = TextUtils.equals(data.userInfo.userId, mHostUserInfo.userId);
        boolean isInteract = data.userInfo.userStatus == USER_STATUS_INTERACT;
        VoiceChatRTCManager.ins().startAudioCapture(isSelfHost || isInteract);
        VoiceChatRTCManager.ins().startMuteAudio(!data.userInfo.isMicOn());
    }

    /**
     * 根据进房的额外信息设置主播间背景
     * @param ext 额外信息的json
     */
    private void setMainBackground(String ext) {
        if (TextUtils.isEmpty(ext)) {
            return;
        }
        try {
            JSONObject json = new JSONObject(ext);
            String nameStr = json.getString("background_image_name");
            int backgroundRes;
            if (nameStr.contains("voicechat_background_1")) {
                backgroundRes = R.drawable.voice_chat_background_1;
            } else if (nameStr.contains("voicechat_background_2")) {
                backgroundRes = R.drawable.voice_chat_background_2;
            } else {
                backgroundRes = R.drawable.voice_chat_background_0;
            }
            mViewBinding.voiceChatMainBg.setImageResource(backgroundRes);
        } catch (Exception e) {
            Log.e(TAG, "setMainBackground exception", e);
        }
    }

    @Override
    public void onBackPressed() {
        if (InputTextDialogFragment.isInputFragmentShowing(getSupportFragmentManager())) {
            closeInput();
        } else {
            attemptLeave();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeInput();
        VoiceChatDataManager.ins().clearData();
        SolutionDemoEventManager.unregister(this);
        VoiceChatRTCManager.ins().leaveRoom();
        VoiceChatRTCManager.ins().stopAudioMixing();
        if (mSelfUserInfo == null || mRoomInfo == null) {
            return;
        }
        if (isLeaveByKickOut) {
            return;
        }
        if (mSelfUserInfo.isHost()) {
            VoiceChatRTCManager.ins().getRTSClient().requestFinishLive(mRoomInfo.roomId, mFinishCallback);
        } else {
            VoiceChatRTCManager.ins().getRTSClient().requestLeaveRoom(mRoomInfo.roomId, mLeaveCallback);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTokenExpiredEvent(AppTokenExpiredEvent event) {
        finish();
    }

    @Override
    protected boolean onMicrophonePermissionClose() {
        Log.d(TAG, "onMicrophonePermissionClose");
        finish();
        return true;
    }

    @Override
    protected boolean onCameraPermissionClose() {
        Log.d(TAG, "onCameraPermissionClose");
        finish();
        return true;
    }

    private void onArgsError(String message) {
        SolutionCommonDialog dialog = new SolutionCommonDialog(this);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setPositiveListener((v) -> {
            finish();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void attemptLeave() {
        if (mSelfUserInfo == null || !mSelfUserInfo.isHost()) {
            finish();
            return;
        }
        SolutionCommonDialog dialog = new SolutionCommonDialog(this);
        dialog.setMessage(getString(R.string.are_you_sure_to_end_the_voice_chatroom));
        dialog.setPositiveListener((v) -> {
            finish();
            dialog.dismiss();
        });
        dialog.setNegativeListener((v) -> dialog.dismiss());
        dialog.show();
    }

    /**
     * 发送聊天消息。
     * @param fragment 输入消息对话框。
     * @param message 聊天消息。
     */
    private void onSendMessage(InputTextDialogFragment fragment, String message) {
        if (mRoomInfo == null) {
            return;
        }
        if (TextUtils.isEmpty(message)) {
            SolutionToast.show(getString(R.string.send_empty_message_cannot_be_empty));
            return;
        }
        closeInput();
        onReceivedMessage(String.format("%s : %s", SolutionDataManager.ins().getUserName(), message));
        try {
            message = URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        VoiceChatRTCManager.ins().getRTSClient().sendMessage(mRoomInfo.roomId, message, mSendMessageCallback);
        fragment.dismiss();
    }

    private void openInput() {
        InputTextDialogFragment.showInput(getSupportFragmentManager(), (this::onSendMessage));
    }

    private void closeInput() {
        InputTextDialogFragment.hideInput(getSupportFragmentManager());
    }

    private void onReceivedMessage(String message) {
        mChatAdapter.addChatMsg(message);
        mViewBinding.voiceChatMainChatRv.post(() -> mViewBinding.voiceChatMainChatRv.smoothScrollToPosition(mChatAdapter.getItemCount()));
    }

    private void switchMicStatus() {
        isMicOn = !isMicOn;

        mViewBinding.voiceChatMainSeatGroup.updateUserMediaStatus(SolutionDataManager.ins().getUserId(), isMicOn);
        VoiceChatRTCManager.ins().startMuteAudio(!isMicOn);
        mViewBinding.voiceChatMainBottomOption.updateMicStatus(isMicOn);
        VoiceChatRTCManager.ins().getRTSClient().updateMediaStatus(
                mRoomInfo.roomId, mSelfUserInfo.userId,
                isMicOn ? VoiceChatDataManager.MIC_OPTION_ON : VoiceChatDataManager.MIC_OPTION_OFF,
                new IRequestCallback<VoiceChatResponse>() {
                    @Override
                    public void onSuccess(VoiceChatResponse data) {

                    }

                    @Override
                    public void onError(int errorCode, String message) {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudienceChangedBroadcast(AudienceChangedEvent event) {
        String suffix = event.isJoin
                ? getString(R.string.voice_joined_the_room)
                : getString(R.string.voice_leave_the_room);
        onReceivedMessage(event.userInfo.userName + suffix);
        mViewBinding.voiceChatMainAudienceNum.setText(String.valueOf(event.audienceCount + 1));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishLiveBroadcast(FinishLiveEvent event) {
        if (mRoomInfo == null || !TextUtils.equals(event.roomId, mRoomInfo.roomId)) {
            return;
        }
        String message = null;
        boolean isHost = mSelfUserInfo != null && mSelfUserInfo.isHost();
        if (event.type == FinishLiveEvent.FINISH_TYPE_AGAINST) {
            message = getString(R.string.closed_terms_service);
        } else if (event.type == FinishLiveEvent.FINISH_TYPE_TIMEOUT && isHost) {
            message = getString(R.string.minutes_error_message);
        } else if (!isHost) {
            message = getString(R.string.live_ended);
        }
        if (!TextUtils.isEmpty(message)) {
            SolutionToast.show(message);
        }
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageBroadcast(ChatMessageEvent event) {
        if (TextUtils.equals(event.userInfo.userId, mSelfUserInfo.userId)) {
            return;
        }
        String message;
        try {
            message = URLDecoder.decode(event.message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            message = event.message;
        }
        onReceivedMessage(String.format("%s : %s", event.userInfo.userName, message));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInteractChangedBroadcast(InteractChangedEvent event) {
        VoiceChatSeatInfo info = new VoiceChatSeatInfo();
        info.userInfo = event.userInfo;
        mViewBinding.voiceChatMainSeatGroup.bindSeatInfo(event.seatId, event.isStart ? info : null);

        String message = getString(event.isStart
                ? R.string.xxx_has_been_on_the_mic_message
                : R.string.xxx_has_dropped_the_mic_message, event.userInfo.userName);

        onReceivedMessage(message);

        boolean isSelf = TextUtils.equals(SolutionDataManager.ins().getUserId(), event.userInfo.userId);
        if (!isSelf) {
            return;
        }
        VoiceChatDataManager.ins().setSelfApply(false);
        if (event.isStart) {
            SolutionToast.show(R.string.you_are_on_mic);
        } else {
            if (event.type == FINISH_INTERACT_TYPE_HOST) {
                SolutionToast.show(R.string.you_have_been_removed_from_the_mic);
            } else {
                SolutionToast.show(R.string.you_have_leave_mic);
            }
        }
        mSelfUserInfo.userStatus = event.isStart ? USER_STATUS_INTERACT : USER_STATUS_NORMAL;
        VoiceChatRTCManager.ins().startAudioCapture(event.isStart);
        VoiceChatRTCManager.ins().startMuteAudio(false);

        mViewBinding.voiceChatMainBottomOption.updateUIByRoleAndStatus(mSelfUserInfo.userRole, mSelfUserInfo.userStatus);
        mViewBinding.voiceChatMainBottomOption.updateMicStatus(event.userInfo.isMicOn());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSeatChangedBroadcast(SeatChangedEvent event) {
        mViewBinding.voiceChatMainSeatGroup.updateSeatStatus(event.seatId, event.type);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedInteractBroadcast(ReceivedInteractEvent event) {
        SolutionCommonDialog dialog = new SolutionCommonDialog(this);
        dialog.setMessage(getString(R.string.anchor_invites_you_to_the_mic));
        dialog.setPositiveBtnText(R.string.accept);
        dialog.setNegativeBtnText(R.string.decline);
        dialog.setPositiveListener((v) -> {
            VoiceChatRTCManager.ins().getRTSClient().replyInvite(
                    mRoomInfo.roomId,
                    VoiceChatDataManager.REPLY_TYPE_ACCEPT,
                    event.seatId,
                    new IRequestCallback<InteractReplyResponse>() {
                        @Override
                        public void onSuccess(InteractReplyResponse data) {

                        }

                        @Override
                        public void onError(int errorCode, String message) {
                            SolutionToast.show(ErrorTool.getErrorMessageByErrorCode(errorCode, message));
                        }
                    });
            dialog.dismiss();
        });
        dialog.setNegativeListener((v) -> {
            VoiceChatRTCManager.ins().getRTSClient().replyInvite(
                    mRoomInfo.roomId,
                    VoiceChatDataManager.REPLY_TYPE_REJECT,
                    event.seatId,
                    new IRequestCallback<InteractReplyResponse>() {
                        @Override
                        public void onSuccess(InteractReplyResponse data) {

                        }

                        @Override
                        public void onError(int errorCode, String message) {

                        }
                    });
            dialog.dismiss();
        });
        dialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudienceApplyBroadcast(AudienceApplyEvent event) {
        VoiceChatDataManager.ins().setNewApply(event.hasNewApply);
        mViewBinding.voiceChatMainBottomOption.updateDotTip(event.hasNewApply);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMediaChangedBroadcast(MediaChangedEvent event) {
        if (TextUtils.equals(mHostUserInfo.userId, event.userInfo.userId)) {
            mHostUserInfo.mic = event.mic;
        }
        mViewBinding.voiceChatMainSeatGroup.updateUserMediaStatus(event.userInfo.userId, event.mic == MIC_STATUS_ON);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMediaOperateBroadcast(MediaOperateEvent event) {
        isMicOn = event.mic == MIC_STATUS_ON;
        mSelfUserInfo.mic = event.mic;
        SolutionToast.show(isMicOn
                ? getString(R.string.anchor_has_unmuted_you)
                : getString(R.string.you_have_been_muted_by_the_host));
        mViewBinding.voiceChatMainSeatGroup.updateUserMediaStatus(SolutionDataManager.ins().getUserId(), isMicOn);
        VoiceChatRTCManager.ins().startMuteAudio(!isMicOn);
        mViewBinding.voiceChatMainBottomOption.updateMicStatus(isMicOn);
        int option = isMicOn ? VoiceChatDataManager.MIC_OPTION_ON : VoiceChatDataManager.MIC_OPTION_OFF;
        VoiceChatRTCManager.ins().getRTSClient().updateMediaStatus(mRoomInfo.roomId, mSelfUserInfo.userId, option,
                new IRequestCallback<VoiceChatResponse>() {
                    @Override
                    public void onSuccess(VoiceChatResponse data) {

                    }

                    @Override
                    public void onError(int errorCode, String message) {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClearUserBroadcast(ClearUserEvent event) {
        if (TextUtils.equals(mRoomInfo.roomId, event.roomId) &&
                TextUtils.equals(SolutionDataManager.ins().getUserId(), event.userId)) {
            SolutionToast.show(R.string.same_logged_in);
            isLeaveByKickOut = true;
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioStatsEvent(SDKAudioStatsEvent event) {
        String builder = getString(R.string.delay) + ":" + event.rtt + "ms";
        mViewBinding.voiceChatMainStats.setText(builder);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSDKAudioPropertiesEvent(SDKAudioPropertiesEvent event) {
        List<SDKAudioPropertiesEvent.SDKAudioProperties> infos = event.audioPropertiesList;
        if (infos != null && infos.size() != 0) {
            for (SDKAudioPropertiesEvent.SDKAudioProperties info : infos) {
                mViewBinding.voiceChatMainSeatGroup.onUserSpeaker(info.userId, 0);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInteractResultBroadcast(InteractResultEvent event) {
        if (event.reply == VoiceChatDataManager.REPLY_TYPE_REJECT) {
            SolutionToast.show(getString(R.string.viewer_xxx_declined_your_invitation, event.userInfo.userName));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReconnect(SDKReconnectToRoomEvent event) {
        VoiceChatRTCManager.ins().getRTSClient().reconnectToServer(mReconnectCallback);
    }

    public static void openFromList(Activity activity, VoiceChatRoomInfo roomInfo) {
        Intent intent = new Intent(activity, VoiceChatRoomMainActivity.class);
        intent.putExtra(REFER_KEY, REFER_FROM_LIST);
        intent.putExtra(REFER_EXTRA_ROOM, GsonUtils.gson().toJson(roomInfo));
        VoiceChatUserInfo userInfo = new VoiceChatUserInfo();
        userInfo.userId = SolutionDataManager.ins().getUserId();
        userInfo.userName = SolutionDataManager.ins().getUserName();
        intent.putExtra(REFER_EXTRA_USER, GsonUtils.gson().toJson(userInfo));
        activity.startActivity(intent);
    }

    public static void openFromCreate(Activity activity, VoiceChatRoomInfo roomInfo, VoiceChatUserInfo userInfo, String rtcToken) {
        Intent intent = new Intent(activity, VoiceChatRoomMainActivity.class);
        intent.putExtra(REFER_KEY, REFER_FROM_CREATE);
        JoinRoomResponse response = new JoinRoomResponse();
        response.hostInfo = userInfo;
        response.userInfo = userInfo;
        response.roomInfo = roomInfo;
        response.rtcToken = rtcToken;
        response.audienceCount = 0;
        intent.putExtra(REFER_EXTRA_CREATE_JSON, GsonUtils.gson().toJson(response));
        activity.startActivity(intent);
    }
}
