package com.volcengine.vertcdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ss.bytertc.engine.RTCEngine;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.RefreshUserNameEvent;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.utils.DeleteAccountManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // region 用户头像、用户名称
        updateUserInfo();
        // endregion

        // region 隐私协议、服务协议、免责声明
        View privacyAgreementLayout = view.findViewById(R.id.profile_privacy_agreement);
        TextView privacyAgreementTv = privacyAgreementLayout.findViewById(R.id.left_tv);
        privacyAgreementTv.setText(R.string.privacy_agreement);
        privacyAgreementLayout.setOnClickListener(v -> openBrowser(BuildConfig.URL_PRIVACY_AGREEMENT));

        View userAgreementLayout = view.findViewById(R.id.profile_user_agreement);
        TextView userAgreementTv = userAgreementLayout.findViewById(R.id.left_tv);
        userAgreementTv.setText(R.string.user_agreement);
        userAgreementLayout.setOnClickListener(v -> openBrowser(BuildConfig.URL_USER_AGREEMENT));

        View serviceAgreementLayout = view.findViewById(R.id.profile_service_agreement);
        TextView serviceAgreementTv = serviceAgreementLayout.findViewById(R.id.left_tv);
        serviceAgreementTv.setText(R.string.service_agreement);
        serviceAgreementLayout.setOnClickListener(v -> openBrowser(BuildConfig.URL_SERVICE_AGREEMENT));

        View disclaimerLayout = view.findViewById(R.id.profile_disclaimer);
        TextView disclaimerTv = disclaimerLayout.findViewById(R.id.left_tv);
        disclaimerTv.setText(R.string.disclaimer);
        disclaimerLayout.setOnClickListener(v -> openBrowser(BuildConfig.URL_DISCLAIMER));

        View sdkListLayout = view.findViewById(R.id.profile_sdk_list);
        TextView sdkListTv = sdkListLayout.findViewById(R.id.left_tv);
        sdkListTv.setText(R.string.sdk_list);
        sdkListLayout.setOnClickListener(v -> openBrowser(BuildConfig.URL_SDK_LIST));

        View usedPermissions = view.findViewById(R.id.used_permissions);
        TextView usedPermissionsTv = usedPermissions.findViewById(R.id.left_tv);
        usedPermissionsTv.setText(R.string.used_permissions);
        usedPermissions.setOnClickListener(v -> openBrowser(BuildConfig.URL_USED_PERMISSIONS));
        // endregion

        // region App 信息、SDK 信息
        View demoVersionLayout = view.findViewById(R.id.profile_demo_version);
        TextView demoVersionLabel = demoVersionLayout.findViewById(R.id.left_tv);
        demoVersionLabel.setText(R.string.demo_version_label);
        TextView demoVersionTv = demoVersionLayout.findViewById(R.id.right_tv);
        demoVersionTv.setText(String.format("v%1$s", BuildConfig.VERSION_NAME));

        View sdkVersionLayout = view.findViewById(R.id.profile_sdk_version);
        TextView sdkVersionLabel = sdkVersionLayout.findViewById(R.id.left_tv);
        sdkVersionLabel.setText(R.string.sdk_version_label);
        TextView sdkVersionTv = sdkVersionLayout.findViewById(R.id.right_tv);
        sdkVersionTv.setText(RTCEngine.getSdkVersion());
        // endregion

        view.findViewById(R.id.profile_delete_account).setOnClickListener((v) -> {
            new AlertDialog.Builder(requireContext()).setMessage(R.string.delete_account_confirm_message)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> DeleteAccountManager.delete())
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    })
                    .show();
        });

        view.findViewById(R.id.profile_exit_login).setOnClickListener((v) -> {
            SolutionDataManager.ins().logout();
        });


        // 监听修改用户信息事件
        SolutionDemoEventManager.register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SolutionDemoEventManager.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserInfo(); // TODO 优化更新用户信息逻辑
    }

    private void updateUserInfo() {
        View view = getView();
        if (view == null) return;
        final String userNameStr = SolutionDataManager.ins().getUserName();
        TextView userAvatar = view.findViewById(R.id.profile_user_avatar);
        if (!TextUtils.isEmpty(userNameStr)) {
            userAvatar.setText(userNameStr.substring(0, 1));
        }

        View userNameLayout = view.findViewById(R.id.profile_user_name);
        TextView userNameLabel = userNameLayout.findViewById(R.id.left_tv);
        userNameLabel.setText(R.string.user_name_label);
        TextView userNameTv = userNameLayout.findViewById(R.id.right_tv);

        if (!TextUtils.isEmpty(userNameStr)) {
            userNameTv.setText(userNameStr);
        }
        userNameLayout.setOnClickListener(v -> startActivity(new Intent(Actions.EDIT_PROFILE)));
    }

    private void openBrowser(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshUserNameEvent(RefreshUserNameEvent event) {
        if (event.isSuccess) {
            SolutionDataManager.ins().setUserName(event.userName);
            updateUserInfo();
        }
    }
}