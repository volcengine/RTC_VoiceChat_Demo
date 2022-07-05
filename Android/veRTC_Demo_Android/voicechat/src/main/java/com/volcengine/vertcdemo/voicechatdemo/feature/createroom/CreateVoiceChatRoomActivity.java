package com.volcengine.vertcdemo.voicechatdemo.feature.createroom;

import static com.volcengine.vertcdemo.core.SolutionConstants.CLICK_RESET_INTERVAL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ss.video.rtc.demo.basic_module.acivities.BaseActivity;
import com.ss.video.rtc.demo.basic_module.utils.SafeToast;
import com.ss.video.rtc.demo.basic_module.utils.Utilities;
import com.ss.video.rtc.demo.basic_module.utils.WindowUtils;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechatdemo.bean.CreateRoomResponse;
import com.volcengine.vertcdemo.voicechatdemo.core.VoiceChatRTCManager;
import com.volcengine.vertcdemo.voicechatdemo.feature.roommain.VoiceChatRoomMainActivity;

public class CreateVoiceChatRoomActivity extends BaseActivity implements View.OnClickListener {

    private long mLastClickStartLiveTs = 0;

    private TextView mCreateRoomBtn;
    private TextView mRoomNameInput;
    private ImageView mRoomBg;
    private ImageView mRoomBg0;
    private ImageView mRoomBg1;
    private ImageView mRoomBg2;

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
            SafeToast.show(message);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_voice_chat_demo_create);
    }

    @Override
    protected void setupStatusBar() {
        WindowUtils.setLayoutFullScreen(getWindow());
    }

    @Override
    protected void onGlobalLayoutCompleted() {
        super.onGlobalLayoutCompleted();

        findViewById(R.id.create_voice_chat_exit).setOnClickListener((v) -> onBackPressed());
        mCreateRoomBtn = findViewById(R.id.create_voice_chat_start);
        mCreateRoomBtn.setBackground(getCreateBtnBackground());
        mRoomNameInput = findViewById(R.id.create_voice_chat_room_name_input);
        mRoomBg = findViewById(R.id.create_voice_chat_background);
        mRoomBg0 = findViewById(R.id.create_voice_chat_bg0);
        mRoomBg0.setImageResource(R.drawable.voice_chat_demo_background_0);
        mRoomBg1 = findViewById(R.id.create_voice_chat_bg1);
        mRoomBg1.setImageResource(R.drawable.voice_chat_demo_background_1);
        mRoomBg2 = findViewById(R.id.create_voice_chat_bg2);
        mRoomBg2.setImageResource(R.drawable.voice_chat_demo_background_2);

        mCreateRoomBtn.setOnClickListener(this);
        mRoomBg0.setOnClickListener(this);
        mRoomBg1.setOnClickListener(this);
        mRoomBg2.setOnClickListener(this);

        mRoomNameInput.setText(String.format("%s的语音聊天室",
                SolutionDataManager.ins().getUserName()));
        updateRoomBgSelect(mRoomBg0);
    }

    @Override
    public void onClick(View v) {
        if (v == mCreateRoomBtn) {
            createRoom();
        } else if (v == mRoomBg0) {
            updateRoomBgSelect(v);
        } else if (v == mRoomBg1) {
            updateRoomBgSelect(v);
        } else if (v == mRoomBg2) {
            updateRoomBgSelect(v);
        }
    }

    private Drawable getCreateBtnBackground() {
        float round = Utilities.dip2Px(25);
        GradientDrawable createDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#F878BC"), Color.parseColor("#B26CFF")});
        createDrawable.setCornerRadii(new float[]{round, round, round, round, round, round, round, round});
        return createDrawable;
    }

    private void updateRoomBgSelect(View view) {
        mRoomBg0.setImageDrawable(null);
        mRoomBg1.setImageDrawable(null);
        mRoomBg2.setImageDrawable(null);
        ((ImageView) view).setImageResource(R.drawable.voice_chat_demo_background_selected);
        if (view == mRoomBg1) {
            mBackgroundImageName = "voicechat_background_1";
            mRoomBg.setImageResource(R.drawable.voice_chat_demo_background_1);
        } else if (view == mRoomBg2) {
            mBackgroundImageName = "voicechat_background_2";
            mRoomBg.setImageResource(R.drawable.voice_chat_demo_background_2);
        } else {
            mBackgroundImageName = "voicechat_background_0";
            mRoomBg.setImageResource(R.drawable.voice_chat_demo_background_0);
        }
    }

    private void createRoom() {
        long now = System.currentTimeMillis();
        if (now - mLastClickStartLiveTs <= CLICK_RESET_INTERVAL) {
            return;
        }
        mLastClickStartLiveTs = now;

        String roomName = mRoomNameInput.getText().toString();
        if (TextUtils.isEmpty(roomName)) {
            SafeToast.show("房间名不能为空");
            mLastClickStartLiveTs = 0;
            return;
        }
        VoiceChatRTCManager.ins().getRTMClient().requestStartLive(SolutionDataManager.ins().getUserName(),
                roomName, mBackgroundImageName + ".jpg", mCreateRoomRequest);
    }

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, CreateVoiceChatRoomActivity.class);
        activity.startActivity(intent);
    }
}
