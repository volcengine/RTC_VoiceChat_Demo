// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import com.volcengine.vertcdemo.core.R;

public class CommonSeekbarSettingDialog extends AppCompatDialog {
    private final View mView;
    private final CommonSeekbarSettingDialog.CommonSeekbarDialogListener mDialogListener;
    private int mCurrentValue = 0;

    public CommonSeekbarSettingDialog(Context context, final int startValue, int endValue, int currentValue, String title, String unit, CommonSeekbarSettingDialog.CommonSeekbarDialogListener dialogListener) {
        super(context, R.style.SolutionCommonDialog);
        this.setCancelable(true);
        this.mCurrentValue = currentValue;
        this.mDialogListener = dialogListener;
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        this.mView = inflater.inflate(R.layout.dialog_common_seekbar_setting, (ViewGroup)null);
        this.mView.findViewById(R.id.common_seekbar_setting_confirm).setOnClickListener((v) -> {
            if (this.mDialogListener != null) {
                this.mDialogListener.onConfirmClick(this.mCurrentValue);
            }

            this.dismiss();
        });
        this.mView.findViewById(R.id.common_seekbar_setting_cancel).setOnClickListener((v) -> {
            this.dismiss();
        });
        ((TextView)this.mView.findViewById(R.id.common_seekbar_setting_title)).setText(title);
        ((TextView)this.mView.findViewById(R.id.common_seekbar_setting_unit)).setText(unit);
        final TextView valueTv = (TextView)this.mView.findViewById(R.id.common_seekbar_setting_value);
        valueTv.setText(this.mCurrentValue + "");
        SeekBar seekBar = (SeekBar)this.mView.findViewById(R.id.common_seekbar_setting);
        int diff = endValue - startValue;
        int max = diff <= 0 ? 1 : diff;
        seekBar.setMax(max);
        seekBar.setProgress(this.mCurrentValue - startValue);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CommonSeekbarSettingDialog.this.mCurrentValue = progress + startValue;
                valueTv.setText(CommonSeekbarSettingDialog.this.mCurrentValue + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void show() {
        super.show();
        LayoutParams params = this.getWindow().getAttributes();
        params.width = WindowUtils.getScreenWidth(this.getContext());
        params.height = -1;
        this.getWindow().setAttributes(params);
        this.getWindow().setContentView(this.mView, new android.view.ViewGroup.LayoutParams(-1, -1));
    }

    public interface CommonSeekbarDialogListener {
        void onConfirmClick(int var1);

        void onValueChanged(int var1);
    }
}
