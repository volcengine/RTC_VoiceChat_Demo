package com.volcengine.vertcdemo.voicechat.feature.roommain;

import static com.volcengine.vertcdemo.voicechat.bean.VCUserInfo.MIC_STATUS_ON;
import static com.volcengine.vertcdemo.voicechat.bean.VCUserInfo.USER_ROLE_HOST;
import static com.volcengine.vertcdemo.voicechat.bean.VCUserInfo.USER_STATUS_INTERACT;
import static com.volcengine.vertcdemo.voicechat.bean.VCUserInfo.USER_STATUS_NORMAL;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.MIC_OPTION_ON;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.SEAT_OPTION_LOCK;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.SEAT_OPTION_MIC_OFF;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.SEAT_OPTION_MIC_ON;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.SEAT_OPTION_UNLOCK;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.SEAT_STATUS_LOCKED;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.SEAT_STATUS_UNLOCKED;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ss.video.rtc.demo.basic_module.ui.CommonDialog;
import com.ss.video.rtc.demo.basic_module.utils.SafeToast;
import com.ss.video.rtc.demo.basic_module.utils.Utilities;
import com.volcengine.vertcdemo.common.BaseDialog;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.AudienceChangedBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.InteractChangedBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.MediaChangedBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.ReplyMicOnResponse;
import com.volcengine.vertcdemo.voicechat.bean.SeatChangedBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.VCSeatInfo;
import com.volcengine.vertcdemo.voicechat.bean.VCUserInfo;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatResponse;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatRTCManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@SuppressWarnings("unused")
public class SeatOptionDialog extends BaseDialog {

    private TextView mInteractBtn;
    private TextView mMicSwitchBtn;
    private TextView mLockBtn;
    private String mRoomId;
    private VCSeatInfo mSeatInfo;
    private @VCUserInfo.UserRole
    int mSelfRole;
    private @VCUserInfo.UserStatus
    int mSelfStatus;

    public SeatOptionDialog(@NonNull Context context) {
        super(context);
    }

    public SeatOptionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SeatOptionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_voice_chat_seat_option);
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public void show() {
        super.show();
        SolutionDemoEventManager.register(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        SolutionDemoEventManager.unregister(this);
    }

    private void initView() {
        mInteractBtn = findViewById(R.id.option_interact);
        mMicSwitchBtn = findViewById(R.id.option_mic_switch);
        mLockBtn = findViewById(R.id.option_seat_lock);

        mInteractBtn.setOnClickListener((v) -> onClickInteract());
        mMicSwitchBtn.setOnClickListener((v) -> onClickMicStatus());
        mLockBtn.setOnClickListener((v) -> onClickLockStatus());

        boolean isSelfHost = mSelfRole == USER_ROLE_HOST;

        if (mSeatInfo == null) {
            mMicSwitchBtn.setVisibility(View.VISIBLE);
            mLockBtn.setVisibility(View.VISIBLE);
            updateInteractStatus(SEAT_STATUS_UNLOCKED, USER_STATUS_NORMAL, VCUserInfo.USER_ROLE_AUDIENCE);
            updateMicStatus(SEAT_STATUS_UNLOCKED, MIC_STATUS_ON, isSelfHost, true);
            updateSeatStatus(SEAT_STATUS_UNLOCKED, false);
        } else {
            VCUserInfo userInfo = mSeatInfo.userInfo;
            if (userInfo == null) {
                updateInteractStatus(mSeatInfo.status, USER_STATUS_NORMAL, mSelfRole);
                updateMicStatus(mSeatInfo.status, MIC_STATUS_ON, isSelfHost, true);
            } else {
                updateInteractStatus(mSeatInfo.status, userInfo.userStatus, mSelfRole);
                updateMicStatus(mSeatInfo.status, userInfo.mic, isSelfHost, false);
            }
            updateSeatStatus(mSeatInfo.status, isSelfHost);
        }
    }

    private void managerSeat(@VoiceChatDataManager.SeatOption int option) {
        VoiceChatRTCManager.ins().getRTMClient().managerSeat(
                mRoomId, mSeatInfo.seatIndex, option,
                new IRequestCallback<VoiceChatResponse>() {
                    @Override
                    public void onSuccess(VoiceChatResponse data) {

                    }

                    @Override
                    public void onError(int errorCode, String message) {

                    }
                });
    }

    public void setData(@NonNull String roomId, VCSeatInfo info,
                        @VCUserInfo.UserRole int selfRole,
                        @VCUserInfo.UserStatus int selfStatus) {
        mRoomId = roomId;
        mSelfRole = selfRole;
        mSelfStatus = selfStatus;
        mSeatInfo = info == null ? null : info.deepCopy();
    }

    private void updateInteractStatus(@VoiceChatDataManager.SeatStatus int seatStatus,
                                      @VCUserInfo.UserStatus int userStatus,
                                      @VCUserInfo.UserRole int selfRole) {
        int drawableRes = userStatus != USER_STATUS_INTERACT
                ? R.drawable.voice_chat_demo_seat_option_interact_on
                : R.drawable.voice_chat_demo_seat_option_interact_off;
        Drawable drawable = getContext().getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0,
                (int) Utilities.dip2Px(44), (int) Utilities.dip2Px(44));
        mInteractBtn.setCompoundDrawables(null, drawable, null, null);
        if (selfRole == USER_ROLE_HOST) {
            if (userStatus != USER_STATUS_INTERACT) {
                mInteractBtn.setText("邀请上麦");
            } else {
                mInteractBtn.setText("下麦嘉宾");
            }
        } else {
            if (userStatus != USER_STATUS_INTERACT) {
                mInteractBtn.setText("上麦");
            } else {
                mInteractBtn.setText("下麦");
            }
        }
        boolean isLocked = seatStatus == SEAT_STATUS_LOCKED;
        mInteractBtn.setVisibility(!isLocked ? View.VISIBLE : View.GONE);
    }

    private void updateMicStatus(@VoiceChatDataManager.SeatStatus int seatStatus, @VCUserInfo.MicStatus int micStatus, boolean isSelfHost, boolean isEmpty) {
        int drawableRes = micStatus == MIC_STATUS_ON
                ? R.drawable.voice_chat_demo_seat_option_mic_on
                : R.drawable.voice_chat_demo_seat_option_mic_off;
        Drawable drawable = getContext().getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0,
                (int) Utilities.dip2Px(44), (int) Utilities.dip2Px(44));
        mMicSwitchBtn.setCompoundDrawables(null, drawable, null, null);
        mMicSwitchBtn.setText(micStatus == MIC_OPTION_ON ? "静音麦位" : "取消静音");
        boolean isLocked = seatStatus == SEAT_STATUS_LOCKED;
        mMicSwitchBtn.setVisibility(!isLocked && isSelfHost && !isEmpty ? View.VISIBLE : View.GONE);
    }

    private void updateSeatStatus(@VoiceChatDataManager.SeatStatus int status, boolean isSelfHost) {
        int drawableRes = status == SEAT_STATUS_UNLOCKED
                ? R.drawable.voice_chat_demo_seat_option_locked
                : R.drawable.voice_chat_demo_seat_option_unlocked;
        Drawable drawable = getContext().getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0,
                (int) Utilities.dip2Px(44), (int) Utilities.dip2Px(44));
        mLockBtn.setText(status == SEAT_STATUS_UNLOCKED ? "封锁麦位" : "解锁麦位");

        mLockBtn.setCompoundDrawables(null, drawable, null, null);
        mLockBtn.setVisibility(isSelfHost ? View.VISIBLE : View.GONE);
    }

    private void onClickInteract() {
        if (mSeatInfo == null) {
            return;
        }
        if (mSeatInfo.userInfo == null) {
            if (mSelfRole == USER_ROLE_HOST) {
                AudienceManagerDialog dialog = new AudienceManagerDialog(getContext());
                dialog.setData(mRoomId, VoiceChatDataManager.ins().getAllowUserApply(),
                        VoiceChatDataManager.ins().hasNewApply(), mSeatInfo.seatIndex);
                dialog.show();
                dismiss();
            } else {
                if (mSelfStatus == VCUserInfo.USER_STATUS_APPLYING) {
                    SafeToast.show("已向主播发送申请");
                } else if (mSelfStatus == USER_STATUS_INTERACT) {
                    SafeToast.show("你已在麦位上");
                } else if (mSelfStatus == USER_STATUS_NORMAL) {
                    VoiceChatRTCManager.ins().getRTMClient().applyInteract(
                            mRoomId, mSeatInfo.seatIndex,
                            new IRequestCallback<ReplyMicOnResponse>() {
                                @Override
                                public void onSuccess(ReplyMicOnResponse data) {
                                    if (data.needApply) {
                                        SafeToast.show("已向主播发送申请");
                                        VoiceChatDataManager.ins().setSelfApply(true);
                                    }
                                }

                                @Override
                                public void onError(int errorCode, String message) {
                                    if (errorCode == 506) {
                                        SafeToast.show("当前麦位已满");
                                    }
                                }
                            });
                    dismiss();
                }
            }
        } else {
            boolean isHost = mSelfRole == USER_ROLE_HOST;
            boolean isSelf = TextUtils.equals(mSeatInfo.userInfo.userId,
                    SolutionDataManager.ins().getUserId());
            if (isHost && !isSelf) {
                managerSeat(VoiceChatDataManager.SEAT_OPTION_END_INTERACT);
            } else if (!isHost && isSelf) {
                VoiceChatRTCManager.ins().getRTMClient().finishInteract(mRoomId, mSeatInfo.seatIndex,
                        new IRequestCallback<VoiceChatResponse>() {
                            @Override
                            public void onSuccess(VoiceChatResponse data) {

                            }

                            @Override
                            public void onError(int errorCode, String message) {

                            }
                        });
                dismiss();
            }
        }
    }

    private void onClickMicStatus() {
        if (mSeatInfo == null || mSeatInfo.userInfo == null) {
            return;
        }
        VCUserInfo userInfo = mSeatInfo.userInfo;
        int option = userInfo.isMicOn() ? SEAT_OPTION_MIC_OFF : SEAT_OPTION_MIC_ON;
        managerSeat(option);
        dismiss();
    }

    private void onClickLockStatus() {
        if (mSeatInfo == null) {
            return;
        }
        if (mSeatInfo.isLocked()) {
            managerSeat(SEAT_OPTION_UNLOCK);
            dismiss();
        } else {
            CommonDialog dialog = new CommonDialog(getContext());
            dialog.setMessage("确定封锁麦位？封锁麦位后，观众无法在此麦位上麦；\n且此麦位上嘉宾将被下麦");
            dialog.setNegativeListener((vv) -> {
                dialog.dismiss();
                dismiss();
            });
            dialog.setPositiveListener((vv) -> {
                managerSeat(SEAT_OPTION_LOCK);
                dialog.dismiss();
                dismiss();
            });
            dialog.show();
        }
    }

    private void checkIfClose(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        VCUserInfo userInfo = mSeatInfo.userInfo;
        if (userInfo != null && TextUtils.equals(userInfo.userId, userId)) {
            dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudienceChangedBroadcast(AudienceChangedBroadcast event) {
        checkIfClose(event.userInfo.userId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInteractChangedBroadcast(InteractChangedBroadcast event) {
        checkIfClose(event.userInfo.userId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMediaChangedBroadcast(MediaChangedBroadcast event) {
        checkIfClose(event.userInfo.userId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSeatChangedBroadcast(SeatChangedBroadcast event) {
        if (event.seatId == mSeatInfo.seatIndex && event.type != mSeatInfo.status) {
            dismiss();
        }
    }
}
