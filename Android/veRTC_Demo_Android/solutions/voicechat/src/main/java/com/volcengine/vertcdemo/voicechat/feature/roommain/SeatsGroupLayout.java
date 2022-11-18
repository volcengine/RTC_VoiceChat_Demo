package com.volcengine.vertcdemo.voicechat.feature.roommain;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.VCSeatInfo;
import com.volcengine.vertcdemo.voicechat.bean.VCUserInfo;
import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeatsGroupLayout extends FrameLayout {

    private SeatLayout mHostSeatLayout;
    private final List<SeatLayout> mSeatInfoList = new ArrayList<>();

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
        LayoutInflater.from(getContext()).inflate(R.layout.layout_voice_chat_demo_seat_group, this, true);
        mHostSeatLayout = findViewById(R.id.seat_group_host);
        mSeatInfoList.add(findViewById(R.id.seat_group_num_0));
        mSeatInfoList.add(findViewById(R.id.seat_group_num_1));
        mSeatInfoList.add(findViewById(R.id.seat_group_num_2));
        mSeatInfoList.add(findViewById(R.id.seat_group_num_3));
        mSeatInfoList.add(findViewById(R.id.seat_group_num_4));
        mSeatInfoList.add(findViewById(R.id.seat_group_num_5));
        mSeatInfoList.add(findViewById(R.id.seat_group_num_6));
        mSeatInfoList.add(findViewById(R.id.seat_group_num_7));

        mHostSeatLayout.setIndex(0);
        mHostSeatLayout.bind(null);

        for (int i = 0; i < mSeatInfoList.size(); i++) {
            SeatLayout layout = mSeatInfoList.get(i);
            layout.setIndex(i + 1);
            layout.bind(null);
        }
    }

    public void bindHostInfo(VCUserInfo userInfo) {
        VCSeatInfo info = new VCSeatInfo();
        info.status = VoiceChatDataManager.SEAT_STATUS_UNLOCKED;
        info.userInfo = userInfo;
        mHostSeatLayout.bind(info);
    }

    public void updateSeatStatus(int seatId, @VoiceChatDataManager.SeatStatus int seatStatus) {
        for (SeatLayout layout : mSeatInfoList) {
            if (layout.getIndex() == seatId) {
                layout.updateSeatLockStatus(seatStatus);
                break;
            }
        }
    }

    public void bindSeatInfo(Map<Integer, VCSeatInfo> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        for (Map.Entry<Integer, VCSeatInfo> seatInfoEntry : map.entrySet()) {
            bindSeatInfo(seatInfoEntry.getKey(), seatInfoEntry.getValue());
        }
    }

    public void bindSeatInfo(int seatId, VCSeatInfo seatInfo) {
        for (SeatLayout layout : mSeatInfoList) {
            if (layout.getIndex() == seatId) {
                layout.setIndex(seatId);
                layout.bind(seatInfo);
                break;
            }
        }
    }

    public void updateUserMediaStatus(String userId, boolean micOn) {
        mHostSeatLayout.updateMicStatus(userId, micOn);
        for (SeatLayout layout : mSeatInfoList) {
            layout.updateMicStatus(userId, micOn);
        }
    }

    public void onUserSpeaker(String userId, int volume) {
        mHostSeatLayout.updateVolumeStatus(userId, volume);
        for (SeatLayout layout : mSeatInfoList) {
            layout.updateVolumeStatus(userId, volume);
        }
    }

    public void setSeatClick(IAction<VCSeatInfo> action) {
        for (SeatLayout layout : mSeatInfoList) {
            layout.setSeatClick(action);
        }
    }
}