package com.volcengine.vertcdemo.common;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ss.video.rtc.demo.basic_module.utils.SPUtils;
import com.volcengine.vertcdemo.core.databinding.LayoutDialogInputTextBinding;

/**
 * 发送消息输入组件
 *
 * 功能：
 * 打开输入面板
 * 自动保存未发送的消息
 *
 * 使用：
 * // 1.AndroidManifest.xml 对应的 activity 增加如下属性：
 * android:windowSoftInputMode="adjustPan"
 *
 * // 2.打开对话框代码
 *private void openInput() {
 *    InputTextDialogFragment.showInput(getSupportFragmentManager(), (this::onSendMessage));
 *}
 *
 * // 3.关闭输入框
 *private void closeInput() {
 *    IMEUtils.closeIME(mViewBinding.getRoot());
 *}
 *
 * // 4.发送消息
 *private void onSendMessage(InputTextDialogFragment fragment, String message) {
 *    if (TextUtils.isEmpty(message)) {
 *        SolutionToast.show(getString(R.string.room_main_message_empty_hint));
 *        return;
 *    }
 *    closeInput();
 *    onReceivedMessage(String.format("%s : %s", SolutionDataManager.ins().getUserName(), message));
 *    try {
 *        message = URLEncoder.encode(message, "UTF-8");
 *    } catch (UnsupportedEncodingException e) {
 *        e.printStackTrace();
 *    }
 *    RTSClient rtsClient = RTCManager.ins().getRTSClient();
 *    if (rtsClient != null) {
 *        rtsClient.sendMessage(getRoomInfo().roomId, message, null);
 *    }
 *    fragment.dismiss();
 *}
 */
public class InputTextDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = InputTextDialogFragment.class.getCanonicalName();
    private static final String SP_KEY_EDIT = "InputTextDialogFragment_editing"; // 待发送的消息
    private static final String SP_KEY_ACTIVITY = "InputTextDialogFragment_activity"; // 上次缓存的activity hashcode

    private LayoutDialogInputTextBinding mViewBinding;

    private IInputCallback mAction;

    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        private int lastBottom;
        @Override
        public void onGlobalLayout() {
            WindowInsetsCompat compat = ViewCompat.getRootWindowInsets(mViewBinding.getRoot());
            if (compat == null) {
                return;
            }
            Insets insets = compat.getInsets(WindowInsetsCompat.Type.ime());
            int bottom = insets.bottom;
            if (lastBottom != 0 && bottom == 0) {
                dismiss();
                mViewBinding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
            }
            lastBottom = bottom;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = LayoutDialogInputTextBinding.inflate(inflater);
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWindowParams();
        initView();
    }

    private void initWindowParams() {
        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(null);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void initView() {
        mViewBinding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        mViewBinding.inputDialogEt.requestFocus();
        mViewBinding.inputDialogSend.setOnClickListener((v) -> {
            String input = mViewBinding.inputDialogEt.getText().toString().trim();
            if (mAction != null) {
                mAction.onSendClick(this, input);
            }
            mViewBinding.inputDialogEt.setText("");
        });

        restoreRecord();
    }

    public void setAction(IInputCallback action) {
        mAction = action;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAction = null;

        saveRecord();
    }

    /**
     * 根据保存的activity的信息判断是否要恢复待发送信息
     */
    private void restoreRecord() {
        String sending = SPUtils.getString(SP_KEY_EDIT, "");
        Activity activity = getActivity();
        String hashCode;
        if (activity == null) {
            hashCode = "";
        } else {
            hashCode = String.valueOf(activity.hashCode());
        }
        String activityRecord = SPUtils.getString(SP_KEY_ACTIVITY, "-1");
        if (!TextUtils.isEmpty(sending) && TextUtils.equals(activityRecord, hashCode)) {
            mViewBinding.inputDialogEt.setText(sending);
            mViewBinding.inputDialogEt.setSelection(sending.length());
        } else {
            SPUtils.putString(SP_KEY_EDIT, "");
            SPUtils.putString(SP_KEY_ACTIVITY, "-1");
        }
    }

    /**
     * 保存当前activity的信息和待发送信息
     */
    private void saveRecord() {
        SPUtils.putString(SP_KEY_EDIT, mViewBinding.inputDialogEt.getText().toString().trim());
        Activity activity = getActivity();
        if (activity != null) {
            SPUtils.putString(SP_KEY_ACTIVITY, String.valueOf(activity.hashCode()));
        }
    }

    public static void showInput(@NonNull FragmentManager manager, IInputCallback sendAction) {
        Fragment fragment = manager.findFragmentByTag(TAG);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
        InputTextDialogFragment inputTextDialogFragment = new InputTextDialogFragment();
        inputTextDialogFragment.setAction(sendAction);
        inputTextDialogFragment.showNow(manager, TAG);
    }

    public interface IInputCallback {

        void onSendClick(InputTextDialogFragment fragment, String text);
    }
}
