package com.volcengine.vertcdemo;

import android.app.Application;

import com.ss.video.rtc.demo.basic_module.utils.SPUtils;
import com.ss.video.rtc.demo.basic_module.utils.Utilities;

public class VeRTCApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SPUtils.initSP(this, "ve_rtc_application_sp");
        Utilities.initApp(this);
        new CrashHandler(this);
    }
}
