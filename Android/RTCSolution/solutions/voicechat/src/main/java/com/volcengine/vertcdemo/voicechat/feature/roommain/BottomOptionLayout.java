// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.feature.roommain;

import static com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo.USER_ROLE_HOST;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo;
import com.volcengine.vertcdemo.voicechat.databinding.LayoutVoiceChatMainBottomOptionBinding;

/**
 * 房间页面底部操作控件
 */
public class BottomOptionLayout extends LinearLayout {

    private LayoutVoiceChatMainBottomOptionBinding mViewBinding;

    private IBottomOptions mIBottomOptions;

    public BottomOptionLayout(@NonNull Context context) {
        super(context);
        initView();
    }

    public BottomOptionLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BottomOptionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.layout_voice_chat_main_bottom_option, this);
        mViewBinding = LayoutVoiceChatMainBottomOptionBinding.bind(view);

        mViewBinding.voiceChatMainOptionInputBtn.setOnClickListener((v) -> {
            if (mIBottomOptions != null) {
                mIBottomOptions.onInputClick();
            }
        });
        mViewBinding.voiceChatMainOptionInteractBtn.setOnClickListener((v) -> {
            if (mIBottomOptions != null) {
                mIBottomOptions.onInteractClick();
            }
        });
        mViewBinding.voiceChatMainOptionBgmBtn.setOnClickListener((v) -> {
            if (mIBottomOptions != null) {
                mIBottomOptions.onBGMClick();
            }
        });
        mViewBinding.voiceChatMainOptionMicOnBtn.setOnClickListener((v) -> {
            if (mIBottomOptions != null) {
                mIBottomOptions.onMicClick();
            }
        });
        mViewBinding.voiceChatMainOptionFinishBtn.setOnClickListener((v) -> {
            if (mIBottomOptions != null) {
                mIBottomOptions.onCloseClick();
            }
        });
    }

    public void setOptionCallback(IBottomOptions callback) {
        mIBottomOptions = callback;
    }

    public void updateUIByRoleAndStatus(@VoiceChatUserInfo.UserRole int role, @VoiceChatUserInfo.UserStatus int status) {
        if (role == USER_ROLE_HOST) {
            mViewBinding.voiceChatMainOptionInteractBtn.setVisibility(VISIBLE);
            mViewBinding.voiceChatMainOptionBgmBtn.setVisibility(VISIBLE);
            mViewBinding.voiceChatMainOptionMicOnBtn.setVisibility(VISIBLE);
        } else {
            mViewBinding.voiceChatMainOptionInteractBtn.setVisibility(GONE);
            mViewBinding.voiceChatMainOptionBgmBtn.setVisibility(GONE);
            boolean isInteracting = status == VoiceChatUserInfo.USER_STATUS_INTERACT;
            mViewBinding.voiceChatMainOptionMicOnBtn.setVisibility(isInteracting ? VISIBLE : GONE);
        }
    }

    public void updateMicStatus(boolean isMicOn) {
        mViewBinding.voiceChatMainOptionMicOnBtn.setImageResource(isMicOn
                ? R.drawable.voice_chat_seat_option_mic_on
                : R.drawable.voice_chat_seat_option_mic_off);
    }

    public void updateDotTip(boolean withDot) {
        mViewBinding.voiceChatMainOptionInteractBtn.setImageResource(withDot
                ? R.drawable.voice_chat_main_option_interact_with_dot
                : R.drawable.voice_chat_main_option_interact);
    }

    public interface IBottomOptions {

        void onInputClick();

        void onInteractClick();

        void onBGMClick();

        void onMicClick();

        void onCloseClick();
    }
}
