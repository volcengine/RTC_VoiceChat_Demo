// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.feature.roommain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.volcengine.vertcdemo.common.BaseDialog;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatRTCManager;
import com.volcengine.vertcdemo.voicechat.databinding.DialogVoiceChatBgmSettingBinding;

/**
 * 背景音乐设置对话框
 */
@SuppressWarnings("unused")
public class BGMSettingDialog extends BaseDialog {

    private DialogVoiceChatBgmSettingBinding mViewBinding;

    private boolean mBGMStatus;
    private int mBGMVolume = 0;
    private int mUserVolume = 0;

    public BGMSettingDialog(@NonNull Context context) {
        super(context);
    }

    public BGMSettingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BGMSettingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mViewBinding = DialogVoiceChatBgmSettingBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        
        super.onCreate(savedInstanceState);
        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        mViewBinding.bgmBgmVolumeSeekbar.setMax(100);
        mViewBinding.bgmUserVolumeSeekbar.setMax(100);
        mViewBinding.bgmBgmVolumeProgress.setText(String.valueOf(mBGMVolume));
        mViewBinding.bgmUserVolumeProgress.setText(String.valueOf(mUserVolume));

        onBGMSwitchChanged(mBGMStatus);
        onBGMVolumeProgressChanged(mBGMVolume);
        onUserVolumeProgressChanged(mUserVolume);

        mViewBinding.bgmSwitchSwitch.setOnCheckedChangeListener((v, checked) -> onBGMSwitchChanged(checked));
        mViewBinding.bgmBgmVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    onBGMVolumeProgressChanged(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mViewBinding.bgmUserVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    onUserVolumeProgressChanged(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setData(boolean bgmSwitch, int bgmVolume, int userVolume) {
        mBGMStatus = bgmSwitch;
        mBGMVolume = bgmVolume;
        mUserVolume = userVolume;
    }

    private void onBGMSwitchChanged(boolean isChecked) {
        mBGMStatus = isChecked;
        mViewBinding.bgmSwitchSwitch.setChecked(isChecked);
        mViewBinding.bgmBgmVolumeLayout.setAlpha(isChecked ? 1 : 0.5F);
        mViewBinding.bgmBgmVolumeSeekbar.setTouchAble(isChecked);
        VoiceChatDataManager.ins().setBGMOpening(isChecked);

        if (isChecked) {
            if (VoiceChatDataManager.ins().getIsFirstSetBGMSwitch()) {
                VoiceChatRTCManager.ins().startAudioMixing(true);
            } else {
                VoiceChatRTCManager.ins().resumeAudioMixing();
            }
            VoiceChatDataManager.ins().setIsFirstSetBGMSwitch(false);
        } else {
            if (VoiceChatDataManager.ins().getIsFirstSetBGMSwitch()) {
                VoiceChatRTCManager.ins().startAudioMixing(false);
            } else {
                VoiceChatRTCManager.ins().pauseAudioMixing();
            }
        }
    }

    private void onBGMVolumeProgressChanged(int progress) {
        mBGMVolume = progress;
        mViewBinding.bgmBgmVolumeProgress.setText(String.valueOf(progress));
        mViewBinding.bgmBgmVolumeSeekbar.setProgress(progress);
        VoiceChatDataManager.ins().setBGMVolume(progress);
        VoiceChatRTCManager.ins().adjustBGMVolume(progress);
    }

    private void onUserVolumeProgressChanged(int progress) {
        mUserVolume = progress;
        mViewBinding.bgmUserVolumeProgress.setText(String.valueOf(progress));
        mViewBinding.bgmUserVolumeSeekbar.setProgress(progress);
        VoiceChatDataManager.ins().setUserVolume(progress);
        VoiceChatRTCManager.ins().adjustUserVolume(progress);
    }
}
