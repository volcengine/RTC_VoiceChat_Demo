// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.core.net;

import com.volcengine.vertcdemo.core.R;
import com.volcengine.vertcdemo.utils.AppUtil;

public class ErrorTool {

    public static final int ERROR_CODE_TOKEN_EXPIRED = 450; // token 过期，需要退出所有的业务页面，跳转到登录页面
    public static final int ERROR_CODE_TOKEN_EMPTY = 451; // token 为空，需要退出所有的业务页面，跳转到登录页面

    public static String getErrorMessageByErrorCode(int errorCode, String serverMessage) {
        String message = serverMessage;
        switch (errorCode) {
            case -1011:
                message = getString(R.string.network_message_1011);
                break;
            case -101:
                message = getString(R.string.network_message_101);
                break;
            case -1:
                message = getString(R.string.server_error_message_unknown);
                break;
            case 200:
                message = getString(R.string.server_error_message_success);
                break;
            case 400:
                message = getString(R.string.network_message_400);
                break;
            case 404:
                message = getString(R.string.network_message_404);
                break;
            case 406:
                message = getString(R.string.network_message_406);
                break;
            case 416:
                message = getString(R.string.network_message_416);
                break;
            case 418:
                message = getString(R.string.network_message_418);
                break;
            case 419:
                message = getString(R.string.network_message_419);
                break;
            case 422:
                message = getString(R.string.network_message_422);
                break;
            case 430:
                message = getString(R.string.network_message_430);
                break;
            case 440:
                message = getString(R.string.network_message_440);
                break;
            case 441:
                message = getString(R.string.network_message_441);
                break;
            case ERROR_CODE_TOKEN_EXPIRED:
            case ERROR_CODE_TOKEN_EMPTY:
                // 451是token为空，同450的处理
                message = getString(R.string.network_message_450);
                break;
            case 472:
                message = getString(R.string.network_message_472);
                break;
            case 500:
                message = getString(R.string.network_message_500);
                break;
            case 504:
                message = getString(R.string.network_message_504);
                break;
            case 506:
                message = getString(R.string.network_message_506);
                break;
            case 611:
                message = getString(R.string.network_message_611);
                break;
            case 622:
                message = getString(R.string.network_message_622);
                break;
            case 632:
                message = getString(R.string.network_message_632);
                break;
            case 643:
                message = getString(R.string.network_message_643);
                break;
            case 644:
                message = getString(R.string.network_message_644);
                break;
            case 645:
                message = getString(R.string.network_message_645);
                break;
            case 702:
                message = getString(R.string.network_message_702);
                break;
            case 801:
                message = getString(R.string.network_message_801);
                break;
            case 802:
                message = getString(R.string.network_message_802);
                break;
            case 804:
                message = getString(R.string.network_message_804);
                break;
            case 805:
                message = getString(R.string.network_message_805);
                break;
            case 806:
                message = getString(R.string.network_message_806);
                break;
        }

        return message;
    }

    public static boolean shouldLeaveRoom(int error) {
        return error == 422 || error == 472;
    }

    private static String getString(int res) {
        return AppUtil.getApplicationContext().getString(res);
    }
}