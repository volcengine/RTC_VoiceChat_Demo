// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.core.net.http;

import java.io.IOException;

public class NetworkException extends IOException {
    /**
     * 正常响应
     */
    public static final int CODE_OK = 200;

    /**
     * 一般性错误，未知原因
     */
    public static final int CODE_ERROR = -1;
    /**
     * Token 过期
     */
    public static final int CODE_TOKEN_EXPIRED = 450;

    /**
     * 输入内容包含敏感词
     */
    public static final int CODE_INPUT_INVALID = 430;

    public final int code;

    public NetworkException(int code, String message) {
        super(message);
        this.code = code;
    }

    public NetworkException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
