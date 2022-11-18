package com.volcengine.vertcdemo.voicechat.core;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class VoiceChatDataManager {

    public static final int DISABLE_ALLOW_APPLY = 1;
    public static final int ENABLE_ALLOW_APPLY = 2;

    @IntDef({ENABLE_ALLOW_APPLY, DISABLE_ALLOW_APPLY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AllowUserApplyType {
    }

    public static final int SEAT_STATUS_LOCKED = 0;
    public static final int SEAT_STATUS_UNLOCKED = 1;

    @IntDef({SEAT_STATUS_LOCKED, SEAT_STATUS_UNLOCKED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SeatStatus {
    }

    public static final int SEAT_OPTION_LOCK = 1;
    public static final int SEAT_OPTION_UNLOCK = 2;
    public static final int SEAT_OPTION_MIC_OFF = 3;
    public static final int SEAT_OPTION_MIC_ON = 4;
    public static final int SEAT_OPTION_END_INTERACT = 5;
    @IntDef({SEAT_OPTION_LOCK, SEAT_OPTION_UNLOCK, SEAT_OPTION_MIC_OFF, SEAT_OPTION_MIC_ON, SEAT_OPTION_END_INTERACT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SeatOption {
    }

    public static final int REPLY_TYPE_ACCEPT = 1;
    public static final int REPLY_TYPE_REJECT = 2;

    @IntDef({REPLY_TYPE_ACCEPT, REPLY_TYPE_REJECT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ReplyType {
    }


    public static final int MIC_OPTION_OFF = 0;
    public static final int MIC_OPTION_ON = 1;

    @IntDef({MIC_OPTION_OFF, MIC_OPTION_ON})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MicOption {
    }


    private static VoiceChatDataManager sInstance;

    public static VoiceChatDataManager ins() {
        if (sInstance == null) {
            sInstance = new VoiceChatDataManager();
        }
        return sInstance;
    }

    private boolean isAllowUserApply = false;
    private boolean isBGMOpening = false;
    private boolean hasNewApply = false;
    private int mBGMVolume = 100;
    private int mUserVolume = 100;
    private boolean isFirstSetBGMSwitch = true;
    private boolean isSelfApply = false;

    public void clearData() {
        isAllowUserApply = false;
        isBGMOpening = false;
        hasNewApply = false;
        mBGMVolume = 100;
        mUserVolume = 100;
        isFirstSetBGMSwitch = true;
        isSelfApply = false;
    }

    public boolean getAllowUserApply() {
        return isAllowUserApply;
    }

    public void setAllowUserApply(boolean isAllowUserApply) {
        this.isAllowUserApply = isAllowUserApply;
    }

    public void setBGMOpening(boolean isBGMOpening) {
        this.isBGMOpening = isBGMOpening;
    }

    public boolean getBGMOpening() {
        return isBGMOpening;
    }

    public void setBGMVolume(int bgmVolume) {
        mBGMVolume = bgmVolume;
    }

    public int getBGMVolume() {
        return mBGMVolume;
    }

    public void setUserVolume(int userVolume) {
        mUserVolume = userVolume;
    }

    public int getUserVolume() {
        return mUserVolume;
    }

    public boolean hasNewApply() {
        return hasNewApply;
    }

    public void setNewApply(boolean hasNewApply) {
        this.hasNewApply = hasNewApply;
    }

    public void setIsFirstSetBGMSwitch(boolean isFirstSetBGMSwitch) {
        this.isFirstSetBGMSwitch = isFirstSetBGMSwitch;
    }

    public boolean getIsFirstSetBGMSwitch() {
        return isFirstSetBGMSwitch;
    }

    public void setSelfApply(boolean isSelfApply) {
        this.isSelfApply = isSelfApply;
    }

    public boolean getSelfApply() {
        return isSelfApply;
    }
}
