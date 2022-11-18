package com.volcengine.vertcdemo.voicechat.feature.roomlist;

import static com.volcengine.vertcdemo.core.SolutionConstants.CLICK_RESET_INTERVAL;
import static com.volcengine.vertcdemo.core.net.rts.RTSInfo.KEY_RTM;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ss.video.rtc.demo.basic_module.acivities.BaseActivity;
import com.ss.video.rtc.demo.basic_module.utils.SafeToast;
import com.ss.video.rtc.demo.basic_module.utils.Utilities;
import com.ss.video.rtc.demo.basic_module.utils.WindowUtils;
import com.vertcdemo.joinrtsparams.bean.JoinRTSRequest;
import com.vertcdemo.joinrtsparams.common.JoinRTSManager;
import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.core.net.ServerResponse;
import com.volcengine.vertcdemo.core.net.rts.RTSBaseClient;
import com.volcengine.vertcdemo.core.net.rts.RTSInfo;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.GetActiveRoomListResponse;
import com.volcengine.vertcdemo.voicechat.bean.VCRoomInfo;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatResponse;
import com.volcengine.vertcdemo.voicechat.core.Constants;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatRTCManager;
import com.volcengine.vertcdemo.voicechat.feature.createroom.CreateVoiceChatRoomActivity;
import com.volcengine.vertcdemo.voicechat.feature.roommain.VoiceChatRoomMainActivity;

import java.util.List;

public class VoiceChatListActivity extends BaseActivity {

    private static final String TAG = "VoiceChatListActivity";

    private View mEmptyListView;

    private RTSInfo mRtmInfo;

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
        mRtmInfo = intent.getParcelableExtra(RTSInfo.KEY_RTM);
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
        RTSBaseClient rtmClient = VoiceChatRTCManager.ins().getRTMClient();
        if (rtmClient == null) {
            finish();
            return;
        }
        rtmClient.login(mRtmInfo.rtmToken, (resultCode, message) -> {
            if (resultCode == RTSBaseClient.LoginCallBack.SUCCESS) {
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
                intent.setClass(Utilities.getApplicationContext(), VoiceChatListActivity.class);
                intent.putExtra(KEY_RTM, data);
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
        JoinRTSRequest request = new JoinRTSRequest();
        request.scenesName = Constants.SOLUTION_NAME_ABBR;
        request.loginToken = SolutionDataManager.ins().getToken();

        JoinRTSManager.setAppInfoAndJoinRTM(request, callback);
    }
}
