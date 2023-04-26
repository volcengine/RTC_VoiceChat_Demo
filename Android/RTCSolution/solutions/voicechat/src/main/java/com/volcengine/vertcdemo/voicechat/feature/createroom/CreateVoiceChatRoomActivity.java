// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.feature.createroom;

import static com.volcengine.vertcdemo.core.SolutionConstants.CLICK_RESET_INTERVAL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.volcengine.vertcdemo.common.SolutionBaseActivity;
import com.volcengine.vertcdemo.common.SolutionToast;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.AppTokenExpiredEvent;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.utils.Utils;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.CreateRoomResponse;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatRTCManager;
import com.volcengine.vertcdemo.voicechat.databinding.ActivityVoiceChatCreateBinding;
import com.volcengine.vertcdemo.voicechat.feature.roommain.VoiceChatRoomMainActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 创建语音聊天室页面
 */
public class CreateVoiceChatRoomActivity extends SolutionBaseActivity {

    private static final String TAG = "CreateVoiceChat";

    private long mLastClickStartLiveTs = 0;

    private ActivityVoiceChatCreateBinding mViewBinding;

    private String mBackgroundImageName;

    private final IRequestCallback<CreateRoomResponse> mCreateRoomRequest = new IRequestCallback<CreateRoomResponse>() {
        @Override
        public void onSuccess(CreateRoomResponse data) {
            mLastClickStartLiveTs = 0;
            VoiceChatRoomMainActivity.openFromCreate(CreateVoiceChatRoomActivity.this,
                    data.roomInfo, data.userInfo, data.rtcToken);
            CreateVoiceChatRoomActivity.this.finish();
        }

        @Override
        public void onError(int errorCode, String message) {
            mLastClickStartLiveTs = 0;
            SolutionToast.show(message);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityVoiceChatCreateBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        String hint = getString(R.string.application_experiencing_xxx_title, "20");
        mViewBinding.experienceTimeHint.setText(hint);
        mViewBinding.createVoiceChatExit.setOnClickListener((v) -> onBackPressed());
        mViewBinding.createVoiceChatStart.setBackground(getCreateBtnBackground());
        mViewBinding.createVoiceChatBg0.setImageResource(R.drawable.voice_chat_background_0);
        mViewBinding.createVoiceChatBg1.setImageResource(R.drawable.voice_chat_background_1);
        mViewBinding.createVoiceChatBg2.setImageResource(R.drawable.voice_chat_background_2);
        mViewBinding.createVoiceChatStart.setOnClickListener(v -> createRoom());
        mViewBinding.createVoiceChatBg0.setOnClickListener(this::updateRoomBgSelect);
        mViewBinding.createVoiceChatBg1.setOnClickListener(this::updateRoomBgSelect);
        mViewBinding.createVoiceChatBg2.setOnClickListener(this::updateRoomBgSelect);

        mViewBinding.createVoiceChatRoomNameInput.setText(getString(R.string.xxx_voice_chat_room,
                SolutionDataManager.ins().getUserName()));
        updateRoomBgSelect(mViewBinding.createVoiceChatBg0);
    }

    private Drawable getCreateBtnBackground() {
        float round = Utils.dp2Px(25);
        GradientDrawable createDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#F878BC"), Color.parseColor("#B26CFF")});
        createDrawable.setCornerRadii(new float[]{round, round, round, round, round, round, round, round});
        return createDrawable;
    }

    private void updateRoomBgSelect(View view) {
        mViewBinding.createVoiceChatBg0.setImageDrawable(null);
        mViewBinding.createVoiceChatBg1.setImageDrawable(null);
        mViewBinding.createVoiceChatBg2.setImageDrawable(null);
        ((ImageView) view).setImageResource(R.drawable.voice_chat_background_selected);
        if (view == mViewBinding.createVoiceChatBg1) {
            mBackgroundImageName = "voicechat_background_1";
            mViewBinding.createVoiceChatBackground.setImageResource(R.drawable.voice_chat_background_1);
        } else if (view == mViewBinding.createVoiceChatBg2) {
            mBackgroundImageName = "voicechat_background_2";
            mViewBinding.createVoiceChatBackground.setImageResource(R.drawable.voice_chat_background_2);
        } else {
            mBackgroundImageName = "voicechat_background_0";
            mViewBinding.createVoiceChatBackground.setImageResource(R.drawable.voice_chat_background_0);
        }
    }

    private void createRoom() {
        long now = System.currentTimeMillis();
        if (now - mLastClickStartLiveTs <= CLICK_RESET_INTERVAL) {
            return;
        }
        mLastClickStartLiveTs = now;

        String roomName = mViewBinding.createVoiceChatRoomNameInput.getText().toString();
        if (TextUtils.isEmpty(roomName)) {
            SolutionToast.show(R.string.room_name_cannot_be_empty);
            mLastClickStartLiveTs = 0;
            return;
        }
        VoiceChatRTCManager.ins().getRTSClient().requestStartLive(SolutionDataManager.ins().getUserName(),
                roomName, mBackgroundImageName + ".jpg", mCreateRoomRequest);
    }

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, CreateVoiceChatRoomActivity.class);
        activity.startActivity(intent);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTokenExpiredEvent(AppTokenExpiredEvent event) {
        finish();
    }
}
