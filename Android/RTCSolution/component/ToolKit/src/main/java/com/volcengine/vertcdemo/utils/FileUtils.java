// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    private static final String TAG = "FileUtils";

    public static boolean copyAssetFolder(Context context, String srcName, String dstName) {
        try {
            boolean result;
            String[] files = context.getAssets().list(srcName);
            if (files == null) return false;

            if (files.length == 0) {
                result = FileUtils.copyAssetFile(context, srcName, dstName);
            } else {
                File file = new File(dstName);
                result = file.mkdirs();
                for (String filename : files) {
                    result &= copyAssetFolder(context, srcName + File.separator + filename, dstName + File.separator + filename);
                }
            }
            return result;
        } catch (IOException e) {
            Log.d(TAG, "copyAssetFolder failed srcName:" + srcName + ",msg:" + e.getLocalizedMessage());
            return false;
        }
    }

    public static boolean copyAssetFile(@NonNull Context context, @NonNull String srcName, @NonNull String dstName) {
        AssetManager assetManager = context.getAssets();
        try (InputStream in = assetManager.open(srcName); OutputStream out = new FileOutputStream(dstName)) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return true;
        } catch (IOException e) {
            Log.d(TAG, "copyAssetFile failed srcName:" + srcName + ",msg:" + e.getLocalizedMessage());
            return false;
        }
    }
}
