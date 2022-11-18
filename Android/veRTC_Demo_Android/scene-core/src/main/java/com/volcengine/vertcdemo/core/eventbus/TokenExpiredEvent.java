package com.volcengine.vertcdemo.core.eventbus;

/**
 * token过期，由网络处理工具统一处理，业务页面接收该通知，当业务页面收到该通知时，应该退出当前页面
 */
public class TokenExpiredEvent {}
