package com.volcengine.vertcdemo;

import android.app.Application;

import com.ss.video.rtc.demo.basic_module.utils.SPUtils;
import com.ss.video.rtc.demo.basic_module.utils.Utilities;
import com.volcengine.vertcdemo.core.startup.StartupManager;

public class VeRTCApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SPUtils.initSP(this, "meeting_sp");
        Utilities.initApp(this);
        StartupManager.invoke(this);
    }
}
