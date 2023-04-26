// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.common;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;

import com.volcengine.vertcdemo.core.R;
import com.volcengine.vertcdemo.core.databinding.DialogSolutionCommonBinding;

public class SolutionCommonDialog extends AppCompatDialog {

    private final DialogSolutionCommonBinding mBinding;

    public SolutionCommonDialog(Context context) {
        super(context, R.style.SolutionCommonDialog);
        this.setCancelable(true);

        mBinding = DialogSolutionCommonBinding.inflate(getLayoutInflater());
        mBinding.getRoot().setOnClickListener((v) -> dismiss());
        setContentView(mBinding.getRoot(), new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    }

    public void setMessage(String txt) {
        mBinding.message.setText(txt);
    }

    public void setNegativeListener(@StringRes int text, View.OnClickListener listener) {
        mBinding.negativeButton.setText(text);
        mBinding.negativeButton.setVisibility(View.VISIBLE);
        mBinding.negativeButton.setOnClickListener(listener);
    }

    public void setNegativeListener(View.OnClickListener listener) {
        mBinding.negativeButton.setVisibility(View.VISIBLE);
        mBinding.negativeButton.setOnClickListener(listener);
    }

    public void setPositiveListener(@StringRes int text, View.OnClickListener listener) {
        mBinding.positiveButton.setText(text);
        mBinding.positiveButton.setOnClickListener(listener);
    }

    public void setPositiveListener(View.OnClickListener listener) {
        mBinding.positiveButton.setOnClickListener(listener);
    }

    public void setNegativeBtnText(@StringRes int text) {
        mBinding.negativeButton.setVisibility(View.VISIBLE);
        mBinding.negativeButton.setText(text);
    }

    public void setPositiveBtnText(@StringRes int text) {
        mBinding.positiveButton.setText(text);
    }
}
