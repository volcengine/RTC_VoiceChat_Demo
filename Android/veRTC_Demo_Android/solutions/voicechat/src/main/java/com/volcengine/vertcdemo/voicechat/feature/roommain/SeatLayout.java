package com.volcengine.vertcdemo.voicechat.feature.roommain;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.VCSeatInfo;
import com.volcengine.vertcdemo.voicechat.bean.VCUserInfo;
import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;

import java.util.Locale;

import static android.view.animation.Animation.INFINITE;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.SEAT_STATUS_LOCKED;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.SEAT_STATUS_UNLOCKED;

public class SeatLayout extends FrameLayout {

    private static final int THRESH_HOLDER = 60;

    private final int mNameColor = Color.parseColor("#F2F3F5");
    private final int mSeatColor = Color.parseColor("#C9CDD4");

    private TextView mNamePrefix;
    private TextView mUserName;
    private View mVolumeAnimation;
    private View mVolumeStatus;
    private final VCSeatInfo mSeatInfo = new VCSeatInfo();
    private boolean mIsSpeaking = false;

    private ValueAnimator mValueAnimator;

    public SeatLayout(@NonNull Context context) {
        super(context);
        initView();
    }

    public SeatLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SeatLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_voice_chat_demo_seat, this, true);
        mNamePrefix = findViewById(R.id.voice_chat_demo_seat_name_prefix);
        mUserName = findViewById(R.id.voice_chat_demo_seat_name);
        mVolumeAnimation = findViewById(R.id.voice_chat_demo_seat_name_animation);
        mVolumeStatus = findViewById(R.id.voice_chat_demo_seat_name_bg);

        startVolumeAnimation();
        mVolumeStatus.setVisibility(INVISIBLE);
        mVolumeAnimation.setVisibility(INVISIBLE);
    }

    public void setIndex(int index) {
        mSeatInfo.seatIndex = index;
    }

    public int getIndex() {
        return mSeatInfo.seatIndex;
    }

    public void bind(@Nullable VCSeatInfo info) {
        if (info == null) {
            mSeatInfo.userInfo = null;
        } else {
            VCUserInfo userInfo = info.userInfo;
            mSeatInfo.userInfo = userInfo == null ? null : userInfo.deepCopy();
        }

        if (info == null) {
            mNamePrefix.setText("");
            mUserName.setTextColor(mSeatColor);
            mUserName.setText(String.format(Locale.US, "%d号麦位", mSeatInfo.seatIndex));
            updateVolumeStatus(0, true);
            updateSeatLockStatus(SEAT_STATUS_UNLOCKED);
            updateMicStatus(null, false);
        } else {
            VCUserInfo userInfo = info.userInfo;
            if (userInfo == null) {
                boolean isLocked = info.isLocked();
                if (isLocked) {
                    updateSeatLockStatus(SEAT_STATUS_LOCKED);
                } else {
                    updateSeatLockStatus(SEAT_STATUS_UNLOCKED);
                }
                mNamePrefix.setText("");
                mUserName.setTextColor(mSeatColor);
                mUserName.setText(String.format(Locale.US, "%d号麦位", mSeatInfo.seatIndex));
                updateVolumeStatus(0, true);
                updateMicStatus(null, false);
            } else {
                String userName = userInfo.userName;
                mUserName.setTextColor(mNameColor);
                mNamePrefix.setBackgroundResource(R.drawable.voice_chat_demo_room_list_name_prefix_icon);
                if (TextUtils.isEmpty(userName)) {
                    mNamePrefix.setText("");
                    mUserName.setText("");
                } else {
                    mNamePrefix.setText(userName.substring(0, 1));
                    mUserName.setText(userName);
                }
                updateMicStatus(userInfo.userId, userInfo.isMicOn());
                String lastUserId = mSeatInfo == null ? null : (mSeatInfo.userInfo == null ? null : mSeatInfo.userInfo.userId);
                updateVolumeStatus(0, !TextUtils.equals(lastUserId, userInfo.userId));
            }
        }
    }

    public void updateSeatLockStatus(@VoiceChatDataManager.SeatStatus int status) {
        mSeatInfo.status = status;
        if (mSeatInfo.userInfo == null) {
            int imageRes = status == VoiceChatDataManager.SEAT_STATUS_LOCKED
                    ? R.drawable.voice_chat_demo_seat_locked
                    : R.drawable.voice_chat_demo_seat_unlocked;
            mNamePrefix.setBackgroundResource(imageRes);
        } else {
            updateMicStatus(mSeatInfo.userInfo.userId, mSeatInfo.userInfo.isMicOn());
        }
    }

    public void updateMicStatus(String userId, boolean micOn) {
        if (mSeatInfo.userInfo != null && TextUtils.equals(mSeatInfo.userInfo.userId, userId)) {
            mVolumeStatus.setVisibility(micOn ? VISIBLE : INVISIBLE);
            mVolumeAnimation.setVisibility(micOn ? VISIBLE : INVISIBLE);
            if (!micOn) {
                mNamePrefix.setText("");
                mNamePrefix.setBackgroundResource(R.drawable.voice_chat_demo_seat_mic_off);
            } else {
                String userName = TextUtils.isEmpty(mSeatInfo.userInfo.userName) ? " " : mSeatInfo.userInfo.userName;
                mNamePrefix.setText(userName.substring(0, 1));
                mNamePrefix.setBackgroundResource(R.drawable.voice_chat_demo_room_list_name_prefix_icon);
            }
            mSeatInfo.userInfo.mic = micOn ? VCUserInfo.MIC_STATUS_ON : VCUserInfo.MIC_STATUS_OFF;
        } else if (mSeatInfo.userInfo == null) {
            mVolumeStatus.setVisibility(INVISIBLE);
            mVolumeAnimation.setVisibility(INVISIBLE);
        }
    }

    private void updateVolumeStatus(int volume, boolean forceUpdate) {
        boolean isSpeaking = volume >= THRESH_HOLDER && mSeatInfo.userInfo.isMicOn();
        mVolumeStatus.setVisibility(isSpeaking ? VISIBLE : INVISIBLE);
        mVolumeAnimation.setVisibility(isSpeaking ? VISIBLE : INVISIBLE);
    }

    public void updateVolumeStatus(String userId, int volume) {
        if (mSeatInfo.userInfo != null && TextUtils.equals(mSeatInfo.userInfo.userId, userId)) {
            updateVolumeStatus(volume, false);
        }
    }

    public void setSeatClick(IAction<VCSeatInfo> action) {
        setOnClickListener((v) -> {
            if (action != null && mSeatInfo != null) {
                action.act(mSeatInfo);
            }
        });
    }

    private void startVolumeAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.end();
        }
        mValueAnimator = ValueAnimator.ofFloat(0F, 0.4F, 0F);
        mValueAnimator.setInterpolator(new DecelerateAccelerateInterpolator());
        mValueAnimator.setDuration(1600);
        mValueAnimator.setRepeatCount(INFINITE);
        mValueAnimator.addUpdateListener(animation -> {
            if (mVolumeAnimation != null) {
                mVolumeAnimation.setAlpha((float) animation.getAnimatedValue());
            }
        });
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
