// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.common;

import static com.volcengine.vertcdemo.core.net.http.AppNetworkStatusEvent.NETWORK_STATUS_CONNECTED;
import static com.volcengine.vertcdemo.core.net.http.AppNetworkStatusEvent.NETWORK_STATUS_DISCONNECTED;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.http.AppNetworkStatusEvent;
import com.volcengine.vertcdemo.core.net.http.AppNetworkStatusUtil;
import com.volcengine.vertcdemo.core.net.http.TopTipView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class SolutionBaseActivity extends AppCompatActivity {

    private static final String TAG = "SolutionBaseActivity";

    private static final String RECORD_KEY_MICROPHONE = "permission_microphone";
    private static final String RECORD_KEY_CAMERA = "permission_camera";
    private static final String RECORD_KEY_LOCALE = "locale";

    private static final String LOADING_FRAGMENT_TAG = "loading";

    private static final int STATUS_UNSET = 0;
    private static final int STATUS_GRANT = 1;
    private static final int STATUS_REJECT = 2;

    private final HashSet<Integer> mRequestCodes = new HashSet<>();
    private TopTipView mTopTipView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        transparentStatusBar();
        super.onCreate(savedInstanceState);

        if (checkIfLocaleChanged(savedInstanceState)) {
            Log.d(TAG, "checkIfLocaleChanged() finish");
            finish();
        }

        if (checkIfMicPermissionChanged(savedInstanceState)) {
            if (onMicrophonePermissionClose()) {
                return;
            }
        }
        if (checkIfCameraPermissionChanged(savedInstanceState)) {
            if (onCameraPermissionClose()) {
                return;
            }
        }

        SolutionDemoEventManager.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SolutionDemoEventManager.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAndShowNetworkDisconnect();
    }

    private void transparentStatusBar() {
        Window window = getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(visibility);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        recordLocale(outState);
        recordMicPermission(outState);
        recordCameraPermission(outState);
    }

    /**
     * 记录当前麦克风权限
     * @param outState 系统提供的记录数据对象
     */
    private void recordCameraPermission(@NonNull Bundle outState) {
        int status = hasPermission(Manifest.permission.CAMERA) ? STATUS_GRANT : STATUS_REJECT;
        outState.putInt(RECORD_KEY_CAMERA, status);
        Log.d(TAG, String.format("recordCameraPermission: %d", status));
    }

    /**
     * 记录当前麦克风权限
     * @param outState 系统提供的记录数据对象
     */
    private void recordMicPermission(@NonNull Bundle outState) {
        int status = hasPermission(Manifest.permission.RECORD_AUDIO) ? STATUS_GRANT : STATUS_REJECT;
        outState.putInt(RECORD_KEY_MICROPHONE, status);
        Log.d(TAG, String.format("recordMicPermission: %d", status));
    }

    /**
     * 记录当前系统语言
     * @param outState 系统提供的记录数据对象
     */
    private void recordLocale(@NonNull Bundle outState) {
        String currentLanguage = Locale.getDefault().getLanguage();
        Log.d(TAG, String.format("recordLocale: %s", currentLanguage));
        outState.putString(RECORD_KEY_LOCALE, currentLanguage);
    }

    /**
     * 检查麦克风权限变化情况
     * @param savedInstanceState 系统保存的数据
     * @return true：发生了变化
     */
    private boolean checkIfMicPermissionChanged(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return false;
        }
        int storeStatus = savedInstanceState.getInt(RECORD_KEY_MICROPHONE, STATUS_UNSET);
        int curStatus = hasPermission(Manifest.permission.RECORD_AUDIO) ? STATUS_GRANT : STATUS_REJECT;
        Log.d(TAG, String.format("checkIfPermissionChanged: %d %d", storeStatus, curStatus));
        return storeStatus == STATUS_GRANT && curStatus == STATUS_REJECT;
    }

    /**
     * 检查摄像头权限变化情况
     * @param savedInstanceState 系统保存的数据
     * @return true：发生了变化
     */
    private boolean checkIfCameraPermissionChanged(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return false;
        }
        int storeStatus = savedInstanceState.getInt(RECORD_KEY_CAMERA, STATUS_UNSET);
        int curStatus = hasPermission(Manifest.permission.CAMERA) ? STATUS_GRANT : STATUS_REJECT;
        Log.d(TAG, String.format("checkIfPermissionChanged: %d %d", storeStatus, curStatus));
        return storeStatus == STATUS_GRANT && curStatus == STATUS_REJECT;
    }

    /**
     * 检查系统默认语言是否发生了变化
     * @param savedInstanceState 系统保存的数据
     * @return true：发生了变化
     */
    private boolean checkIfLocaleChanged(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return false;
        }
        String currentLocale = Locale.getDefault().getLanguage();
        return !TextUtils.equals(savedInstanceState.getString(RECORD_KEY_LOCALE), currentLocale);
    }

    protected boolean onMicrophonePermissionClose() {
        return false;
    }

    protected boolean onCameraPermissionClose() {
        return false;
    }

    protected void onPermissionResult(String permission, boolean granted) {
    }

    protected final boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    protected final void requestPermissions(String... permissions) {
        if (permissions != null && permissions.length > 0) {
            ArrayList<String> temp = new ArrayList<>();
            int length = permissions.length;

            for (String permission : permissions) {
                if (!this.hasPermission(permission)) {
                    temp.add(permission);
                } else {
                    this.onPermissionResult(permission, true);
                }
            }

            if (!temp.isEmpty()) {
                ActivityCompat.requestPermissions(this, (String[])temp.toArray(new String[0]), this.generateRequestCode());
            }
        }
    }

    private int generateRequestCode() {
        int code;
        code = (int)(System.currentTimeMillis() % 100L);
        while (mRequestCodes.contains(code)) {
            ++code;
        }

        mRequestCodes.add(code);
        return code;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (this.mRequestCodes.contains(requestCode)) {

            for (String permission : permissions) {
                this.onPermissionResult(permission, this.hasPermission(permission));
            }
        }
    }

    protected void checkAndShowNetworkDisconnect() {
        if (AppNetworkStatusUtil.isConnected(getApplicationContext())) {
            onNetworkAvailable();
        } else {
            onNetworkUnavailable();
        }
    }

    private void onNetworkAvailable() {
        Log.d(TAG, "onNetworkAvailable()");
        if (mTopTipView != null) {
            mTopTipView.setVisibility(View.GONE);
            ViewGroup rootView = ((ViewGroup) findViewById(android.R.id.content));
            if (rootView != null) {
                rootView.removeView(mTopTipView);
            }
        }
    }

    private void onNetworkUnavailable() {
        Log.d(TAG, "onNetworkUnavailable()");
        if (mTopTipView == null) {
            mTopTipView = new TopTipView(this);
            ViewGroup rootView = ((ViewGroup) findViewById(android.R.id.content));
            if (rootView != null) {
                rootView.addView(mTopTipView);
            }
        }
        mTopTipView.setNetworkDisconnect();
        mTopTipView.bringToFront();
        mTopTipView.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkStatusChanged(AppNetworkStatusEvent event) {
        if (event.status == NETWORK_STATUS_CONNECTED) {
            onNetworkAvailable();
        } else if (event.status == NETWORK_STATUS_DISCONNECTED) {
            onNetworkUnavailable();
        }
    }

    public void showLoadingDialog() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag(LOADING_FRAGMENT_TAG);
        if (prev != null) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(new ProgressDialogFragment(), LOADING_FRAGMENT_TAG);
        ft.commit();
    }

    public void dismissLoadingDialog() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag(LOADING_FRAGMENT_TAG);
        if (prev != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(prev);
            ft.commit();
        }
    }
}
