package com.volcengine.vertcdemo.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;

import androidx.annotation.Nullable;

/**
 * 权限工具
 */
public class PermissionUtil {
    /**
     * 是否有摄像头权限
     * @param context 上下文对象，null返回false
     * @return 是否有权限
     */
    public static boolean hasCameraPermission(@Nullable Context context) {
        return hasPermission(context, Manifest.permission.CAMERA);
    }

    /**
     * 是否有麦克风权限
     * @param context 上下文对象，null返回false
     * @return 是否有权限
     */
    public static boolean hasAudioPermission(@Nullable Context context) {
        return hasPermission(context, Manifest.permission.RECORD_AUDIO);
    }

    /**
     * 是否有对应权限
     * @param context 上下文对象，null返回false
     * @param permission 权限名
     * @return 是否有权限
     */
    public static boolean hasPermission(@Nullable Context context, String permission) {
        if (context == null) {
            return false;
        }
        int res = context.getApplicationContext().checkPermission(
                permission, android.os.Process.myPid(), Process.myUid());
        return res == PackageManager.PERMISSION_GRANTED;
    }
}
