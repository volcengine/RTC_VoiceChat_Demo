// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.feature.roommain;

import android.content.Context;
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
import com.volcengine.vertcdemo.voicechat.databinding.LayoutVoiceChatSeatGroupBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 所有座位控件集合
 * 包含一个主持人控件和八个嘉宾控件
 */
public class SeatsGroupLayout extends ConstraintLayout {

    private LayoutVoiceChatSeatGroupBinding mViewBinding;

    private final List<SeatView> mSeatInfoList = new ArrayList<>();

    public SeatsGroupLayout(@NonNull Context context) {
        super(context);
        initView();
    }

    public SeatsGroupLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SeatsGroupLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.layout_voice_chat_seat_group, this);
        mViewBinding = LayoutVoiceChatSeatGroupBinding.bind(view);

        mSeatInfoList.add(mViewBinding.seatGroupNum0);
        mSeatInfoList.add(mViewBinding.seatGroupNum1);
        mSeatInfoList.add(mViewBinding.seatGroupNum2);
        mSeatInfoList.add(mViewBinding.seatGroupNum3);
        mSeatInfoList.add(mViewBinding.seatGroupNum4);
        mSeatInfoList.add(mViewBinding.seatGroupNum5);
        mSeatInfoList.add(mViewBinding.seatGroupNum6);
        mSeatInfoList.add(mViewBinding.seatGroupNum7);

        mViewBinding.seatGroupHost.setIndex(0);
        mViewBinding.seatGroupHost.bind(null);

        for (int i = 0; i < mSeatInfoList.size(); i++) {
            SeatView layout = mSeatInfoList.get(i);
            layout.setIndex(i + 1);
            layout.bind(null);
        }
    }

    public void bindHostInfo(VoiceChatUserInfo userInfo) {
        VoiceChatSeatInfo info = new VoiceChatSeatInfo();
        info.status = VoiceChatDataManager.SEAT_STATUS_UNLOCKED;
        info.userInfo = userInfo;
        mViewBinding.seatGroupHost.bind(info);
    }

    public void updateSeatStatus(int seatId, @VoiceChatDataManager.SeatStatus int seatStatus) {
        for (SeatView layout : mSeatInfoList) {
            if (layout.getIndex() == seatId) {
                layout.updateSeatLockStatus(seatStatus);
                break;
            }
        }
    }

    public void bindSeatInfo(Map<Integer, VoiceChatSeatInfo> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        for (Map.Entry<Integer, VoiceChatSeatInfo> seatInfoEntry : map.entrySet()) {
            bindSeatInfo(seatInfoEntry.getKey(), seatInfoEntry.getValue());
        }
    }

    public void bindSeatInfo(int seatId, VoiceChatSeatInfo seatInfo) {
        for (SeatView layout : mSeatInfoList) {
            if (layout.getIndex() == seatId) {
                layout.setIndex(seatId);
                layout.bind(seatInfo);
                break;
            }
        }
    }

    public void updateUserMediaStatus(String userId, boolean micOn) {
        mViewBinding.seatGroupHost.updateMicStatus(userId, micOn);
        for (SeatView layout : mSeatInfoList) {
            layout.updateMicStatus(userId, micOn);
        }
    }

    public void onUserSpeaker(String userId, int volume) {
        mViewBinding.seatGroupHost.updateVolumeStatus(userId, volume);
        for (SeatView layout : mSeatInfoList) {
            layout.updateVolumeStatus(userId, volume);
        }
    }

    public void setSeatClick(IAction<VoiceChatSeatInfo> action) {
        for (SeatView layout : mSeatInfoList) {
            layout.setSeatClick(action);
        }
    }
}