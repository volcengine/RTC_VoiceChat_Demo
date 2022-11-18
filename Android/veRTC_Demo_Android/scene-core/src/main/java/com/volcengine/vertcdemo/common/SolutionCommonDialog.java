package com.volcengine.vertcdemo.common;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import com.volcengine.vertcdemo.core.R;

/**
 * 场景化工程通用对话框
 */
public class SolutionCommonDialog extends AppCompatDialog {
    private TextView mTitleTv;
    private TextView mMessageTv;
    private Button mPositiveBtn;
    private Button mNegativeBtn;
    private View mDivider;

    public SolutionCommonDialog(Context context) {
        super(context, R.style.SolutionCommonDialog);
        this.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(R.layout.dialog_solution_common, (ViewGroup)null);
        this.mTitleTv = (TextView)view.findViewById(R.id.dialog_title_tv);
        this.mMessageTv = (TextView)view.findViewById(R.id.dialog_msg_tv);
        this.mPositiveBtn = (Button)view.findViewById(R.id.dialog_positive_btn);
        this.mNegativeBtn = (Button)view.findViewById(R.id.dialog_negative_btn);
        this.mDivider = view.findViewById(R.id.dialog_btn_divider);
        this.setContentView(view, new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        view.setOnClickListener((v) -> {
            this.dismiss();
        });
    }

    public void setMessage(String txt) {
        this.mMessageTv.setText(txt);
    }

    public void setNegativeListener(View.OnClickListener listener) {
        this.mDivider.setVisibility(View.VISIBLE);
        this.mNegativeBtn.setVisibility(View.VISIBLE);
        this.mNegativeBtn.setOnClickListener(listener);
    }

    public void setPositiveListener(View.OnClickListener listener) {
        this.mPositiveBtn.setOnClickListener(listener);
    }

    public void setNegativeBtnText(int resId) {
        this.mNegativeBtn.setText(resId);
    }

    public void setNegativeBtnText(String text) {
        this.mNegativeBtn.setText(text);
    }

    public void setPositiveBtnText(int resId) {
        this.mPositiveBtn.setText(resId);
    }

    public void setPositiveBtnText(String text) {
        this.mPositiveBtn.setText(text);
    }
}
