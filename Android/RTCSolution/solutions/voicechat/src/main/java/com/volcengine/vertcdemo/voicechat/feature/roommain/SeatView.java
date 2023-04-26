// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.feature.roommain;

import static android.view.animation.Animation.INFINITE;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.SEAT_STATUS_LOCKED;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.SEAT_STATUS_UNLOCKED;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatSeatInfo;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;
import com.volcengine.vertcdemo.voicechat.databinding.LayoutVoiceChatSeatBinding;

import java.util.Locale;

/**
 * 单个座位消息展示控件
 */
public class SeatView extends ConstraintLayout {

    private static final int THRESH_HOLDER = 60;

    private final int mNameColor = Color.parseColor("#F2F3F5");
    private final int mSeatColor = Color.parseColor("#C9CDD4");

    private LayoutVoiceChatSeatBinding mViewBinding;

    private final VoiceChatSeatInfo mSeatInfo = new VoiceChatSeatInfo();

    private ValueAnimator mValueAnimator;

    public SeatView(@NonNull Context context) {
        super(context);
        initView();
    }

    public SeatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SeatView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.layout_voice_chat_seat, this);
        mViewBinding = LayoutVoiceChatSeatBinding.bind(view);

        startVolumeAnimation();
        mViewBinding.voiceChatSeatNameBg.setVisibility(INVISIBLE);
        mViewBinding.voiceChatSeatNameAnimation.setVisibility(INVISIBLE);
    }

    public void setIndex(int index) {
        mSeatInfo.seatIndex = index;
    }

    public int getIndex() {
        return mSeatInfo.seatIndex;
    }

    public void bind(@Nullable VoiceChatSeatInfo info) {
        if (info == null) {
            mSeatInfo.userInfo = null;
        } else {
            VoiceChatUserInfo userInfo = info.userInfo;
            mSeatInfo.userInfo = userInfo == null ? null : userInfo.deepCopy();
        }

        if (info == null) {
            mViewBinding.voiceChatSeatNamePrefix.setText("");
            mViewBinding.voiceChatSeatName.setTextColor(mSeatColor);
            mViewBinding.voiceChatSeatName.setText(getContext().getString(R.string.xxx_mic_location, String.valueOf(mSeatInfo.seatIndex)));
            updateVolumeStatus(0, true);
            updateSeatLockStatus(SEAT_STATUS_UNLOCKED);
            updateMicStatus(null, false);
        } else {
            VoiceChatUserInfo userInfo = info.userInfo;
            if (userInfo == null) {
                boolean isLocked = info.isLocked();
                if (isLocked) {
                    updateSeatLockStatus(SEAT_STATUS_LOCKED);
                } else {
                    updateSeatLockStatus(SEAT_STATUS_UNLOCKED);
                }
                mViewBinding.voiceChatSeatNamePrefix.setText("");
                mViewBinding.voiceChatSeatName.setTextColor(mSeatColor);
                mViewBinding.voiceChatSeatName.setText(getContext().getString(R.string.xxx_mic_location, String.valueOf(mSeatInfo.seatIndex)));
                updateVolumeStatus(0, true);
                updateMicStatus(null, false);
            } else {
                String userName = userInfo.userName;
                mViewBinding.voiceChatSeatName.setTextColor(mNameColor);
                mViewBinding.voiceChatSeatNamePrefix.setBackgroundResource(R.drawable.voice_chat_room_list_name_prefix_icon);
                if (TextUtils.isEmpty(userName)) {
                    mViewBinding.voiceChatSeatNamePrefix.setText("");
                    mViewBinding.voiceChatSeatName.setText("");
                } else {
                    mViewBinding.voiceChatSeatNamePrefix.setText(userName.substring(0, 1));
                    mViewBinding.voiceChatSeatName.setText(userName);
                }
                updateMicStatus(userInfo.userId, userInfo.isMicOn());
                String lastUserId = mSeatInfo.userInfo == null ? null : mSeatInfo.userInfo.userId;
                updateVolumeStatus(0, !TextUtils.equals(lastUserId, userInfo.userId));
            }
        }
    }

    public void updateSeatLockStatus(@VoiceChatDataManager.SeatStatus int status) {
        mSeatInfo.status = status;
        if (mSeatInfo.userInfo == null) {
            int imageRes = status == VoiceChatDataManager.SEAT_STATUS_LOCKED
                    ? R.drawable.voice_chat_seat_locked
                    : R.drawable.voice_chat_seat_unlocked;
            mViewBinding.voiceChatSeatNamePrefix.setBackgroundResource(imageRes);
        } else {
            updateMicStatus(mSeatInfo.userInfo.userId, mSeatInfo.userInfo.isMicOn());
        }
    }

    public void updateMicStatus(String userId, boolean micOn) {
        if (mSeatInfo.userInfo != null && TextUtils.equals(mSeatInfo.userInfo.userId, userId)) {
            mViewBinding.voiceChatSeatNameBg.setVisibility(micOn ? VISIBLE : INVISIBLE);
            mViewBinding.voiceChatSeatNameAnimation.setVisibility(micOn ? VISIBLE : INVISIBLE);
            if (!micOn) {
                mViewBinding.voiceChatSeatNamePrefix.setText("");
                mViewBinding.voiceChatSeatNamePrefix.setBackgroundResource(R.drawable.voice_chat_seat_mic_off);
            } else {
                String userName = TextUtils.isEmpty(mSeatInfo.userInfo.userName) ? " " : mSeatInfo.userInfo.userName;
                mViewBinding.voiceChatSeatNamePrefix.setText(userName.substring(0, 1));
                mViewBinding.voiceChatSeatNamePrefix.setBackgroundResource(R.drawable.voice_chat_room_list_name_prefix_icon);
            }
            mSeatInfo.userInfo.mic = micOn ? VoiceChatUserInfo.MIC_STATUS_ON : VoiceChatUserInfo.MIC_STATUS_OFF;
        } else if (mSeatInfo.userInfo == null) {
            mViewBinding.voiceChatSeatNameBg.setVisibility(INVISIBLE);
            mViewBinding.voiceChatSeatNameAnimation.setVisibility(INVISIBLE);
        }
    }

    private void updateVolumeStatus(int volume, boolean forceUpdate) {
        boolean isSpeaking = volume >= THRESH_HOLDER && mSeatInfo.userInfo.isMicOn();
        mViewBinding.voiceChatSeatNameBg.setVisibility(isSpeaking ? VISIBLE : INVISIBLE);
        mViewBinding.voiceChatSeatNameAnimation.setVisibility(isSpeaking ? VISIBLE : INVISIBLE);
    }

    public void updateVolumeStatus(String userId, int volume) {
        if (mSeatInfo.userInfo != null && TextUtils.equals(mSeatInfo.userInfo.userId, userId)) {
            updateVolumeStatus(volume, false);
        }
    }

    public void setSeatClick(IAction<VoiceChatSeatInfo> action) {
        setOnClickListener((v) -> {
            if (action != null && mSeatInfo != null) {
                action.act(mSeatInfo);
            }
        });
    }

    /**
     * 开始播放音量变化动画
     * 用户说话时，展示该动画的view可见，否则不可见
     */
    private void startVolumeAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.end();
        }
        mValueAnimator = ValueAnimator.ofFloat(0F, 0.4F, 0F);
        mValueAnimator.setInterpolator(new DecelerateAccelerateInterpolator());
        mValueAnimator.setDuration(1600);
        mValueAnimator.setRepeatCount(INFINITE);
        mValueAnimator.addUpdateListener(animation ->
                mViewBinding.voiceChatSeatNameAnimation.setAlpha((float) animation.getAnimatedValue()));
        mValueAnimator.start();
    }

    private static class DecelerateAccelerateInterpolator implements TimeInterpolator {

        @Override
        public float getInterpolation(float input) {
            float result;
            if (input <= 0.5) {
                result = (float) (Math.sin(Math.PI * input)) / 2;
            } else {
                result = (float) (2 - Math.sin(Math.PI * input)) / 2;
            }
            return result;
        }
    }
}
