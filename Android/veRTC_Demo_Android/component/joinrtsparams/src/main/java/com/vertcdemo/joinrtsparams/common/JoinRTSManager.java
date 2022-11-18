package com.vertcdemo.joinrtsparams.common;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vertcdemo.joinrtsparams.bean.JoinRTSRequest;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.core.net.ServerResponse;
import com.volcengine.vertcdemo.core.net.http.HttpRequestHelper;
import com.volcengine.vertcdemo.core.net.rts.RTSInfo;

import org.json.JSONObject;

public class JoinRTSManager {

    private static final String TAG = "JoinRTSManager";

    public static void setAppInfoAndJoinRTM(JoinRTSRequest joinRTSRequest,
                                            IRequestCallback<ServerResponse<RTSInfo>> callBack) {
        if (joinRTSRequest == null) {
            if (callBack != null) {
                callBack.onError(-1, "input can not be empty");
            }
            return;
        }
        String message = null;

        JsonElement element = new Gson().toJsonTree(joinRTSRequest);
        JsonObject content = element.getAsJsonObject();

        if (TextUtils.isEmpty(Constants.APP_ID)) {
            message = "APPID";
        } else {
            content.addProperty("app_id", Constants.APP_ID);
        }
        if (TextUtils.isEmpty(Constants.APP_KEY)) {
            message = "APPKey";
        } else {
            content.addProperty("app_key", Constants.APP_KEY);
        }
        if (TextUtils.isEmpty(Constants.VOLC_AK)) {
            message = "AccessKeyID";
        } else {
            content.addProperty("volc_ak", Constants.VOLC_AK);
        }
        if (TextUtils.isEmpty(Constants.VOLC_SK)) {
            message = "SecretAccessKey";
        } else {
            content.addProperty("volc_sk", Constants.VOLC_SK);
        }

        if (!TextUtils.isEmpty(message)) {
            if (callBack != null) {
                callBack.onError(-1, String.format("%s is empty", message));
            }
            return;
        }
        try {
            JSONObject params = new JSONObject();
            params.put("event_name", "setAppInfo");
            params.put("content", content.toString());
            params.put("device_id", SolutionDataManager.ins().getDeviceId());

            Log.d(TAG, "setAppInfo params: " + params);
            HttpRequestHelper.sendPost(params, RTSInfo.class, callBack);
        } catch (Exception e) {
            Log.d(TAG, "setAppInfo failed", e);
            callBack.onError(-1, e.getMessage());
        }
    }
}
