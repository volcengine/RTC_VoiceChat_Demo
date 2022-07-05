package com.volcengine.vertcdemo.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.ss.video.rtc.demo.basic_module.utils.AppExecutors;
import com.ss.video.rtc.demo.basic_module.utils.Utilities;
import com.volcengine.vertcdemo.core.R;

import java.lang.reflect.Field;

@SuppressLint("SoonBlockedPrivateApi")
public class SolutionToast {
    private static Field sField_TN;
    private static Field sField_TN_Handler;

    private SolutionToast() {
    }

    public static void show(CharSequence message) {
        show(Utilities.getApplicationContext(), message, 0);
    }

    public static void show(@StringRes int resId) {
        show(Utilities.getApplicationContext(), resId, 0);
    }

    public static void show(Context context, CharSequence message, int duration) {
        AppExecutors.mainThread().execute(() -> {
            View view = View.inflate(context, R.layout.solution_toast, null);
            Toast toast = new Toast(context);
            toast.setView(view);
            ((TextView) view).setText(message);
            hook(toast);
            toast.setDuration(duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });
    }

    public static void show(Context context, int resId, int duration) {
        Toast toast = Toast.makeText(context.getApplicationContext(), resId, duration);
        hook(toast);
        toast.show();
    }

    private static void hook(Toast toast) {
        if (Build.VERSION.SDK_INT < 26) {
            try {
                Object tn = sField_TN.get(toast);
                Handler tn_handler = (Handler) sField_TN_Handler.get(tn);
                sField_TN_Handler.set(tn, new SafeHandler(tn_handler));
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

    }

    static {
        try {
            sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (Exception var1) {

        }

    }

    private static class SafeHandler extends Handler {
        private Handler mTN_Handler;

        public SafeHandler(Handler impl) {
            this.mTN_Handler = impl;
        }

        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception var3) {
            }

        }

        public void handleMessage(Message msg) {
            try {
                this.mTN_Handler.handleMessage(msg);
            } catch (Exception var3) {
            }

        }
    }
}
