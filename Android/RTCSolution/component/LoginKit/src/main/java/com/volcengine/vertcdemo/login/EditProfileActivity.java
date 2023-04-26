// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.login;

import android.os.Bundle;
import android.text.TextUtils;

import com.volcengine.vertcdemo.common.CommonTitleLayout;
import com.volcengine.vertcdemo.common.SolutionBaseActivity;
import com.volcengine.vertcdemo.common.SolutionToast;
import com.volcengine.vertcdemo.common.WarningEditText;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.RefreshUserNameEvent;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.ErrorTool;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.core.net.ServerResponse;
import com.volcengine.vertcdemo.login.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends SolutionBaseActivity {

    public static final int USER_NAME_MAX_LENGTH = 18;
    
    private String mUserName;

    private boolean mConfirmEnable = false;
    
    private ActivityEditProfileBinding mViewBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        CommonTitleLayout titleLayout = findViewById(R.id.title_bar_layout);
        titleLayout.setLeftBack(v -> onBackPressed());
        titleLayout.setTitle(R.string.change_user_name);
        titleLayout.setRightText(R.string.ok, v -> onClickConfirm());

        mViewBinding.inputNameEt.setRegex(Constants.INPUT_REGEX, USER_NAME_MAX_LENGTH, new WarningEditText.Callback() {
            @Override
            public void checkResult(boolean valid) {
                mUserName = mViewBinding.inputNameEt.getInputText();
                mConfirmEnable = valid;
            }
        });
        mViewBinding.inputNameEt.setHintText(R.string.please_enter_user_nickname);
        mViewBinding.inputNameEt.setWarningText(getString(R.string.content_limit, String.valueOf(USER_NAME_MAX_LENGTH)));
        mUserName = SolutionDataManager.ins().getUserName();
        mViewBinding.inputNameEt.setInputText(mUserName);

        mViewBinding.profileUserNameClear.setOnClickListener(v -> mViewBinding.inputNameEt.setInputText(""));
    }

    private void onClickConfirm() {
        if (!mConfirmEnable) {
            return;
        }
        String userName = mViewBinding.inputNameEt.getInputText();
        if (!TextUtils.isEmpty(userName)) {
            LoginApi.changeUserName(userName, SolutionDataManager.ins().getToken(),
                    new IRequestCallback<ServerResponse<Void>>() {
                        @Override
                        public void onSuccess(ServerResponse<Void> response) {
                            SolutionDataManager.ins().setUserName(userName);
                            RefreshUserNameEvent event = new RefreshUserNameEvent(userName, true);
                            SolutionDemoEventManager.post(event);
                            finish();
                        }

                        @Override
                        public void onError(int errorCode, String message) {
                            SolutionToast.show(ErrorTool.getErrorMessageByErrorCode(errorCode, message));
                        }
                    });
        }
    }
}
