// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.core.net.http;

import android.content.Context;
import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.google.gson.JsonSyntaxException;
import com.volcengine.vertcdemo.common.AppExecutors;
import com.volcengine.vertcdemo.common.GsonUtils;
import com.volcengine.vertcdemo.core.BuildConfig;
import com.volcengine.vertcdemo.core.R;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.AppTokenExpiredEvent;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.ErrorTool;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.core.net.ServerResponse;
import com.volcengine.vertcdemo.utils.AppUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpRequestHelper {
    /*
     可以填入提供的测试服务器域名，上线正式时，需要部署自己的服务端并更换为自己的服务端域名
     */
    private static final String LOGIN_URL = BuildConfig.HEAD_URL + "login";

    private static final String COMMON_URL = BuildConfig.HEAD_URL + "common";

    private static final String TAG = "HttpRequestHelper";

    public static final OkHttpClient DEFAULT_OKHTTP_CLIENT = new OkHttpClient.Builder()
            .hostnameVerifier((hostname, session) -> true)
            .build();

    @AnyThread
    public static <T> void sendPost(JSONObject params,
                                    Class<T> resultClass,
                                    @NonNull IRequestCallback<ServerResponse<T>> callBack) {
        AppExecutors.networkIO().execute(() -> sendPost(LOGIN_URL, params, resultClass, callBack));
    }

    @AnyThread
    public static <T> void sendCommonPost(JSONObject params,
                                    Class<T> resultClass,
                                    @NonNull IRequestCallback<ServerResponse<T>> callBack) {
        AppExecutors.networkIO().execute(() -> sendPost(COMMON_URL, params, resultClass, callBack));
    }


    @WorkerThread
    public static <T> void sendPost(@NonNull String url,
                                    JSONObject params,
                                    @Nullable Class<T> resultClass,
                                    @NonNull IRequestCallback<ServerResponse<T>> callBack) {
        try {
            Context context = AppUtil.getApplicationContext();
            String language = context.getString(R.string.language_code);
            params.put("language", language);
        } catch (JSONException ignored) {

        }
        RequestBody body = RequestBody.create(params.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = DEFAULT_OKHTTP_CLIENT.newCall(request);

        try (final Response response = call.execute()) {
            Log.d(TAG, "Request: " + params);
            if (!response.isSuccessful()) {
                throw new IOException("http code = " + response.code());
            }

            final ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("ResponseBody is null");
            }

            JSONObject json = new JSONObject(responseBody.string());
            Log.d(TAG, "Response: " + json);

            final int code = json.optInt("code");
            final String message = json.optString("message");

            if (code == ErrorTool.ERROR_CODE_TOKEN_EXPIRED || code == ErrorTool.ERROR_CODE_TOKEN_EMPTY) {
                SolutionDataManager.ins().setToken("");
                SolutionDemoEventManager.post(new AppTokenExpiredEvent());
            } else if (code != 200) {
                throw new NetworkException(code, message);
            }

            final long timestamp = json.optLong("timestamp");
            Object responseObject = json.opt("response");

            T data;
            if (responseObject == null
                    || resultClass == null
                    || resultClass == Void.class) {
                data = null;
            } else {
                data = GsonUtils.gson().fromJson(responseObject.toString(), resultClass);
            }

            final ServerResponse<T> sr = ServerResponse.create(code, message, data, timestamp);
            AppExecutors.mainThread().execute(() -> callBack.onSuccess(sr));
        } catch (NetworkException e) {
            Log.d(TAG, "post fail url:" + url, e);
            AppExecutors.mainThread().execute(() -> callBack.onError(e.code, e.getMessage()));
        } catch (IOException | JsonSyntaxException | JSONException e) {
            Log.d(TAG, "post fail url:" + url, e);
            AppExecutors.mainThread().execute(() -> callBack.onError(NetworkException.CODE_ERROR, e.getMessage()));
        }
    }
}
