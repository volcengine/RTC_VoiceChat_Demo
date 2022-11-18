package com.volcengine.vertcdemo.utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AgreementManager {
    public interface ResultCallback {
        void onResult(boolean result);
    }

    public static <T extends AppCompatActivity & ResultCallback> void check(@NonNull T activity) {
        activity.onResult(true);
    }
}
