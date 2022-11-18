package com.volcengine.vertcdemo.utils;

import com.volcengine.vertcdemo.core.SolutionDataManager;

public class DeleteAccountManager {
    public static void delete() {
        SolutionDataManager.ins().logout();
    }
}
