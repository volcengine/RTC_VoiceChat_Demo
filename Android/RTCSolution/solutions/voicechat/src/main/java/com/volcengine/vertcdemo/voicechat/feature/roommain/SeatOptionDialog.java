// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.feature.roommain;

import static com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo.MIC_STATUS_ON;
import static com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo.USER_ROLE_HOST;
import static com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo.USER_STATUS_INTERACT;
import static com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo.USER_STATUS_NORMAL;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.volcengine.vertcdemo.common.SolutionCommonDialog;
import com.volcengine.vertcdemo.common.SolutionToast;
import com.volcengine.vertcdemo.core.net.ErrorTool;
import com.volcengine.vertcdemo.utils.Utils;
import com.volcengine.vertcdemo.common.BaseDialog;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.AudienceChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.InteractChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.MediaChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.ApplyInteractResponse;
import com.volcengine.vertcdemo.voicechat.bean.SeatChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatResponse;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatSeatInfo;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatRTCManager;
import com.volcengine.vertcdemo.voicechat.databinding.DialogVoiceChatSeatOptionBinding;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 座位管理对话框
 */
@SuppressWarnings("unused")
public class SeatOptionDialog extends BaseDialog {

    private DialogVoiceChatSeatOptionBinding mViewBinding;

    private String mRoomId;
    private VoiceChatSeatInfo mSeatInfo;
    private @VoiceChatUserInfo.UserRole
    int mSelfRole;
    private @VoiceChatUserInfo.UserStatus
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
        mViewBinding = DialogVoiceChatSeatOptionBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
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
        mViewBinding.optionInteract.setOnClickListener((v) -> onClickInteract());
        mViewBinding.optionMicSwitch.setOnClickListener((v) -> onClickMicStatus());
        mViewBinding.optionSeatLock.setOnClickListener((v) -> onClickLockStatus());

        boolean isSelfHost = mSelfRole == USER_ROLE_HOST;

        if (mSeatInfo == null) {
            mViewBinding.optionMicSwitch.setVisibility(View.VISIBLE);
            mViewBinding.optionSeatLock.setVisibility(View.VISIBLE);
            updateInteractStatus(SEAT_STATUS_UNLOCKED, USER_STATUS_NORMAL, VoiceChatUserInfo.USER_ROLE_AUDIENCE);
            updateMicStatus(SEAT_STATUS_UNLOCKED, MIC_STATUS_ON, isSelfHost, true);
            updateSeatStatus(SEAT_STATUS_UNLOCKED, false);
        } else {
            VoiceChatUserInfo userInfo = mSeatInfo.userInfo;
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
        VoiceChatRTCManager.ins().getRTSClient().managerSeat(
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

    public void setData(@NonNull String roomId, VoiceChatSeatInfo info,
                        @VoiceChatUserInfo.UserRole int selfRole,
                        @VoiceChatUserInfo.UserStatus int selfStatus) {
        mRoomId = roomId;
        mSelfRole = selfRole;
        mSelfStatus = selfStatus;
        mSeatInfo = info == null ? null : info.deepCopy();
    }

    private void updateInteractStatus(@VoiceChatDataManager.SeatStatus int seatStatus,
                                      @VoiceChatUserInfo.UserStatus int userStatus,
                                      @VoiceChatUserInfo.UserRole int selfRole) {
        int drawableRes = userStatus != USER_STATUS_INTERACT
                ? R.drawable.voice_chat_seat_option_interact_on
                : R.drawable.voice_chat_seat_option_interact_off;
        Drawable drawable = getContext().getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0,
                (int) Utils.dp2Px(44), (int) Utils.dp2Px(44));
        mViewBinding.optionInteract.setCompoundDrawables(null, drawable, null, null);
        if (selfRole == USER_ROLE_HOST) {
            if (userStatus != USER_STATUS_INTERACT) {
                mViewBinding.optionInteract.setText(R.string.invite_to_mic);
            } else {
                mViewBinding.optionInteract.setText(R.string.distinguished_guests);
            }
        } else {
            if (userStatus != USER_STATUS_INTERACT) {
                mViewBinding.optionInteract.setText(R.string.on_mic);
            } else {
                mViewBinding.optionInteract.setText(R.string.leave_mic);
            }
        }
        boolean isLocked = seatStatus == SEAT_STATUS_LOCKED;
        mViewBinding.optionInteract.setVisibility(!isLocked ? View.VISIBLE : View.GONE);
    }

    private void updateMicStatus(@VoiceChatDataManager.SeatStatus int seatStatus, @VoiceChatUserInfo.MicStatus int micStatus, boolean isSelfHost, boolean isEmpty) {
        int drawableRes = micStatus == MIC_STATUS_ON
                ? R.drawable.voice_chat_seat_option_mic_on
                : R.drawable.voice_chat_seat_option_mic_off;
        Drawable drawable = getContext().getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0,
                (int) Utils.dp2Px(44), (int) Utils.dp2Px(44));
        mViewBinding.optionMicSwitch.setCompoundDrawables(null, drawable, null, null);
        mViewBinding.optionMicSwitch.setText(micStatus == MIC_OPTION_ON
                        ? getContext().getString(R.string.mute_mic)
                        : getContext().getString(R.string.unmute));
        boolean isLocked = seatStatus == SEAT_STATUS_LOCKED;
        mViewBinding.optionMicSwitch.setVisibility(!isLocked && isSelfHost && !isEmpty ? View.VISIBLE : View.GONE);
    }

    private void updateSeatStatus(@VoiceChatDataManager.SeatStatus int status, boolean isSelfHost) {
        int drawableRes = status == SEAT_STATUS_UNLOCKED
                ? R.drawable.voice_chat_seat_option_locked
                : R.drawable.voice_chat_seat_option_unlocked;
        Drawable drawable = getContext().getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0,
                (int) Utils.dp2Px(44), (int) Utils.dp2Px(44));
        mViewBinding.optionSeatLock.setText(status == SEAT_STATUS_UNLOCKED
                ? getContext().getString(R.string.lock_mic)
                : getContext().getString(R.string.unlock_mic));

        mViewBinding.optionSeatLock.setCompoundDrawables(null, drawable, null, null);
        mViewBinding.optionSeatLock.setVisibility(isSelfHost ? View.VISIBLE : View.GONE);
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
                if (mSelfStatus == VoiceChatUserInfo.USER_STATUS_APPLYING) {
                    SolutionToast.show(R.string.application_has_been_sent_to_the_host);
                } else if (mSelfStatus == USER_STATUS_INTERACT) {
                    SolutionToast.show(R.string.you_are_on_the_mic);
                } else if (mSelfStatus == USER_STATUS_NORMAL) {
                    VoiceChatRTCManager.ins().getRTSClient().applyInteract(
                            mRoomId, mSeatInfo.seatIndex,
                            new IRequestCallback<ApplyInteractResponse>() {
                                @Override
                                public void onSuccess(ApplyInteractResponse data) {
                                    if (data.needApply) {
                                        SolutionToast.show(R.string.application_has_been_sent_to_the_host);
                                        VoiceChatDataManager.ins().setSelfApply(true);
                                    }
                                }

                                @Override
                                public void onError(int errorCode, String message) {
                                    SolutionToast.show(ErrorTool.getErrorMessageByErrorCode(errorCode, message));
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
                VoiceChatRTCManager.ins().getRTSClient().finishInteract(mRoomId, mSeatInfo.seatIndex,
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
        VoiceChatUserInfo userInfo = mSeatInfo.userInfo;
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
            SolutionCommonDialog dialog = new SolutionCommonDialog(getContext());
            dialog.setMessage(getContext().getString(R.string.are_you_sure_to_block_the_microphone));
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
        VoiceChatUserInfo userInfo = mSeatInfo.userInfo;
        if (userInfo != null && TextUtils.equals(userInfo.userId, userId)) {
            dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudienceChangedBroadcast(AudienceChangedEvent event) {
        checkIfClose(event.userInfo.userId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInteractChangedBroadcast(InteractChangedEvent event) {
        checkIfClose(event.userInfo.userId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMediaChangedBroadcast(MediaChangedEvent event) {
        checkIfClose(event.userInfo.userId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSeatChangedBroadcast(SeatChangedEvent event) {
        if (event.seatId == mSeatInfo.seatIndex && event.type != mSeatInfo.status) {
            dismiss();
        }
    }
}
