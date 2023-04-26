// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.event;

/**
 * SDK 音频统计事件
 */
public class SDKAudioStatsEvent {
    public int rtt;
    public float upload;
    public float download;

    public SDKAudioStatsEvent(int rtt, float upload, float download) {
        this.rtt = rtt;
        this.upload = upload;
        this.download = download;
    }
}
