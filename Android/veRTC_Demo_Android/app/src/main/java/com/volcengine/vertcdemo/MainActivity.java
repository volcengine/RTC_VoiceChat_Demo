package com.volcengine.vertcdemo;

import android.os.Bundle;

import com.ss.video.rtc.demo.basic_module.acivities.BaseActivity;
import com.ss.video.rtc.demo.basic_module.utils.WindowUtils;
import com.volcengine.vertcdemo.utils.AgreementManager;

public class MainActivity extends BaseActivity implements AgreementManager.ResultCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AgreementManager.check(this);
    }

    @Override
    public void onResult(boolean agree) {
        if (agree) {
            setContentView(R.layout.activity_main);
        } else {
            finish();
        }
    }

    @Override
    protected void setupStatusBar() {
        WindowUtils.setLayoutFullScreen(getWindow());
    }
}
