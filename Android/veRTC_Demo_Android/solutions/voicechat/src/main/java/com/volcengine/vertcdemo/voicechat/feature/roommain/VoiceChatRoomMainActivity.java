package com.volcengine.vertcdemo.voicechat.feature.roommain;

import static com.volcengine.vertcdemo.voicechat.bean.InteractChangedBroadcast.FINISH_INTERACT_TYPE_HOST;
import static com.volcengine.vertcdemo.voicechat.bean.VCUserInfo.MIC_STATUS_ON;
import static com.volcengine.vertcdemo.voicechat.bean.VCUserInfo.USER_STATUS_INTERACT;
import static com.volcengine.vertcdemo.voicechat.bean.VCUserInfo.USER_STATUS_NORMAL;
import static com.volcengine.vertcdemo.voicechat.feature.roommain.AudienceManagerDialog.SEAT_ID_BY_SERVER;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ss.video.rtc.demo.basic_module.acivities.BaseActivity;
import com.ss.video.rtc.demo.basic_module.utils.GsonUtils;
import com.ss.video.rtc.demo.basic_module.utils.IMEUtils;
import com.ss.video.rtc.demo.basic_module.utils.SafeToast;
import com.ss.video.rtc.demo.basic_module.utils.Utilities;
import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.common.SolutionCommonDialog;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.SocketConnectEvent;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.AudienceApplyBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.AudienceChangedBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.ClearUserBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.FinishLiveBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.InteractChangedBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.InteractResultBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.JoinRoomResponse;
import com.volcengine.vertcdemo.voicechat.bean.MediaChangedBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.MediaOperateBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.MessageBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.ReceivedInteractBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.ReplyResponse;
import com.volcengine.vertcdemo.voicechat.bean.SeatChangedBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.VCRoomInfo;
import com.volcengine.vertcdemo.voicechat.bean.VCSeatInfo;
import com.volcengine.vertcdemo.voicechat.bean.VCUserInfo;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatResponse;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatRTCManager;
import com.volcengine.vertcdemo.voicechat.event.AudioStatsEvent;
import com.volcengine.vertcdemo.voicechat.event.SDKAudioPropertiesEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

public class VoiceChatRoomMainActivity extends BaseActivity {

    private static final String TAG = "VoiceChatRoomMain";

    private static final String REFER_KEY = "refer";
    private static final String REFER_FROM_CREATE = "create";
    private static final String REFER_FROM_LIST = "list";
    private static final String REFER_EXTRA_ROOM = "extra_room";
    private static final String REFER_EXTRA_USER = "extra_user";
    private static final String REFER_EXTRA_CREATE_JSON = "extra_create_json";

    private View mTopTip;
    private ImageView mBackgroundIv;
    private TextView mRoomTitleTv;
    private TextView mRoomStatsTv;
    private TextView mAudienceCountTv;
    private SeatsGroupLayout mSeatsGroupLayout;
    private BottomOptionLayout mBottomOptionLayout;
    private RecyclerView mVCChatRv;
    private FrameLayout mInputLayout;
    private EditText mInputEt;

    private ChatAdapter mChatAdapter;

    private VCRoomInfo mRoomInfo;
    private VCUserInfo mHostUserInfo;
    private VCUserInfo mSelfUserInfo;

    private boolean isMicOn = true;
    private boolean isLeaveByKickOut = false;

    private final IRequestCallback<JoinRoomResponse> mJoinCallback = new IRequestCallback<JoinRoomResponse>() {
        @Override
        public void onSuccess(JoinRoomResponse data) {
            data.isFromCreate = true;
            initViewWithData(data);
        }

        @Override
        public void onError(int errorCode, String message) {
            if (errorCode == 422) {
                message = "房间不存在，回到房间列表页";
            }
            onArgsError(message);
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

    private final IAction<VCSeatInfo> mOnSeatClick = seatInfo -> {
        if (mInputLayout.getVisibility() == View.VISIBLE) {
            closeInput();
        } else {
            if (seatInfo != null && !mSelfUserInfo.isHost() && seatInfo.isLocked()) {
                return;
            }
            VCUserInfo userInfo = seatInfo == null ? null : seatInfo.userInfo;
            String userId = userInfo == null ? null : userInfo.userId;
            String selfUserId = SolutionDataManager.ins().getUserId();
            boolean isSelf = TextUtils.equals(selfUserId, userId);
            boolean isSelfHost = TextUtils.equals(selfUserId, mHostUserInfo.userId);
            if (!isSelfHost && !isSelf) {
                if (userInfo != null) {
                    return;
                }
                if (mSelfUserInfo.userStatus == USER_STATUS_INTERACT) {
                    SafeToast.show("你已在麦位上");
                    return;
                } else if (mSelfUserInfo.userStatus == USER_STATUS_NORMAL) {
                    if (VoiceChatDataManager.ins().getSelfApply()) {
                        SafeToast.show("已向主播发送申请");
                        return;
                    }
                }
            }
            SeatOptionDialog dialog = new SeatOptionDialog(VoiceChatRoomMainActivity.this);
            dialog.setData(mRoomInfo.roomId, seatInfo, mSelfUserInfo.userRole, mSelfUserInfo.userStatus);
            dialog.show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_voice_chat_demo_main);
    }

    @Override
    protected void onGlobalLayoutCompleted() {
        super.onGlobalLayoutCompleted();

        mTopTip = findViewById(R.id.main_disconnect_tip);
        mBackgroundIv = findViewById(R.id.voice_chat_demo_main_bg);
        mBackgroundIv.setOnClickListener((v) -> closeInput());
        mRoomTitleTv = findViewById(R.id.voice_chat_demo_main_title);
        mRoomStatsTv = findViewById(R.id.voice_chat_demo_main_stats);
        mAudienceCountTv = findViewById(R.id.voice_chat_demo_main_audience_num);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_audience);
        drawable.setBounds(0, 0,
                (int) Utilities.dip2Px(22), (int) Utilities.dip2Px(20));
        mAudienceCountTv.setCompoundDrawables(drawable, null, null, null);

        mSeatsGroupLayout = findViewById(R.id.voice_chat_demo_main_seat_group);
        mSeatsGroupLayout.setSeatClick(mOnSeatClick);

        mBottomOptionLayout = findViewById(R.id.voice_chat_demo_main_bottom_option);
        mBottomOptionLayout.setOptionCallback(mIBottomOptions);

        mChatAdapter = new ChatAdapter();
        mVCChatRv = findViewById(R.id.voice_chat_demo_main_chat_rv);
        mVCChatRv.setLayoutManager(new LinearLayoutManager(
                VoiceChatRoomMainActivity.this, RecyclerView.VERTICAL, false));
        mVCChatRv.setAdapter(mChatAdapter);
        mVCChatRv.setOnClickListener((v) -> closeInput());

        mInputLayout = findViewById(R.id.voice_chat_demo_main_input_layout);
        mInputEt = findViewById(R.id.voice_chat_demo_main_input_et);
        TextView inputSend = findViewById(R.id.voice_chat_demo_main_input_send);
        inputSend.setBackground(getSendBtnBackground());
        inputSend.setOnClickListener((v) -> onSendMessage(mInputEt.getText().toString()));

        closeInput();

        if (!checkArgs()) {
            onArgsError("错误错误，回到房间列表页");
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
        VCRoomInfo roomInfo = GsonUtils.gson().fromJson(roomJson, VCRoomInfo.class);
        VCUserInfo selfInfo = GsonUtils.gson().fromJson(selfJson, VCUserInfo.class);
        if (TextUtils.equals(refer, REFER_FROM_LIST)) {
            VoiceChatRTCManager.ins().getRTMClient().requestJoinRoom(selfInfo.userName, roomInfo.roomId, mJoinCallback);
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
        mRoomTitleTv.setText(data.roomInfo.roomName);
        mAudienceCountTv.setText(String.valueOf(data.audienceCount + 1));
        mRoomInfo = data.roomInfo;
        mHostUserInfo = data.hostInfo;
        mSelfUserInfo = data.userInfo;
        mSeatsGroupLayout.bindHostInfo(data.hostInfo);
        mSeatsGroupLayout.bindSeatInfo(data.seatMap);
        mBottomOptionLayout.updateUIByRoleAndStatus(data.userInfo.userRole, data.userInfo.userStatus);

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
                backgroundRes = R.drawable.voice_chat_demo_background_1;
            } else if (nameStr.contains("voicechat_background_2")) {
                backgroundRes = R.drawable.voice_chat_demo_background_2;
            } else {
                backgroundRes = R.drawable.voice_chat_demo_background_0;
            }
            mBackgroundIv.setImageResource(backgroundRes);
        } catch (Exception e) {
            Log.e(TAG, "setMainBackground exception", e);
        }
    }

    private Drawable getSendBtnBackground() {
        float round = Utilities.dip2Px(14);
        GradientDrawable createDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#1664FF"), Color.parseColor("#1664FF")});
        createDrawable.setCornerRadii(new float[]{round, round, round, round, round, round, round, round});
        return createDrawable;
    }

    @Override
    public void onBackPressed() {
        if (mInputLayout.getVisibility() == View.VISIBLE) {
            closeInput();
        } else {
            attemptLeave();
        }
    }

    @Override
    public void finish() {
        super.finish();
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
            VoiceChatRTCManager.ins().getRTMClient().requestFinishLive(mRoomInfo.roomId, mFinishCallback);
        } else {
            VoiceChatRTCManager.ins().getRTMClient().requestLeaveRoom(mRoomInfo.roomId, mLeaveCallback);
        }
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
        dialog.setMessage("确认结束语音聊天室？");
        dialog.setPositiveListener((v) -> {
            finish();
            dialog.dismiss();
        });
        dialog.setNegativeListener((v) -> dialog.dismiss());
        dialog.show();
    }

    private void onSendMessage(String message) {
        if (mRoomInfo == null) {
            return;
        }
        if (TextUtils.isEmpty(message)) {
            SafeToast.show("发送空消息不能为空");
            return;
        }
        mInputEt.setText("");
        closeInput();
        onReceivedMessage(String.format("%s : %s", mSelfUserInfo.userName, message));
        try {
            message = URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        VoiceChatRTCManager.ins().getRTMClient().sendMessage(mRoomInfo.roomId, message, mSendMessageCallback);
    }

    private void openInput() {
        mInputLayout.setVisibility(View.VISIBLE);
        IMEUtils.openIME(mInputEt);
        mBottomOptionLayout.setVisibility(View.GONE);
    }

    private void closeInput() {
        IMEUtils.closeIME(mInputEt);
        mInputLayout.setVisibility(View.GONE);
        mBottomOptionLayout.setVisibility(View.VISIBLE);
    }

    private void onReceivedMessage(String message) {
        mChatAdapter.addChatMsg(message);
        mVCChatRv.post(() -> mVCChatRv.smoothScrollToPosition(mChatAdapter.getItemCount()));
    }

    private void switchMicStatus() {
        isMicOn = !isMicOn;

        mSeatsGroupLayout.updateUserMediaStatus(SolutionDataManager.ins().getUserId(), isMicOn);
        VoiceChatRTCManager.ins().startMuteAudio(!isMicOn);
        mBottomOptionLayout.updateMicStatus(isMicOn);
        VoiceChatRTCManager.ins().getRTMClient().updateMediaStatus(
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

    private void showTopTip() {
        mTopTip.setVisibility(View.VISIBLE);
    }

    private void hideTopTip() {
        mTopTip.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudienceChangedBroadcast(AudienceChangedBroadcast event) {
        String suffix = event.isJoin ? " 进入了房间" : " 退出了房间";
        onReceivedMessage(event.userInfo.userName + suffix);
        mAudienceCountTv.setText(String.valueOf(event.audienceCount + 1));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishLiveBroadcast(FinishLiveBroadcast event) {
        if (mRoomInfo == null || !TextUtils.equals(event.roomId, mRoomInfo.roomId)) {
            return;
        }
        String message = null;
        boolean isHost = mSelfUserInfo != null && mSelfUserInfo.isHost();
        if (event.type == FinishLiveBroadcast.FINISH_TYPE_AGAINST) {
            message = "直播间内容违规，直播间已被关闭";
        } else if (event.type == FinishLiveBroadcast.FINISH_TYPE_TIMEOUT && isHost) {
            message = "本次体验时间已超过20分钟";
        } else if (!isHost) {
            message = "主播已关闭直播";
        }
        if (!TextUtils.isEmpty(message)) {
            SafeToast.show(message);
        }
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageBroadcast(MessageBroadcast event) {
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
    public void onInteractChangedBroadcast(InteractChangedBroadcast event) {
        VCSeatInfo info = new VCSeatInfo();
        info.userInfo = event.userInfo;
        mSeatsGroupLayout.bindSeatInfo(event.seatId, event.isStart ? info : null);

        String suffix = event.isStart ? " 已上麦" : " 已下麦";
        onReceivedMessage(event.userInfo.userName + suffix);

        boolean isSelf = TextUtils.equals(SolutionDataManager.ins().getUserId(), event.userInfo.userId);
        if (!isSelf) {
            return;
        }
        VoiceChatDataManager.ins().setSelfApply(false);
        if (event.isStart) {
            SafeToast.show("你已上麦");
        } else {
            if (event.type == FINISH_INTERACT_TYPE_HOST) {
                SafeToast.show("你已被主播移出麦位");
            } else {
                SafeToast.show("你已下麦");
            }
        }
        mSelfUserInfo.userStatus = event.isStart ? USER_STATUS_INTERACT : USER_STATUS_NORMAL;
        VoiceChatRTCManager.ins().startAudioCapture(event.isStart);
        VoiceChatRTCManager.ins().startMuteAudio(false);

        mBottomOptionLayout.updateUIByRoleAndStatus(mSelfUserInfo.userRole, mSelfUserInfo.userStatus);
        mBottomOptionLayout.updateMicStatus(event.userInfo.isMicOn());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSeatChangedBroadcast(SeatChangedBroadcast event) {
        mSeatsGroupLayout.updateSeatStatus(event.seatId, event.type);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedInteractBroadcast(ReceivedInteractBroadcast event) {
        SolutionCommonDialog dialog = new SolutionCommonDialog(this);
        dialog.setMessage("主播邀请你上麦，是否接受");
        dialog.setPositiveBtnText(R.string.accept);
        dialog.setNegativeBtnText(R.string.reject);
        dialog.setPositiveListener((v) -> {
            VoiceChatRTCManager.ins().getRTMClient().replyInvite(
                    mRoomInfo.roomId,
                    VoiceChatDataManager.REPLY_TYPE_ACCEPT,
                    event.seatId,
                    new IRequestCallback<ReplyResponse>() {
                        @Override
                        public void onSuccess(ReplyResponse data) {

                        }

                        @Override
                        public void onError(int errorCode, String message) {
                            if (errorCode == 506) {
                                SafeToast.show("当前麦位已满");
                            }
                        }
                    });
            dialog.dismiss();
        });
        dialog.setNegativeListener((v) -> {
            VoiceChatRTCManager.ins().getRTMClient().replyInvite(
                    mRoomInfo.roomId,
                    VoiceChatDataManager.REPLY_TYPE_REJECT,
                    event.seatId,
                    new IRequestCallback<ReplyResponse>() {
                        @Override
                        public void onSuccess(ReplyResponse data) {

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
    public void onAudienceApplyBroadcast(AudienceApplyBroadcast event) {
        VoiceChatDataManager.ins().setNewApply(event.hasNewApply);
        mBottomOptionLayout.updateDotTip(event.hasNewApply);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMediaChangedBroadcast(MediaChangedBroadcast event) {
        if (TextUtils.equals(mHostUserInfo.userId, event.userInfo.userId)) {
            mHostUserInfo.mic = event.mic;
        }
        mSeatsGroupLayout.updateUserMediaStatus(event.userInfo.userId, event.mic == MIC_STATUS_ON);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMediaOperateBroadcast(MediaOperateBroadcast event) {
        isMicOn = event.mic == MIC_STATUS_ON;
        mSelfUserInfo.mic = event.mic;
        SafeToast.show(isMicOn ? "主播已解除对你的静音" : "你已被主播静音");
        mSeatsGroupLayout.updateUserMediaStatus(SolutionDataManager.ins().getUserId(), isMicOn);
        VoiceChatRTCManager.ins().startMuteAudio(!isMicOn);
        mBottomOptionLayout.updateMicStatus(isMicOn);
        int option = isMicOn ? VoiceChatDataManager.MIC_OPTION_ON : VoiceChatDataManager.MIC_OPTION_OFF;
        VoiceChatRTCManager.ins().getRTMClient().updateMediaStatus(mRoomInfo.roomId, mSelfUserInfo.userId, option,
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
    public void onClearUserBroadcast(ClearUserBroadcast event) {
        if (TextUtils.equals(mRoomInfo.roomId, event.roomId) &&
                TextUtils.equals(SolutionDataManager.ins().getUserId(), event.userId)) {
            SafeToast.show("相同ID用户已登录，您已被强制下线");
            isLeaveByKickOut = true;
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioStatsEvent(AudioStatsEvent event) {
        String builder = "延迟:" + event.rtt + "ms";
        mRoomStatsTv.setText(builder);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSDKAudioPropertiesEvent(SDKAudioPropertiesEvent event) {
        List<SDKAudioPropertiesEvent.SDKAudioProperties> infos = event.audioPropertiesList;
        if (infos != null && infos.size() != 0) {
            for (SDKAudioPropertiesEvent.SDKAudioProperties info : infos) {
                mSeatsGroupLayout.onUserSpeaker(info.userId, 0);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInteractResultBroadcast(InteractResultBroadcast event) {
        if (event.reply == VoiceChatDataManager.REPLY_TYPE_REJECT) {
            SafeToast.show(String.format("观众%s拒绝了你的邀请", event.userInfo.userName));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketConnectEvent(SocketConnectEvent event) {
        if (event.status == SocketConnectEvent.ConnectStatus.DISCONNECTED) {
            showTopTip();
        } else if (event.status == SocketConnectEvent.ConnectStatus.RECONNECTED) {
            VoiceChatRTCManager.ins().getRTMClient().reconnectToServer(mReconnectCallback);
        } else if (event.status == SocketConnectEvent.ConnectStatus.CONNECTED) {
            hideTopTip();
        }
    }

    public static void openFromList(Activity activity, VCRoomInfo roomInfo) {
        Intent intent = new Intent(activity, VoiceChatRoomMainActivity.class);
        intent.putExtra(REFER_KEY, REFER_FROM_LIST);
        intent.putExtra(REFER_EXTRA_ROOM, GsonUtils.gson().toJson(roomInfo));
        VCUserInfo userInfo = new VCUserInfo();
        userInfo.userId = SolutionDataManager.ins().getUserId();
        userInfo.userName = SolutionDataManager.ins().getUserName();
        intent.putExtra(REFER_EXTRA_USER, GsonUtils.gson().toJson(userInfo));
        activity.startActivity(intent);
    }

    public static void openFromCreate(Activity activity, VCRoomInfo roomInfo, VCUserInfo userInfo, String rtcToken) {
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
