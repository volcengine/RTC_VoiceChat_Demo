package com.volcengine.vertcdemo.core.net;

import com.ss.video.rtc.demo.basic_module.utils.Utilities;
import com.volcengine.vertcdemo.core.R;

public class ErrorTool {

    public static final int ERROR_CODE_TOKEN_EXPIRED = 450; // token 过期，需要退出所有的业务页面，跳转到登录页面
    public static final int ERROR_CODE_TOKEN_EMPTY = 451; // token 为空，需要退出所有的业务页面，跳转到登录页面

    public static String getErrorMessageByErrorCode(int errorCode, String serverMessage) {
        String message = serverMessage;
        switch (errorCode) {
            case -1011:
                message = getString(R.string.server_error_message_bad_server);
                break;
            case -101:
                message = getString(R.string.server_error_message_failed);
                break;
            case -1:
                message = getString(R.string.server_error_message_unknown);
                break;
            case 200:
                message = getString(R.string.server_error_message_success);
                break;
            case 400:
                message = getString(R.string.server_error_message_invalid_argument);
                break;
            case 404:
                message = getString(R.string.server_error_message_user_not_found);
                break;
            case 406:
                message = getString(R.string.server_error_message_over_room_limit);
                break;
            case 416:
                message = getString(R.string.server_error_message_not_authorized);
                break;
            case 418:
                message = getString(R.string.server_error_message_disconnect_time_out);
                break;
            case 419:
                message = getString(R.string.server_error_message_room_user_inactive);
                break;
            case 422:
                message = getString(R.string.server_error_message_room_disbanded);
                break;
            case 430:
                message = getString(R.string.server_error_message_sensitive_words);
                break;
            case 440:
                message = getString(R.string.server_error_message_verify_code_expired);
                break;
            case 441:
                message = getString(R.string.server_error_message_verify_code_invalid);
                break;
            case ERROR_CODE_TOKEN_EXPIRED:
            case ERROR_CODE_TOKEN_EMPTY:
                // 451是token为空，同450的处理
                message = getString(R.string.server_error_message_token_expired);
                break;
            case 472:
                message = getString(R.string.server_error_message_interact_number_limit);
                break;
            case 500:
                message = getString(R.string.server_error_message_server_internal_error);
                break;
            case 504:
                message = getString(R.string.server_error_message_transfer_host_role_failed);
                break;
            case 506:
                message = getString(R.string.server_error_message_mic_number_limit);
                break;
            case 622:
                message = getString(R.string.server_error_message_user_inviting);
                break;
            case 630:
                message = getString(R.string.server_error_message_bid_failed);
                break;
            case 632:
                message = getString(R.string.server_error_message_anchor_co_hosting);
                break;
            case 643:
                message = getString(R.string.server_error_message_anchor_linking_audience);
                break;
            case 644:
                message = getString(R.string.server_error_message_anchor_co_hosting2);
                break;
            case 645:
                message = getString(R.string.server_error_message_waiting_anchor_response);
                break;
            case 702:
                message = getString(R.string.server_error_message_generate_token_error);
                break;
            case 800:
                message = getString(R.string.server_error_message_set_app_info_error);
                break;
            case 801:
                message = getString(R.string.server_error_message_set_app_info_error);
                break;
            case 802:
                message = getString(R.string.server_error_message_traffic);
                break;
            case 803:
                message = getString(R.string.server_error_message_traffic);
                break;
            case 804:
                message = getString(R.string.server_error_message_vod_failed);
                break;
            case 805:
                message = getString(R.string.server_error_message_tw_error);
                break;
            case 806:
                message = getString(R.string.server_error_message_bid_failed);
                break;
        }

        return message;
    }

    private static String getString(int res) {
        return Utilities.getApplicationContext().getString(res);
    }
}