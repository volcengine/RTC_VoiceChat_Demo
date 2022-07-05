package com.volcengine.vertcdemo.core.annotation;


import static com.volcengine.vertcdemo.core.annotation.UserStatus.USER_STATUS_AUDIENCE_INTERACTING;
import static com.volcengine.vertcdemo.core.annotation.UserStatus.USER_STATUS_AUDIENCE_INVITING;
import static com.volcengine.vertcdemo.core.annotation.UserStatus.USER_STATUS_CO_HOSTING;
import static com.volcengine.vertcdemo.core.annotation.UserStatus.USER_STATUS_HOST_INVITING;
import static com.volcengine.vertcdemo.core.annotation.UserStatus.USER_STATUS_OTHER;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        USER_STATUS_OTHER,
        USER_STATUS_HOST_INVITING,
        USER_STATUS_CO_HOSTING,
        USER_STATUS_AUDIENCE_INVITING,
        USER_STATUS_AUDIENCE_INTERACTING
})
@Retention(RetentionPolicy.SOURCE)
public @interface UserStatus {
    /**
     * 其它
     */
    int USER_STATUS_OTHER = 1;
    /**
     * 主播连麦邀请中
     */
    int USER_STATUS_HOST_INVITING = 2;
    /**
     * 主播连麦互动中
     */
    int USER_STATUS_CO_HOSTING = 3;
    /**
     * 观众连麦邀请中
     */
    int USER_STATUS_AUDIENCE_INVITING = 4;
    /**
     * 观众连麦互动中
     */
    int USER_STATUS_AUDIENCE_INTERACTING = 5;
}