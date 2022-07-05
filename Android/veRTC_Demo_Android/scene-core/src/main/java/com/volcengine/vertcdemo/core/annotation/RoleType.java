package com.volcengine.vertcdemo.core.annotation;

import static com.volcengine.vertcdemo.core.annotation.RoleType.AUDIENCE;
import static com.volcengine.vertcdemo.core.annotation.RoleType.HOST;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({AUDIENCE, HOST})
@Retention(RetentionPolicy.SOURCE)
public @interface RoleType {
    int AUDIENCE = 1;
    int HOST = 2;

}
