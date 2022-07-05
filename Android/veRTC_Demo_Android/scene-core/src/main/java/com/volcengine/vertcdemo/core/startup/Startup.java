package com.volcengine.vertcdemo.core.startup;

import android.app.Application;

import androidx.annotation.MainThread;

/**
 * 子工程通过配置 {@code meta-data} 信息，注入到 {@link Application#onCreate()} 中
 * 使用 "START_UP_" 开头的 meta-data key 标记需要调用的类
 * <pre>{@code
 * <meta-data
 *     android:name="START_UP_INTERACTIVE_LIVE"
 *     android:value="com.volcengine.vertcdemo.interactivelivedemo.core.InteractiveLiveStartup" />
 * }</pre>
 * <p>
 * 各模块应该以模块名称创建后缀 {@code START_UP_${YOU-MODULE-NAME}}，避免因重复而遭到覆盖，例如 {@code START_UP_INTERACTIVE_LIVE}
 *
 * @see StartupManager
 */
public interface Startup {
    @MainThread
    void call(Application application);
}
