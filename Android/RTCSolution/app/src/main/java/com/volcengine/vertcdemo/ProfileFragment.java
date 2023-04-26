// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ss.bytertc.engine.RTCVideo;
import com.volcengine.vertcdemo.common.KeyValueView;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.RefreshUserNameEvent;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.utils.AppUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateUserInfo();

        bindSettingData(view, initSettingData());

        view.findViewById(R.id.profile_delete_account).setOnClickListener(v ->
                onClickCancelAccount());

        view.findViewById(R.id.profile_exit_login).setOnClickListener(
                (v) -> SolutionDataManager.ins().logout());

        SolutionDemoEventManager.register(this);
    }

    private List<SettingInfo> initSettingData() {
        List<SettingInfo> list = new ArrayList<>();

        list.add(new SettingInfo(getString(R.string.privacy_policy), null, BuildConfig.PRIVACY_POLICY_URL));
        list.add(new SettingInfo(getString(R.string.terms_service), null, BuildConfig.TERMS_OF_SERVICE_URL));
        list.add(new SettingInfo(getString(R.string.user_agreement), null, "https://www.volcengine.com/docs/6348/128955"));
        list.add(new SettingInfo(getString(R.string.disclaimer), null, "https://www.volcengine.com/docs/6348/68916"));
        list.add(new SettingInfo(getString(R.string.related_party_sdk_list), null, "https://www.volcengine.com/docs/6348/133654"));
        list.add(new SettingInfo(getString(R.string.permission_application_checklist), null, "https://www.volcengine.com/docs/6348/155009"));

        list.add(new SettingInfo(getString(R.string.app_version), String.format("v%1$s", AppUtil.getAppVersionName()), null));
        list.add(new SettingInfo(getString(R.string.sdk_version), RTCVideo.getSDKVersion(), null));
        return list;
    }

    private void bindSettingData(View rootView, List<SettingInfo> infoList) {
        if (infoList == null || infoList.isEmpty()) {
            return;
        }
        LinearLayout container = rootView.findViewById(R.id.setting_container);
        for (SettingInfo info : infoList) {
            KeyValueView keyValueView = new KeyValueView(getContext());
            boolean hasMore = !TextUtils.isEmpty(info.url);
            keyValueView.setKeyValue(info.key, info.value, hasMore);
            if (hasMore) {
                keyValueView.setOnClickListener(v -> openBrowser(info.url));
            }
            container.addView(keyValueView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SolutionDemoEventManager.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserInfo();
    }

    private void updateUserInfo() {
        View view = getView();
        if (view == null) return;
        final String userNameStr = SolutionDataManager.ins().getUserName();
        TextView userAvatar = view.findViewById(R.id.profile_user_avatar);
        if (!TextUtils.isEmpty(userNameStr)) {
            userAvatar.setText(userNameStr.substring(0, 1));
        }

        KeyValueView userNameView = view.findViewById(R.id.profile_user_name);
        userNameView.setKeyValue(getString(R.string.user_name), userNameStr, true);
        userNameView.setOnClickListener(v -> startActivity(new Intent(Actions.EDIT_PROFILE)));
    }

    private void onClickCancelAccount() {
        new AlertDialog.Builder(requireContext()).setMessage(R.string.cancel_account_alert_message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> new ILoginImpl().closeAccount(null))
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                })
                .show();
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

    private static class SettingInfo {

        public final String key;
        public final String value;
        public final String url;

        public SettingInfo(String key, String value, String url) {
            this.key = key;
            this.value = value;
            this.url = url;
        }
    }
}