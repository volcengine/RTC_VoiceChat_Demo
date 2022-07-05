package com.volcengine.vertcdemo.voicechatdemo.core;

import static com.ss.bytertc.engine.data.AudioMixingType.AUDIO_MIXING_TYPE_PLAYOUT_AND_PUBLISH;

import android.content.Context;
import android.util.Log;

import com.ss.bytertc.engine.RTCEngine;
import com.ss.bytertc.engine.RTCRoomConfig;
import com.ss.bytertc.engine.UserInfo;
import com.ss.bytertc.engine.data.AudioMixingConfig;
import com.ss.bytertc.engine.data.MuteState;
import com.ss.bytertc.engine.data.StreamIndex;
import com.ss.video.rtc.demo.basic_module.utils.AppExecutors;
import com.ss.video.rtc.demo.basic_module.utils.Utilities;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.rtm.RTCEventHandlerWithRTM;
import com.volcengine.vertcdemo.core.net.rtm.RtmInfo;
import com.volcengine.vertcdemo.voicechatdemo.core.event.AudioStatsEvent;
import com.volcengine.vertcdemo.voicechatdemo.core.event.AudioVolumeEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VoiceChatRTCManager {

    private static final String TAG = "VoiceChatRTCManager";

    private static VoiceChatRTCManager sInstance;

    private VoiceChatRtmClient mRTMClient;

    private final RTCEventHandlerWithRTM mIRTCEngineEventHandler = new RTCEventHandlerWithRTM() {

        @Override
        public void onRoomStateChanged(String roomId, String uid, int state, String extraInfo) {
            super.onRoomStateChanged(roomId, uid, state, extraInfo);
            Log.d(TAG, String.format("onRoomStateChanged: %s, %s, %d, %s", roomId, uid, state, extraInfo));
        }

        @Override
        public void onWarning(int warn) {
            super.onWarning(warn);
            Log.d(TAG, String.format("onWarning: %d", warn));
        }

        @Override
        public void onError(int err) {
            super.onError(err);
            Log.d(TAG, String.format("onError: %d", err));
        }

        @Override
        public void onLocalStreamStats(LocalStreamStats stats) {
            super.onLocalStreamStats(stats);
            AudioStatsEvent event = new AudioStatsEvent(
                    stats.audioStats.rtt, stats.audioStats.audioLossRate, 0);
            SolutionDemoEventManager.post(event);
        }

        @Override
        public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalRemoteVolume) {
            super.onAudioVolumeIndication(speakers, totalRemoteVolume);
            SolutionDemoEventManager.post(new AudioVolumeEvent(speakers));
        }
    };

    private RTCEngine mInstance;

    public static VoiceChatRTCManager ins() {
        if (sInstance == null) {
            sInstance = new VoiceChatRTCManager();
        }
        return sInstance;
    }

    public VoiceChatRtmClient getRTMClient() {
        return mRTMClient;
    }

    public void initEngine(RtmInfo info) {
        destroyEngine();
        mInstance = RTCEngine.createEngine(Utilities.getApplicationContext(), info.appId, mIRTCEngineEventHandler, null, null);
        mInstance.stopVideoCapture();
        mInstance.setAudioVolumeIndicationInterval(2000);
        mRTMClient = new VoiceChatRtmClient(mInstance, info);
        mIRTCEngineEventHandler.setBaseClient(mRTMClient);
        initBGMRes();
        Log.d(TAG, String.format("initEngine: %s", info));
    }

    private void initBGMRes() {
        AppExecutors.diskIO().execute(() -> {
            File bgmPath = new File(getExternalResourcePath(), "bgm/voicechat_bgm.mp3");
            if (!bgmPath.exists()) {
                File dir = new File(getExternalResourcePath() + "bgm");
                if (!dir.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    dir.mkdirs();
                }
                copyAssetFile(Utilities.getApplicationContext(), "voicechat_bgm.mp3", bgmPath.getAbsolutePath());
            }
        });
    }

    private String getExternalResourcePath() {
        return Utilities.getApplicationContext().getExternalFilesDir("assets").getAbsolutePath() + "/resource/";
    }

    public void destroyEngine() {
        Log.d(TAG, "destroyEngine");
        RTCEngine.destroyEngine(mInstance);
    }

    public void joinRoom(String roomId, String token, String userId) {
        Log.d(TAG, String.format("joinRoom: %s %s %s", roomId, userId, token));
        if (mInstance != null) {
            RTCRoomConfig config = new RTCRoomConfig(
                    RTCEngine.ChannelProfile.CHANNEL_PROFILE_LIVE_BROADCASTING,
                    true, true, true);
            mInstance.joinRoom(token, roomId, new UserInfo(userId, null), config);
        }
    }

    public void leaveRoom() {
        Log.d(TAG, "leaveRoom");
        if (mInstance != null) {
            mInstance.leaveRoom();
        }
    }

    public void startAudioCapture(boolean isStart) {
        Log.d(TAG, String.format("startAudioCapture: %b", isStart));
        if (mInstance != null) {
            if (isStart) {
                mInstance.startAudioCapture();
            } else {
                mInstance.stopAudioCapture();
            }
        }
    }

    public void startMuteAudio(boolean isStart) {
        Log.d(TAG, String.format("startMuteAudio: %b", isStart));
        if (mInstance != null) {
            MuteState state = isStart ? MuteState.MUTE_STATE_ON : MuteState.MUTE_STATE_OFF;
            mInstance.muteLocalAudio(state);
        }
    }

    public void startAudioMixing(boolean isStart) {
        Log.d(TAG, String.format("startAudioMixing: %b", isStart));
        if (mInstance != null) {
            if (isStart) {
                String bgmPath = getExternalResourcePath() + "bgm/voicechat_bgm.mp3";
                mInstance.getAudioMixingManager().preloadAudioMixing(0, bgmPath);
                AudioMixingConfig config = new AudioMixingConfig(AUDIO_MIXING_TYPE_PLAYOUT_AND_PUBLISH, -1);
                mInstance.getAudioMixingManager().startAudioMixing(0, bgmPath, config);
            } else {
                mInstance.getAudioMixingManager().stopAudioMixing(0);
            }
        }
    }

    public void resumeAudioMixing() {
        Log.d(TAG, "resumeAudioMixing");
        if (mInstance != null) {
            mInstance.getAudioMixingManager().resumeAudioMixing(0);
        }
    }

    public void pauseAudioMixing() {
        Log.d(TAG, "pauseAudioMixing");
        if (mInstance != null) {
            mInstance.getAudioMixingManager().pauseAudioMixing(0);
        }
    }

    public void adjustBGMVolume(int progress) {
        Log.d(TAG, String.format("adjustBGMVolume: %d", progress));
        if (mInstance != null) {
            mInstance.getAudioMixingManager().setAudioMixingVolume(0, progress, AUDIO_MIXING_TYPE_PLAYOUT_AND_PUBLISH);
        }
    }

    public void adjustUserVolume(int progress) {
        Log.d(TAG, String.format("adjustUserVolume: %d", progress));
        if (mInstance != null) {
            mInstance.setCaptureVolume(StreamIndex.STREAM_INDEX_MAIN, progress);
        }
    }


    public static boolean copyAssetFile(Context context, String srcName, String dstName) {
        try {
            InputStream in = context.getAssets().open(srcName);
            File outFile = new File(dstName);
            OutputStream out = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
