// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.feature.roomlist;

import static com.volcengine.vertcdemo.core.SolutionConstants.CLICK_RESET_INTERVAL;
import static com.volcengine.vertcdemo.core.net.rts.RTSInfo.KEY_RTS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vertcdemo.joinrtsparams.bean.JoinRTSRequest;
import com.vertcdemo.joinrtsparams.common.JoinRTSManager;
import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.common.SolutionBaseActivity;
import com.volcengine.vertcdemo.common.SolutionToast;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.AppTokenExpiredEvent;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.core.net.ServerResponse;
import com.volcengine.vertcdemo.core.net.rts.RTSBaseClient;
import com.volcengine.vertcdemo.core.net.rts.RTSInfo;
import com.volcengine.vertcdemo.utils.AppUtil;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.GetActiveRoomListResponse;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatRoomInfo;
import com.volcengine.vertcdemo.voicechat.core.Constants;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatRTCManager;
import com.volcengine.vertcdemo.voicechat.databinding.ActivityVoiceChatListBinding;
import com.volcengine.vertcdemo.voicechat.feature.createroom.CreateVoiceChatRoomActivity;
import com.volcengine.vertcdemo.voicechat.feature.roommain.VoiceChatRoomMainActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 语音聊天室房间列表页面
 */
public class VoiceChatListActivity extends SolutionBaseActivity {

    private static final String TAG = "VoiceChatListActivity";

    private ActivityVoiceChatListBinding mViewBinding;

    private RTSInfo mRTSInfo;

    private long mLastClickCreateTs = 0;
    private long mLastClickRequestTs = 0;

    private final IAction<VoiceChatRoomInfo> mOnClickRoomInfo = roomInfo
            -> VoiceChatRoomMainActivity.openFromList(VoiceChatListActivity.this, roomInfo);

    private final VoiceChatRoomListAdapter mVoiceChatRoomListAdapter = new VoiceChatRoomListAdapter(mOnClickRoomInfo);

    private final IRequestCallback<GetActiveRoomListResponse> mRequestRoomList =
            new IRequestCallback<GetActiveRoomListResponse>() {
                @Override
                public void onSuccess(GetActiveRoomListResponse data) {
                    setRoomList(data.roomList);
                }

                @Override
                public void onError(int errorCode, String message) {
                    SolutionToast.show(message);
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityVoiceChatListBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        initRTSInfo();

        mViewBinding.voiceChatListTitleBarLayout.setLeftBack(v -> finish());
        mViewBinding.voiceChatListTitleBarLayout.setTitle(R.string.voice_chat);
        mViewBinding.voiceChatListTitleBarLayout.setRightRefresh(v -> requestRoomList());

        mViewBinding.voiceChatListCreateRoom.setOnClickListener(v -> onClickCreateRoom());


        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mViewBinding.voiceChatListRv.setLayoutManager(linearLayoutManager);
        mViewBinding.voiceChatListRv.setAdapter(mVoiceChatRoomListAdapter);

        initRTC();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VoiceChatRTCManager.ins().getRTSClient().removeAllEventListener();
        VoiceChatRTCManager.ins().getRTSClient().logout();
        VoiceChatRTCManager.ins().destroyEngine();
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

    /**
     * 获取RTS信息
     */
    private void initRTSInfo() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mRTSInfo = intent.getParcelableExtra(RTSInfo.KEY_RTS);
        if (mRTSInfo == null || !mRTSInfo.isValid()) {
            finish();
        }
    }

    /**
     * 初始化RTC
     */
    private void initRTC() {
        VoiceChatRTCManager.ins().initEngine(mRTSInfo);
        RTSBaseClient rtsClient = VoiceChatRTCManager.ins().getRTSClient();
        if (rtsClient == null) {
            finish();
            return;
        }
        rtsClient.login(mRTSInfo.rtsToken, (resultCode, message) -> {
            if (resultCode == RTSBaseClient.LoginCallBack.SUCCESS) {
                requestRoomList();
            } else {
                SolutionToast.show("Login Rtm Fail Error:" + resultCode + ",Message:" + message);
            }
        });
    }

    private void requestRoomList() {
        long now = System.currentTimeMillis();
        if (now - mLastClickRequestTs <= CLICK_RESET_INTERVAL) {
            return;
        }
        mLastClickRequestTs = now;

        VoiceChatRTCManager.ins().getRTSClient().requestClearUser(() -> {
            mLastClickRequestTs = 0;
            VoiceChatRTCManager.ins().getRTSClient().getActiveRoomList(mRequestRoomList);
        });
    }

    private void setRoomList(List<VoiceChatRoomInfo> roomList) {
        mVoiceChatRoomListAdapter.setRoomList(roomList);
        mViewBinding.voiceChatEmptyListView.setVisibility(
                (roomList == null || roomList.isEmpty()) ? View.VISIBLE : View.GONE);
    }

    private void onClickCreateRoom() {
        long now = System.currentTimeMillis();
        if (now - mLastClickCreateTs <= CLICK_RESET_INTERVAL) {
            return;
        }
        mLastClickCreateTs = now;
        CreateVoiceChatRoomActivity.open(this);
    }

    @Keep
    @SuppressWarnings("unused")
    public static void prepareSolutionParams(Activity activity, IAction<Object> doneAction) {
        Log.d(TAG, "prepareSolutionParams() invoked");
        IRequestCallback<ServerResponse<RTSInfo>> callback = new IRequestCallback<ServerResponse<RTSInfo>>() {
            @Override
            public void onSuccess(ServerResponse<RTSInfo> response) {
                RTSInfo data = response == null ? null : response.getData();
                if (data == null || !data.isValid()) {
                    onError(-1, "");
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClass(AppUtil.getApplicationContext(), VoiceChatListActivity.class);
                intent.putExtra(KEY_RTS, data);
                activity.startActivity(intent);
                if (doneAction != null) {
                    doneAction.act(null);
                }
            }

            @Override
            public void onError(int errorCode, String message) {
                if (doneAction != null) {
                    doneAction.act(null);
                }
            }
        };
        JoinRTSRequest request = new JoinRTSRequest(Constants.SOLUTION_NAME_ABBR, SolutionDataManager.ins().getToken());
        JoinRTSManager.setAppInfoAndJoinRTM(request, callback);
    }
}
