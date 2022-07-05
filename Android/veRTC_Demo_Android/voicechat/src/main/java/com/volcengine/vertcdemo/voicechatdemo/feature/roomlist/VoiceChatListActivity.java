package com.volcengine.vertcdemo.voicechatdemo.feature.roomlist;

import static com.volcengine.vertcdemo.core.SolutionConstants.CLICK_RESET_INTERVAL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ss.video.rtc.demo.basic_module.acivities.BaseActivity;
import com.ss.video.rtc.demo.basic_module.utils.SafeToast;
import com.ss.video.rtc.demo.basic_module.utils.WindowUtils;
import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.core.net.rtm.RTMBaseClient;
import com.volcengine.vertcdemo.core.net.rtm.RtmInfo;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechatdemo.bean.GetActiveRoomListResponse;
import com.volcengine.vertcdemo.voicechatdemo.bean.VCRoomInfo;
import com.volcengine.vertcdemo.voicechatdemo.bean.VoiceChatResponse;
import com.volcengine.vertcdemo.voicechatdemo.core.VoiceChatRTCManager;
import com.volcengine.vertcdemo.voicechatdemo.feature.createroom.CreateVoiceChatRoomActivity;
import com.volcengine.vertcdemo.voicechatdemo.feature.roommain.VoiceChatRoomMainActivity;

import java.util.List;

public class VoiceChatListActivity extends BaseActivity {

    private View mEmptyListView;

    private RtmInfo mRtmInfo;

    private long mLastClickCreateTs = 0;
    private long mLastClickRequestTs = 0;

    private final IAction<VCRoomInfo> mOnClickRoomInfo = roomInfo
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
                    SafeToast.show(message);
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_voice_chat_demo_list);

        initRtmInfo();
    }

    /**
     * 获取RTM信息
     */
    private void initRtmInfo() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mRtmInfo = intent.getParcelableExtra(RtmInfo.KEY_RTM);
        if (mRtmInfo == null || !mRtmInfo.isValid()) {
            finish();
        }
    }

    @Override
    protected void setupStatusBar() {
        WindowUtils.setLayoutFullScreen(getWindow());
    }

    @Override
    protected void onGlobalLayoutCompleted() {
        super.onGlobalLayoutCompleted();

        ((TextView) findViewById(R.id.title_bar_title_tv)).setText("语音聊天室");
        ImageView backArrow = findViewById(R.id.title_bar_left_iv);
        backArrow.setImageResource(R.drawable.back_arrow);
        backArrow.setOnClickListener(v -> finish());
        ImageView rightIv = findViewById(R.id.title_bar_right_iv);
        rightIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        rightIv.setImageResource(R.drawable.refresh);
        rightIv.setOnClickListener(v -> requestRoomList());

        View createBtn = findViewById(R.id.voice_chat_list_create_room);
        createBtn.setOnClickListener((v) -> onClickCreateRoom());

        RecyclerView dataRv = findViewById(R.id.voice_chat_list_rv);
        dataRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        dataRv.setAdapter(mVoiceChatRoomListAdapter);
        mEmptyListView = findViewById(R.id.voice_chat_empty_list_view);

        initRTC();
    }

    @Override
    public void finish() {
        super.finish();
        VoiceChatRTCManager.ins().getRTMClient().removeAllEventListener();
        VoiceChatRTCManager.ins().getRTMClient().logout();
        VoiceChatRTCManager.ins().destroyEngine();
    }

    /**
     * 初始化RTC
     */
    private void initRTC() {
        VoiceChatRTCManager.ins().initEngine(mRtmInfo);
        RTMBaseClient rtmClient = VoiceChatRTCManager.ins().getRTMClient();
        if (rtmClient == null) {
            finish();
            return;
        }
        rtmClient.login(mRtmInfo.rtmToken, (resultCode, message) -> {
            if (resultCode == RTMBaseClient.LoginCallBack.SUCCESS) {
                requestRoomList();
            } else {
                SafeToast.show("Login Rtm Fail Error:" + resultCode + ",Message:" + message);
            }
        });
    }

    private void requestRoomList() {
        long now = System.currentTimeMillis();
        if (now - mLastClickRequestTs <= CLICK_RESET_INTERVAL) {
            return;
        }
        mLastClickRequestTs = now;

        VoiceChatRTCManager.ins().getRTMClient().requestClearUser(new IRequestCallback<VoiceChatResponse>() {
            @Override
            public void onSuccess(VoiceChatResponse data) {
                mLastClickRequestTs = 0;
                VoiceChatRTCManager.ins().getRTMClient().getActiveRoomList(mRequestRoomList);
            }

            @Override
            public void onError(int errorCode, String message) {
                mLastClickRequestTs = 0;
                VoiceChatRTCManager.ins().getRTMClient().getActiveRoomList(mRequestRoomList);
            }
        });
    }

    private void setRoomList(List<VCRoomInfo> roomList) {
        mVoiceChatRoomListAdapter.setRoomList(roomList);
        mEmptyListView.setVisibility((roomList == null || roomList.isEmpty()) ? View.VISIBLE : View.GONE);
    }

    private void onClickCreateRoom() {
        long now = System.currentTimeMillis();
        if (now - mLastClickCreateTs <= CLICK_RESET_INTERVAL) {
            return;
        }
        mLastClickCreateTs = now;
        CreateVoiceChatRoomActivity.open(this);
    }
}
