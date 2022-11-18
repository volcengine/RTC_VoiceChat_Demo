package com.volcengine.vertcdemo.core.net.rts;

import static com.ss.bytertc.engine.type.UserMessageSendResult.USER_MESSAGE_SEND_RESULT_SUCCESS;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.ss.bytertc.engine.RTCVideo;
import com.ss.bytertc.engine.type.LoginErrorCode;
import com.ss.video.rtc.demo.basic_module.utils.AppExecutors;
import com.ss.video.rtc.demo.basic_module.utils.Utilities;
import com.volcengine.vertcdemo.core.R;
import com.volcengine.vertcdemo.core.SolutionDataManager;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.eventbus.TokenExpiredEvent;
import com.volcengine.vertcdemo.core.net.IBroadcastListener;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.core.net.ServerResponse;

import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RTM网络请求基础类
 */
public abstract class RTSBaseClient {
    private static final String TAG = "RTSBaseClient";
    public static final int ERROR_CODE_USERNAME_SAME = 414;
    public static final int ERROR_CODE_ROOM_FULL = 507;
    public static final int ERROR_CODE_DEFAULT = -1;

    @NonNull
    private final RTCVideo mRTCVideo;
    /***是否初始化业务服务器完成*/
    private boolean mInitBizServerCompleted;
    private LoginCallBack mLoginCallback;

    @NonNull
    protected final RTSInfo mRtmInfo;
    /*** RTM请求集合，Key:发送消息id; value为请求回调*/
    protected final ConcurrentHashMap<Long, IRequestCallback<? extends RTSBizResponse>> mCallBacksWithMsgId = new ConcurrentHashMap<>();
    /*** RTM请求集合，Key:请求requestId; value为请求回调及数据类型class*/
    protected final ConcurrentHashMap<String, RTSRequest<? extends RTSBizResponse>> mCallBacksWithRequestId = new ConcurrentHashMap<>();
    /*** RTM通知消息监听器*/
    protected final ConcurrentHashMap<String, IBroadcastListener<? extends RTSBizInform>> mEventListeners = new ConcurrentHashMap<>();


    public RTSBaseClient(@NonNull RTCVideo engine, @NonNull RTSInfo rtmInfo) {
        mRTCVideo = engine;
        mRtmInfo = rtmInfo;
    }

    /**
     * 登陆RTM,必须先登录注册一个 uid，才能发送房间外消息和向业务服务器发送消息
     * https://www.volcengine.com/docs/6348/70080#RTCEngine-login
     *
     * @param token 登陆token
     */
    public void login(@NonNull String token, @NonNull LoginCallBack callback) {
        mLoginCallback = callback;
        final String userId = SolutionDataManager.ins().getUserId();
        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(userId)) {
            notifyLoginResult(LoginCallBack.DEFAULT_FAIL_CODE,
                    "login fail because params is illegal :"
                            + "token:" + token
                            + ",uid:" + userId
                            + ",mRTCEngine:" + mRTCVideo);
            return;
        }
        mRTCVideo.login(token, userId);
    }

    /**
     * 登录结果回调,调用 login 后，会收到此回调
     * https://www.volcengine.com/docs/6348/70081#IRTCEngineEventHandler-onloginresult
     *
     * @param uid     登陆用户uid
     * @param code    登陆rtm结果代码
     * @param elapsed 从调用 login 接口开始到返回结果所用时长。单位为 ms
     */
    public void onLoginResult(String uid, int code, int elapsed) {
        if (TextUtils.isEmpty(mRtmInfo.serverUrl) || TextUtils.isEmpty(mRtmInfo.serverSignature)) {
            notifyLoginResult(LoginCallBack.DEFAULT_FAIL_CODE,
                    "onLoginResult fail because params is illegal :"
                            + "mRTCEngine:" + mRTCVideo
                            + ",mRtmInfo:" + mRtmInfo);
            return;
        }
        if (code == LoginErrorCode.LOGIN_ERROR_CODE_SUCCESS) {
            setServerParams(mRtmInfo.serverSignature, mRtmInfo.serverUrl);
        } else {
            notifyLoginResult(code, "onLoginResult fail because");
        }
    }

    /**
     * 登出RTM,调用本接口登出后，无法调用房间外消息以及端到服务器消息相关的方法或收到相关回调。
     * https://www.volcengine.com/docs/6348/70080#logout
     */
    public void logout() {
        Log.d(TAG, "logout");
        mRTCVideo.logout();
    }

    /**
     * 设置业务服务器参数
     * 1. 用户必须调用 login 登录后，才能调用本接口;
     * 2. 客户端调用 sendServerMessage 或 sendServerBinaryMessage 发送消息给业务服务器之前，
     * 必须需要设置有效签名和业务服务器地址
     * https://www.volcengine.com/docs/6348/70080#setserverparams
     *
     * @param signature 签名
     * @param url       业务服务器地址
     */
    public void setServerParams(String signature, String url) {
        if (TextUtils.isEmpty(signature) || TextUtils.isEmpty(url)) {
            notifyLoginResult(LoginCallBack.DEFAULT_FAIL_CODE, "setServerParams params is illegal :"
                    + "mRTCEngine:" + mRTCVideo
                    + "signature:" + signature
                    + ",url:" + url);
            return;
        }
        mRTCVideo.setServerParams(signature, url);
    }

    /**
     * 设置业务服务器参数的返回结果
     *
     * @param error 设置业务服务器错误的错误码
     */
    public void onServerParamsSetResult(int error) {
        if (error != 200) {
            notifyLoginResult(error, "onServerParamsSetResult fail");
            return;
        }
        notifyLoginResult(LoginCallBack.SUCCESS, "");
        mInitBizServerCompleted = true;
    }

    /**
     * 客户端给业务服务器发送文本消息,发送的文本消息内容消息不超过 62KB
     * https://www.volcengine.com/docs/6348/70080#RTCEngine-sendservermessage
     *
     * @return 消息id
     */
    private <T extends RTSBizResponse> long sendServerMessage(String message, IRequestCallback<T> callBack) {
        if (TextUtils.isEmpty(message)) {
            notifyRequestFail(ERROR_CODE_DEFAULT, "sendServerMessage fail mRTCEngine:" + mRTCVideo + ",message:" + message, callBack);
            return ERROR_CODE_DEFAULT;
        }
        Log.e(TAG, "sendServerMessage message:" + message);
        long msgId = mRTCVideo.sendServerMessage(message);
        if (msgId == ERROR_CODE_DEFAULT && callBack != null) {
            notifyRequestFail(ERROR_CODE_DEFAULT, "sendServerMessage fail msgId:" + msgId, callBack);
            return ERROR_CODE_DEFAULT;
        }
        synchronized (mCallBacksWithMsgId) {
            if (callBack != null) {
                mCallBacksWithMsgId.put(msgId, callBack);
            }
        }
        return msgId;
    }


    /**
     * 给业务服务器发送消息的回调，当调用 sendServerMessage 或 sendServerBinaryMessage 接口发送消息后，会收到此回调
     * https://www.volcengine.com/docs/6348/70081#IRTCEngineEventHandler-onservermessagesendresult
     */
    public void onServerMessageSendResult(long messageId, int error) {
        IRequestCallback callback = mCallBacksWithMsgId.get(messageId);
        if (callback != null && error != USER_MESSAGE_SEND_RESULT_SUCCESS) {
            notifyRequestFail(ERROR_CODE_DEFAULT, "sendServerMessage fail error:" + error, callback);
        }
        mCallBacksWithMsgId.remove(messageId);
    }

    /**
     * 组装RTM业务消息，并发送
     *
     * @param eventName   事件名称
     * @param roomId      房间号
     * @param content     事件需要的参数
     * @param resultClass 返回数据的class
     * @param callback    回调接口
     */
    public <T extends RTSBizResponse> void sendServerMessage(String eventName,
                                                             String roomId,
                                                             JsonObject content,
                                                             @Nullable Class<T> resultClass,
                                                             @Nullable IRequestCallback<T> callback) {
        Log.e(TAG, "sendServerMessage eventName:" + eventName + ",content:" + content);
        if (!mInitBizServerCompleted) {
            String msg = "sendServerMessage failed mInitBizServerCompleted: false";
            notifyRequestFail(ERROR_CODE_DEFAULT, msg, callback);
            Log.e(TAG, msg);
            return;
        }
        if (content == null) {
            content = new JsonObject();
        }
        content.addProperty("login_token", SolutionDataManager.ins().getToken());
        String requestId = String.valueOf(UUID.randomUUID());
        JsonObject message = new JsonObject();
        message.addProperty("app_id", mRtmInfo.appId);
        message.addProperty("room_id", roomId);
        message.addProperty("user_id", SolutionDataManager.ins().getUserId());
        message.addProperty("event_name", eventName);
        message.addProperty("content", content.toString());
        message.addProperty("request_id", requestId);
        message.addProperty("device_id", SolutionDataManager.ins().getDeviceId());
        long msgId = sendServerMessage(message.toString(), callback);
        if (msgId > 0) {
            RTSRequest<T> request = new RTSRequest<>(eventName, callback, resultClass);
            mCallBacksWithRequestId.put(requestId, request);
        }
    }

    /**
     * 收到RTM业务请求回调消息及通知消息，并解析
     */
    public void onMessageReceived(String uid, String message) {
        try {
            JSONObject messageJson = new JSONObject(message);
            String messageType = messageJson.getString("message_type");
            if (TextUtils.equals(messageType, ServerResponse.MESSAGE_TYPE_RETURN)) {
                String requestId = messageJson.getString("request_id");
                RTSRequest<?> request = mCallBacksWithRequestId.remove(requestId);
                if (request == null) {
                    Log.e(TAG, "onMessageReceived request is null");
                    return;
                }
                Log.e(TAG, String.format("onMessageReceived (%s): %s", request.eventName, message));

                final int code = messageJson.optInt("code");
                if (code == 200) {
                    final String data = messageJson.optString("response");
                    AppExecutors.mainThread().execute(() -> request.notifySuccess(data));
                } else {
                    final String msg;
                    if (code == 450) {
                        SolutionDemoEventManager.post(new TokenExpiredEvent());
                        msg = messageJson.optString("message");
                    } else if (code == 430) {
                        msg = Utilities.getApplicationContext().getString(R.string.error_msg_sensitive_word_input);
                    } else {
                        msg = messageJson.optString("message");
                    }
                    AppExecutors.mainThread().execute(() -> request.notifyError(code, msg));
                }
            } else if (TextUtils.equals(messageType, ServerResponse.MESSAGE_TYPE_INFORM)) {
                String event = messageJson.getString("event");
                if (!TextUtils.isEmpty(event)) {
                    IBroadcastListener<?> eventListener = mEventListeners.get(event);
                    if (eventListener != null) {
                        String dataStr = messageJson.optString("data");
                        Log.e(TAG, String.format("onMessageReceived broadcast: event: %s \n message: %s", event, dataStr));
                        eventListener.onData(dataStr);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onMessageReceived parse message failed uid:" + uid + ",message:" + message);
        }
    }

    private static void notifyRequestFail(int code, String msg, IRequestCallback<?> callback) {
        AppExecutors.mainThread().execute(() -> {
            if (callback == null) return;
            callback.onError(code, msg);
        });
    }

    private void notifyLoginResult(int code, String msg) {
        AppExecutors.mainThread().execute(() -> {
            if (mLoginCallback == null) return;
            mLoginCallback.notifyLoginResult(code, msg);
            mLoginCallback = null;
        });
    }

    /**
     * 登陆成功或者失败的回调
     */
    public interface LoginCallBack {
        int SUCCESS = 200;
        int DEFAULT_FAIL_CODE = -1;

        /**
         * @param resultCode 登陆结果码，如果为200则为成功
         * @param message    失败提示信息
         */
        void notifyLoginResult(int resultCode, String message);
    }
}

