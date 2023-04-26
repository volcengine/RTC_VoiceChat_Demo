// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.core.net.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;

/**
 * 应用网络状态判断
 *
 * 参考: https://developer.android.com/reference/android/net/NetworkRequest
 *
 * 需要在 AndroidManifest.xml 申请权限：
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 */
public class AppNetworkStatusUtil {

    private static final String TAG = "AppNetworkStatus";

    private static final NetworkCallback sCallback = new NetworkCallback(){

        /**
         * 网络连接成功
         */
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            Log.e(TAG, "onAvailable");
            SolutionDemoEventManager.post(new AppNetworkStatusEvent(AppNetworkStatusEvent.NETWORK_STATUS_CONNECTED));
        }

        /**
         * 网络已断开连接
         */
        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            Log.e(TAG, "onLost");
            SolutionDemoEventManager.post(new AppNetworkStatusEvent(AppNetworkStatusEvent.NETWORK_STATUS_DISCONNECTED));
        }

        /**
         * 网络连接超时或网络不可达
         */
        @Override
        public void onUnavailable() {
            super.onUnavailable();
            Log.e(TAG, "onUnavailable");
            SolutionDemoEventManager.post(new AppNetworkStatusEvent(AppNetworkStatusEvent.NETWORK_STATUS_DISCONNECTED));
        }
    };

    /**
     * 注册网络状态监听
     * @param context 上下文对象，为空则注册失败
     */
    public static void registerNetworkCallback(@Nullable Context context) {
        if (context == null) {
            Log.e(TAG, "registerNetworkCallback failed, because app context is null");
            return;
        }
        Context appContext = context.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Log.e(TAG, "registerNetworkCallback invoke");
            NetworkRequest.Builder builder = new NetworkRequest.Builder();

            NetworkRequest request = builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .build();

            connectivityManager.registerNetworkCallback(request, sCallback);
        } else {
            Log.e(TAG, "registerNetworkCallback failed, because ConnectivityManager is null");
        }
    }

    /**
     * 取消注册网络状态监听
     * @param context 上下文对象，为空则取消注册失败
     */
    public static void unregisterNetworkCallback(@Nullable Context context) {
        if (context == null) {
            Log.e(TAG, "unregisterNetworkCallback failed, because app context is null");
            return;
        }
        Context appContext = context.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Log.e(TAG, "unregisterNetworkCallback invoke");

            connectivityManager.unregisterNetworkCallback(sCallback);
        } else {
            Log.e(TAG, "unregisterNetworkCallback failed, because ConnectivityManager is null");
        }
    }

    /**
     * 网络是否连接
     * @param context 上下文对象
     * @return true: 处于连接状态
     */
    public static boolean isConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
