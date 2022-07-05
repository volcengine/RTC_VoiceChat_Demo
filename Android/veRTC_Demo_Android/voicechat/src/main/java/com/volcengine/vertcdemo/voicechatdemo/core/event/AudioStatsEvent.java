package com.volcengine.vertcdemo.voicechatdemo.core.event;

public class AudioStatsEvent {
    public int rtt;
    public float upload;
    public float download;

    public AudioStatsEvent(int rtt, float upload, float download) {
        this.rtt = rtt;
        this.upload = upload;
        this.download = download;
    }
}
