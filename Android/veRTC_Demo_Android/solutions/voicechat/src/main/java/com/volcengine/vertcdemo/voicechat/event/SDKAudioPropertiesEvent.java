package com.volcengine.vertcdemo.voicechat.event;

import com.ss.bytertc.engine.data.AudioPropertiesInfo;

import java.util.List;

/**
 * SDK回调用户音量大小变化事件
 */
public class SDKAudioPropertiesEvent {

    public List<SDKAudioProperties> audioPropertiesList;

    public SDKAudioPropertiesEvent(List<SDKAudioProperties> audioPropertiesList) {
        this.audioPropertiesList = audioPropertiesList;
    }

    public static class SDKAudioProperties {
        public String userId;

        public AudioPropertiesInfo audioPropertiesInfo;

        public SDKAudioProperties(String userId, AudioPropertiesInfo audioPropertiesInfo) {
            this.userId = userId;
            this.audioPropertiesInfo = audioPropertiesInfo;
        }
    }
}