// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.common;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.volcengine.vertcdemo.core.R;
import com.volcengine.vertcdemo.core.databinding.LayoutWarningEditTextBinding;

import java.util.regex.Pattern;

public class WarningEditText extends LinearLayout {

    private LayoutWarningEditTextBinding mViewBinding;

    private boolean mIsOverflow;
    private int mLength = Integer.MAX_VALUE;
    private String mInputRegex = ".*";
    private Callback mCallback;

    private final Runnable mAutoDismiss = new Runnable() {
        @Override
        public void run() {
            mViewBinding.warningTv.setVisibility(INVISIBLE);
        }
    };

    private final TextWatcher mTextWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(@NonNull Editable s) {
            checkInput(s.toString());
        }
    };

    public WarningEditText(Context context) {
        super(context);
        initView();
    }

    public WarningEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WarningEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.layout_warning_edit_text, this);
        mViewBinding = LayoutWarningEditTextBinding.bind(view);

        mViewBinding.inputEt.addTextChangedListener(mTextWatch);
        setRegex(mInputRegex, mLength, null);
    }

    public void setInputText(String text) {
        mViewBinding.inputEt.setText(text);
        checkInput(text);
    }

    public void setInputText(int textRes) {
        mViewBinding.inputEt.setText(textRes);
        checkInput(mViewBinding.inputEt.getText().toString());
    }

    public void setHintText(String text) {
        mViewBinding.inputEt.setHint(text);
    }

    public void setHintText(int textRes) {
        mViewBinding.inputEt.setHint(textRes);
    }

    public void setRegex(String regex, int length, Callback callback) {
        mInputRegex = regex == null ? "" : regex;
        length = Math.max(0, length);
        mLength = length;
        mCallback = callback;

        InputFilter inputFilter = new LengthFilterWithCallback(length, (overflow) -> {
            mIsOverflow = overflow;
            checkInput(mViewBinding.inputEt.getText().toString());
        });
        InputFilter[] inputFilters = new InputFilter[]{inputFilter};
        mViewBinding.inputEt.setFilters(inputFilters);
    }

    public void setWarningText(String text) {
        mViewBinding.warningTv.setText(text);
    }

    public void setWarningText(int textRes) {
        mViewBinding.warningTv.setText(textRes);
    }

    private void checkInput(String input) {
        mViewBinding.warningTv.removeCallbacks(mAutoDismiss);
        if (Pattern.matches(mInputRegex, input)) {
            if (mCallback != null) {
                mCallback.checkResult(true);
            }
            if (mIsOverflow) {
                mViewBinding.warningTv.setVisibility(View.VISIBLE);
                mViewBinding.warningTv.postDelayed(mAutoDismiss, 2500);
            } else {
                mViewBinding.warningTv.setVisibility(View.INVISIBLE);
            }
        } else {
            if (mCallback != null) {
                mCallback.checkResult(false);
            }
            mViewBinding.warningTv.setVisibility(View.VISIBLE);
            mViewBinding.warningTv.postDelayed(mAutoDismiss, 2500);
        }
    }

    public String getInputText() {
        return mViewBinding.inputEt.getText().toString();
    }

    public interface Callback {

        void checkResult(boolean valid);
    }
}
