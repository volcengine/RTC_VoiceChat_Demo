package com.volcengine.vertcdemo.login;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ss.video.rtc.demo.basic_module.acivities.BaseActivity;
import com.ss.video.rtc.demo.basic_module.adapter.TextWatcherAdapter;
import com.ss.video.rtc.demo.basic_module.utils.IMEUtils;
import com.ss.video.rtc.demo.basic_module.utils.SafeToast;
import com.ss.video.rtc.demo.basic_module.utils.WindowUtils;
import com.volcengine.vertcdemo.common.LengthFilterWithCallback;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.RefreshUserNameEvent;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.ErrorTool;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.core.net.ServerResponse;
import com.volcengine.vertcdemo.entity.LoginInfo;

import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mUserNameEt;
    private TextView mUserNameError;
    private TextView mConfirmTv;
    private View mRoomLayout;
    private View mToastLayout;
    private TextView mToastTv;
    private TextView mPolicyText;
    private ImageView mPolicyIcon;

    private boolean mIsPolicyChecked = false;

    private final Runnable mAutoDismiss = () -> {
        if (isFinishing()) {
            return;
        }
        if (mToastLayout != null) {
            mToastLayout.setVisibility(View.GONE);
        }
    };

    private final TextWatcherAdapter mTextWatcher = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            setupConfirmStatus();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void setupStatusBar() {
        WindowUtils.setLayoutFullScreen(getWindow());
    }

    @Override
    protected void onGlobalLayoutCompleted() {
        mRoomLayout = findViewById(R.id.verify_root_layout);
        mToastLayout = findViewById(R.id.verify_toast_layout);
        mToastTv = findViewById(R.id.verify_toast_text);
        mUserNameEt = findViewById(R.id.verify_input_user_name_et);
        mUserNameError = findViewById(R.id.verify_input_user_name_waring_tv);
        mConfirmTv = findViewById(R.id.verify_confirm);
        mPolicyIcon = findViewById(R.id.verify_policy_state);
        mPolicyText = findViewById(R.id.verify_policy_text);
        mConfirmTv.setOnClickListener(this);
        mRoomLayout.setOnClickListener(this);
        mPolicyText.setOnClickListener(this);

        SpannableStringBuilder builder = new SpannableStringBuilder("已阅读并同意《服务协议》和《隐私权政策》");
        ForegroundColorSpan greySpan0 = new ForegroundColorSpan(Color.parseColor("#86909C"));
        ForegroundColorSpan greySpan1 = new ForegroundColorSpan(Color.parseColor("#86909C"));
        ForegroundColorSpan blueSpan0 = new ForegroundColorSpan(Color.parseColor("#4080FF"));
        ForegroundColorSpan blueSpan1 = new ForegroundColorSpan(Color.parseColor("#4080FF"));
        builder.setSpan(greySpan0, 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(blueSpan0, 6, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                openBrowser("https://www.volcengine.com/docs/6348/68917");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(Color.parseColor("#4080FF"));
                ds.setUnderlineText(false);
            }
        }, 6, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(greySpan1, 12, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(blueSpan1, 13, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                openBrowser("https://www.volcengine.com/docs/6348/68918");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(Color.parseColor("#4080FF"));
                ds.setUnderlineText(false);
            }
        }, 13, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mPolicyText.setText(builder);
        mPolicyText.setMovementMethod(LinkMovementMethod.getInstance());

        InputFilter userNameFilter = new LengthFilterWithCallback(18, (overflow) -> {
            if (overflow) {
                mUserNameError.setVisibility(View.VISIBLE);
                mUserNameError.setText(R.string.login_input_wrong_content_waring);
            } else {
                mUserNameError.setVisibility(View.INVISIBLE);
            }
        });
        InputFilter[] userNameFilters = new InputFilter[]{userNameFilter};
        mUserNameEt.setFilters(userNameFilters);

        mUserNameEt.addTextChangedListener(mTextWatcher);
        setupConfirmStatus();
    }

    @Override
    public void onClick(View v) {
        if (v == mConfirmTv) {
            onClickConfirm();
        } else if (v == mRoomLayout) {
            IMEUtils.closeIME(mRoomLayout);
        } else if (v == mPolicyText) {
            updatePolicyChecked();
            setupConfirmStatus();
        }
    }

    private void setupConfirmStatus() {
        String userName = mUserNameEt.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            mConfirmTv.setAlpha(0.3F);
            mConfirmTv.setEnabled(false);
        } else {
            boolean matchRegex = Pattern.matches(Constants.INPUT_REGEX, userName);
            if (mIsPolicyChecked && matchRegex) {
                mConfirmTv.setEnabled(true);
                mConfirmTv.setAlpha(1F);
            } else {
                if (!matchRegex) {
                    mUserNameError.setVisibility(View.VISIBLE);
                    mUserNameError.setText(R.string.login_input_wrong_content_waring);
                }
                mConfirmTv.setAlpha(0.3F);
                mConfirmTv.setEnabled(false);
            }
        }
    }

    private void onClickConfirm() {
        String userName = mUserNameEt.getText().toString().trim();
        mConfirmTv.setEnabled(false);
        IMEUtils.closeIME(mConfirmTv);
        LoginApi.passwordFreeLogin(userName, new IRequestCallback<ServerResponse<LoginInfo>>() {

            @Override
            public void onSuccess(ServerResponse<LoginInfo> data) {
                LoginInfo login = data.getData();
                if (login == null) {
                    SafeToast.show(R.string.login_response_empty);
                    return;
                }

                mConfirmTv.setEnabled(true);
                SolutionDataManager.ins().setUserName(login.user_name);
                SolutionDataManager.ins().setUserId(login.user_id);
                SolutionDataManager.ins().setToken(login.login_token);
                SolutionDemoEventManager.post(new RefreshUserNameEvent(login.user_name, true));
                LoginActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String message) {
                showToast(ErrorTool.getErrorMessageByErrorCode(errorCode, message));
                mConfirmTv.setEnabled(true);
            }
        });
    }

    private void showToast(String toast) {
        mToastLayout.setVisibility(View.VISIBLE);
        mToastLayout.removeCallbacks(mAutoDismiss);
        mToastLayout.postDelayed(mAutoDismiss, 2000);
        mToastTv.setText(toast);
    }

    private void updatePolicyChecked() {
        mIsPolicyChecked = !mIsPolicyChecked;
        mPolicyIcon.setImageResource(mIsPolicyChecked ? R.drawable.circle_checked : R.drawable.circle_unchecked);
    }

    private void openBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
