// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

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

import androidx.annotation.NonNull;

import com.volcengine.vertcdemo.common.LengthFilterWithCallback;
import com.volcengine.vertcdemo.common.SolutionBaseActivity;
import com.volcengine.vertcdemo.common.SolutionToast;
import com.volcengine.vertcdemo.common.TextWatcherAdapter;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.RefreshUserNameEvent;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.ErrorTool;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.core.net.ServerResponse;
import com.volcengine.vertcdemo.entity.LoginInfo;
import com.volcengine.vertcdemo.login.databinding.ActivityLoginBinding;
import com.volcengine.vertcdemo.utils.IMEUtils;

import java.util.regex.Pattern;

public class LoginActivity extends SolutionBaseActivity implements View.OnClickListener {

    private ActivityLoginBinding mViewBinding;

    private boolean mIsPolicyChecked = false;

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

        mViewBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        mViewBinding.verifyConfirm.setOnClickListener(this);
        mViewBinding.verifyRootLayout.setOnClickListener(this);
        mViewBinding.verifyPolicyText.setOnClickListener(this);
        mViewBinding.verifyPolicyText.setText(getSpannedText());
        mViewBinding.verifyPolicyText.setMovementMethod(LinkMovementMethod.getInstance());

        InputFilter userNameFilter = new LengthFilterWithCallback(18, (overflow) -> {
            if (overflow) {
                mViewBinding.verifyInputUserNameWaringTv.setVisibility(View.VISIBLE);
                mViewBinding.verifyInputUserNameWaringTv.setText(getString(R.string.content_limit, "18"));
            } else {
                mViewBinding.verifyInputUserNameWaringTv.setVisibility(View.INVISIBLE);
            }
        });
        InputFilter[] userNameFilters = new InputFilter[]{userNameFilter};
        mViewBinding.verifyInputUserNameEt.setFilters(userNameFilters);

        mViewBinding.verifyInputUserNameEt.addTextChangedListener(mTextWatcher);
        setupConfirmStatus();
    }

    private SpannableStringBuilder getSpannedText() {
        String termsOfService = getString(R.string.login_terms_of_service);
        String privacyPolicy = getString(R.string.login_privacy_policy);
        String completeTip = getString(R.string.read_and_agree, termsOfService, privacyPolicy);

        SpannableStringBuilder ssb = new SpannableStringBuilder(completeTip);
        ForegroundColorSpan greySpan = new ForegroundColorSpan(Color.parseColor("#86909C"));
        ssb.setSpan(greySpan, 0, completeTip.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        int termsIndex = completeTip.indexOf(termsOfService);
        if (termsIndex >= 0) {
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    openBrowser(BuildConfig.TERMS_OF_SERVICE_URL);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setColor(Color.parseColor("#4080FF"));
                    ds.setUnderlineText(false);
                }
            }, termsIndex, termsIndex + termsOfService.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        int policyIndex = completeTip.indexOf(privacyPolicy);
        if (policyIndex >= 0) {
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    openBrowser(BuildConfig.PRIVACY_POLICY_URL);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setColor(Color.parseColor("#4080FF"));
                    ds.setUnderlineText(false);
                }
            }, policyIndex, policyIndex + privacyPolicy.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        return ssb;
    }

    @Override
    public void onClick(View v) {
        if (v == mViewBinding.verifyConfirm) {
            onClickConfirm();
        } else if (v == mViewBinding.verifyRootLayout) {
            IMEUtils.closeIME(mViewBinding.verifyRootLayout);
        } else if (v == mViewBinding.verifyPolicyText) {
            updatePolicyChecked();
            setupConfirmStatus();
        }
    }

    private void setupConfirmStatus() {
        String userName = mViewBinding.verifyInputUserNameEt.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            mViewBinding.verifyConfirm.setAlpha(0.3F);
            mViewBinding.verifyConfirm.setEnabled(false);
        } else {
            boolean matchRegex = Pattern.matches(Constants.INPUT_REGEX, userName);
            if (mIsPolicyChecked && matchRegex) {
                mViewBinding.verifyConfirm.setEnabled(true);
                mViewBinding.verifyConfirm.setAlpha(1F);
            } else {
                if (!matchRegex) {
                    mViewBinding.verifyInputUserNameWaringTv.setVisibility(View.VISIBLE);
                    mViewBinding.verifyInputUserNameWaringTv.setText(getString(R.string.content_limit, "18"));
                }
                mViewBinding.verifyConfirm.setAlpha(0.3F);
                mViewBinding.verifyConfirm.setEnabled(false);
            }
        }
    }

    private void onClickConfirm() {
        String userName = mViewBinding.verifyInputUserNameEt.getText().toString().trim();
        mViewBinding.verifyConfirm.setEnabled(false);
        IMEUtils.closeIME(mViewBinding.verifyConfirm);
        LoginApi.passwordFreeLogin(userName, new IRequestCallback<ServerResponse<LoginInfo>>() {

            @Override
            public void onSuccess(ServerResponse<LoginInfo> data) {
                LoginInfo login = data.getData();
                if (login == null) {
                    SolutionToast.show(R.string.network_message_1011);
                    return;
                }

                mViewBinding.verifyConfirm.setEnabled(true);
                SolutionDataManager.ins().setUserName(login.user_name);
                SolutionDataManager.ins().setUserId(login.user_id);
                SolutionDataManager.ins().setToken(login.login_token);
                SolutionDemoEventManager.post(new RefreshUserNameEvent(login.user_name, true));
                LoginActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String message) {
                SolutionToast.show(ErrorTool.getErrorMessageByErrorCode(errorCode, message));
                mViewBinding.verifyConfirm.setEnabled(true);
            }
        });
    }

    private void updatePolicyChecked() {
        mIsPolicyChecked = !mIsPolicyChecked;
        mViewBinding.verifyPolicyText.setSelected(mIsPolicyChecked);
    }

    private void openBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
