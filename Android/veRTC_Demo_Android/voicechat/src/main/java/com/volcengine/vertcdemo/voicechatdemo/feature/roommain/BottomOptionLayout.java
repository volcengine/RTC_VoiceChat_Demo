package com.volcengine.vertcdemo.voicechatdemo.feature.roommain;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechatdemo.bean.VCRoomInfo;
import com.volcengine.vertcdemo.voicechatdemo.bean.VCUserInfo;

import static com.volcengine.vertcdemo.voicechatdemo.bean.VCUserInfo.USER_ROLE_HOST;

public class BottomOptionLayout extends LinearLayout {

    private TextView mInputTv;
    private ImageView mInteractBtn;
    private ImageView mBGMBtn;
    private ImageView mMicSwitchBtn;
    private ImageView mCloseBtn;

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
        LayoutInflater.from(getContext()).inflate(R.layout.layout_voice_chat_demo_main_bottom_option, this, true);
        mInputTv = findViewById(R.id.voice_chat_demo_main_option_input_btn);
        mInteractBtn = findViewById(R.id.voice_chat_demo_main_option_interact_btn);
        mBGMBtn = findViewById(R.id.voice_chat_demo_main_option_bgm_btn);
        mMicSwitchBtn = findViewById(R.id.voice_chat_demo_main_option_mic_on_btn);
        mCloseBtn = findViewById(R.id.voice_chat_demo_main_option_finish_btn);

        mInputTv.setOnClickListener((v) -> {
            if (mIBottomOptions != null) {
                mIBottomOptions.onInputClick();
            }
        });
        mInteractBtn.setOnClickListener((v) -> {
            if (mIBottomOptions != null) {
                mIBottomOptions.onInteractClick();
            }
        });
        mBGMBtn.setOnClickListener((v) -> {
            if (mIBottomOptions != null) {
                mIBottomOptions.onBGMClick();
            }
        });
        mMicSwitchBtn.setOnClickListener((v) -> {
            if (mIBottomOptions != null) {
                mIBottomOptions.onMicClick();
            }
        });
        mCloseBtn.setOnClickListener((v) -> {
            if (mIBottomOptions != null) {
                mIBottomOptions.onCloseClick();
            }
        });
    }

    public void setOptionCallback(IBottomOptions callback) {
        mIBottomOptions = callback;
    }

    public void updateUIByRoleAndStatus(@VCUserInfo.UserRole int role, @VCUserInfo.UserStatus int status) {


        if (role == USER_ROLE_HOST) {
            mInteractBtn.setVisibility(VISIBLE);
            mBGMBtn.setVisibility(VISIBLE);
            mMicSwitchBtn.setVisibility(VISIBLE);
        } else {
            mInteractBtn.setVisibility(GONE);
            mBGMBtn.setVisibility(GONE);
            boolean isInteracting = status == VCUserInfo.USER_STATUS_INTERACT;
            mMicSwitchBtn.setVisibility(isInteracting ? VISIBLE : GONE);
        }
    }

    public void updateMicStatus(boolean isMicOn) {
        mMicSwitchBtn.setImageResource(isMicOn
                ? R.drawable.voice_chat_demo_seat_option_mic_on
                : R.drawable.voice_chat_demo_seat_option_mic_off);
    }

    public void updateDotTip(boolean withDot) {
        mInteractBtn.setImageResource(withDot
                ? R.drawable.voice_chat_demo_main_option_interact_with_dot
                : R.drawable.voice_chat_demo_main_option_interact);
    }

    public interface IBottomOptions {

        void onInputClick();

        void onInteractClick();

        void onBGMClick();

        void onMicClick();

        void onCloseClick();
    }
}
